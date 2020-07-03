package com.spiraclesoftware.androidsample.features.transaction.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.GenericFastAdapter
import com.mikepenz.fastadapter.GenericItem
import com.mikepenz.fastadapter.adapters.GenericItemAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.databinding.TransactionListFragmentBinding
import com.spiraclesoftware.androidsample.features.transaction.list.TransactionListFragment.ListItemsTransformations.sortAndGroupByDay
import com.spiraclesoftware.androidsample.features.transaction.list.TransactionListFragment.ListItemsTransformations.toListItems
import com.spiraclesoftware.androidsample.features.transaction.list.TransactionListFragmentDirections.Companion.toRatesConverter
import com.spiraclesoftware.androidsample.features.transaction.list.TransactionListFragmentDirections.Companion.toTransactionDetail
import com.spiraclesoftware.androidsample.shared.domain.*
import com.spiraclesoftware.androidsample.shared.ui.DelightUI
import com.spiraclesoftware.androidsample.shared.ui.RetryCallback
import com.spiraclesoftware.core.data.EventObserver
import com.spiraclesoftware.core.data.NullableEventObserver
import com.spiraclesoftware.core.data.Resource
import com.spiraclesoftware.core.data.Status
import com.spiraclesoftware.core.extensions.string
import com.spiraclesoftware.core.utils.LanguageSwitcher
import kotlinx.android.synthetic.main.transaction__list__fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.temporal.ChronoUnit
import java.math.BigDecimal

class TransactionListFragment : Fragment() {

    private val viewModel by viewModel<TransactionListViewModel>()

    private lateinit var binding: TransactionListFragmentBinding
    private lateinit var fastAdapter: GenericFastAdapter
    private lateinit var itemAdapter: GenericItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = TransactionListFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.retryCallback = object : RetryCallback {
            override fun retry() {
                viewModel.refresh()
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.setupWithNavController(findNavController())

        toolbar.inflateMenu(R.menu.transaction_list_menu)
        toolbar.setOnMenuItemClickListener(::onMenuItemClicked)

        DelightUI.setupToolbarTitleAppearingOnScroll(toolbar, scrollView)

        fun setupFastItemAdapter() {
            itemAdapter = ItemAdapter.items()
            fastAdapter = FastAdapter.with(itemAdapter)
            fastAdapter.apply {
                onClickListener = { _, _, item, _ ->
                    when (item) {
                        is TransactionItem -> {
                            viewModel.openTransactionDetail(item.transaction.id)
                            true
                        }
                        else -> false
                    }
                }
            }
        }
        setupFastItemAdapter()

        fun setupRecyclerView() {
            val linearLayoutManager = LinearLayoutManager(requireContext())

            recyclerView.apply {
                layoutManager = linearLayoutManager
                adapter = fastAdapter
                itemAnimator = null
            }
        }
        setupRecyclerView()

        fun setupFilterSpinner() {

            fun createSpinnerAdapter(): ArrayAdapter<String> {
                return ArrayAdapter(
                    requireContext(),
                    R.layout.filter_spinner_item,
                    TransferDirectionFilter.values().map { string(it.stringRes) }
                ).also {
                    it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                }
            }

            filterSpinner.adapter = createSpinnerAdapter()

            filterSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}

                override fun onItemSelected(
                    parent: AdapterView<*>?, view: View?, position: Int, id: Long
                ) {
                    val transferDirectionFilter = TransferDirectionFilter.values()[position]
                    viewModel.setTransferDirectionFilter(transferDirectionFilter)
                }
            }
        }
        setupFilterSpinner()
    }

    private fun onMenuItemClicked(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_rates_converter -> {
                viewModel.openRatesConverter()
                return true
            }
            R.id.action_switch_locale -> {
                LanguageSwitcher.toggleLanguageAndRestart(requireContext())
                return true
            }
            R.id.action_refresh -> {
                viewModel.refresh()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        fun subscribeUi() {
            viewModel.transactionListFilter.observe(
                viewLifecycleOwner,
                Observer(::bindTransactionListFilter)
            )

            viewModel.listData.observe(
                viewLifecycleOwner,
                Observer(::bindListData)
            )

            viewModel.navigateToDetailAction.observe(
                viewLifecycleOwner,
                EventObserver { transactionId ->
                    findNavController().navigate(toTransactionDetail(transactionId.value))
                }
            )

            viewModel.navigateToRatesConverterAction.observe(
                viewLifecycleOwner,
                NullableEventObserver {
                    findNavController().navigate(toRatesConverter())
                }
            )
        }
        subscribeUi()
    }

    private fun bindTransactionListFilter(filter: TransactionListFilter) {
        filterSpinner.setSelection(filter.transferDirectionFilter.ordinal)
    }

    private fun bindListData(data: Triple<Account, Resource<List<Transaction>>, Resource<ConversionRates>>) {
        val (account, transactions, rates) = data
        binding.transactionListResource = transactions
        setTransactions(account, transactions, rates)
    }

    private fun setTransactions(
        account: Account,
        transactions: Resource<List<Transaction>>,
        rates: Resource<ConversionRates>
    ) {
        if (transactions.status == Status.SUCCESS && rates.status == Status.SUCCESS) {
            itemAdapter.set(transactions.data!!.sortAndGroupByDay().toListItems(account, rates.data!!))
        } else {
            scrollView.scrollTo(0, 0) // otherwise toolbar elevation wouldn't reset
            itemAdapter.set(emptyList())
        }
    }

    private object ListItemsTransformations {

        fun List<Transaction>.sortAndGroupByDay() =
            this.sortedByDescending { it.processingDate }
                .groupBy { it.processingDate.truncatedTo(ChronoUnit.DAYS) }

        fun Map<ZonedDateTime, List<Transaction>>.toListItems(
            account: Account,
            rates: ConversionRates
        ): List<GenericItem> {
            val listItems = arrayListOf<GenericItem>()

            this.forEach { (day, transactionsInDay) ->
                // header will be inserted at what used to be the end of the list before be added new items
                val headerPos = listItems.count()
                var contributionToBalance = Money(BigDecimal.ZERO, account.currency)

                transactionsInDay.forEach { transaction ->
                    listItems.add(TransactionItem(transaction))

                    contributionToBalance = contributionToBalance.add(
                        transaction.getContributionToBalance(rates, account.currency).amount
                    )
                }
                listItems.add(headerPos, HeaderItem(day, contributionToBalance))
            }

            return listItems
        }
    }
}
