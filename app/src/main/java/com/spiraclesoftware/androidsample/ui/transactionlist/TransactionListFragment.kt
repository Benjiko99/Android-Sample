package com.spiraclesoftware.androidsample.ui.transactionlist

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import co.zsmb.rainbowcake.base.OneShotEvent
import co.zsmb.rainbowcake.base.RainbowCakeFragment
import co.zsmb.rainbowcake.extensions.exhaustive
import co.zsmb.rainbowcake.koin.getViewModelFromFactory
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.GenericFastAdapter
import com.mikepenz.fastadapter.GenericItem
import com.mikepenz.fastadapter.adapters.GenericItemAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.shared.domain.*
import com.spiraclesoftware.androidsample.shared.ui.DelightUI
import com.spiraclesoftware.androidsample.ui.transactionlist.TransactionListFragmentDirections.Companion.toTransactionDetail
import com.spiraclesoftware.androidsample.ui.transactionlist.TransactionListViewModel.NavigateToDetailEvent
import com.spiraclesoftware.core.extensions.string
import com.spiraclesoftware.core.utils.LanguageSwitcher
import kotlinx.android.synthetic.main.transaction__list__fragment.*
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.temporal.ChronoUnit
import java.math.BigDecimal

class TransactionListFragment : RainbowCakeFragment<TransactionListViewState, TransactionListViewModel>() {

    override fun provideViewModel() = getViewModelFromFactory()
    override fun getViewResource() = R.layout.transaction__list__fragment

    private lateinit var fastAdapter: GenericFastAdapter
    private lateinit var itemAdapter: GenericItemAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fun setupToolbar() {
            toolbar.setupWithNavController(findNavController())
            DelightUI.setupToolbarTitleAppearingOnScroll(toolbar, scrollView)

            toolbar.inflateMenu(R.menu.transaction_list_menu)
            toolbar.setOnMenuItemClickListener(::onMenuItemClicked)
        }
        setupToolbar()

        fun setupFastItemAdapter() {
            itemAdapter = ItemAdapter.items()
            fastAdapter = FastAdapter.with(itemAdapter)
            fastAdapter.apply {
                onClickListener = { _, _, item, _ ->
                    when (item) {
                        is TransactionItem -> {
                            viewModel.onListItemClicked(item.transaction.id)
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
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val transferDirectionFilter = TransferDirectionFilter.values()[position]
                    viewModel.setTransferDirectionFilter(transferDirectionFilter)
                }
            }
        }
        setupFilterSpinner()

        retryButton.setOnClickListener { viewModel.reload() }
    }

    private fun onMenuItemClicked(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_switch_locale -> {
                LanguageSwitcher.toggleLanguageAndRestart(requireContext())
                return true
            }
            R.id.action_refresh -> {
                viewModel.reload()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        fun subscribeUi() {
            viewModel.listFilter.observe(
                viewLifecycleOwner,
                Observer(::bindTransactionListFilter)
            )
        }
        subscribeUi()
    }

    override fun render(viewState: TransactionListViewState) {
        when (viewState) {
            Loading -> {
                loadingIndicator.isVisible = true
                errorGroup.isVisible = false
            }
            is ListReady -> {
                loadingIndicator.isVisible = false
                recyclerView.isVisible = true
                errorGroup.isVisible = false

                setListItems(
                    viewState.account,
                    viewState.transactions,
                    viewState.conversionRates
                )
            }
            NetworkError -> {
                loadingIndicator.isVisible = false
                recyclerView.isVisible = false
                errorGroup.isVisible = true

                setEmptyListItems()
            }
        }.exhaustive
    }

    private fun setListItems(
        account: Account,
        transactions: List<Transaction>,
        rates: ConversionRates
    ) {
        itemAdapter.set(transactions.sortAndGroupByDay().toListItems(account, rates))
    }

    private fun setEmptyListItems() {
        itemAdapter.set(emptyList())
    }

    override fun onEvent(event: OneShotEvent) {
        when (event) {
            is NavigateToDetailEvent -> {
                findNavController().navigate(toTransactionDetail(event.id.value))
            }
        }
    }

    private fun bindTransactionListFilter(filter: TransactionListFilter) {
        filterSpinner.setSelection(filter.transferDirectionFilter.ordinal)
    }

    // TODO: Move to ViewModel
    private fun List<Transaction>.sortAndGroupByDay() =
        this.sortedByDescending { it.processingDate }
            .groupBy { it.processingDate.truncatedTo(ChronoUnit.DAYS) }

    private fun Map<ZonedDateTime, List<Transaction>>.toListItems(
        account: Account,
        rates: ConversionRates
    ): List<GenericItem> {
        val listItems = arrayListOf<GenericItem>()

        this.forEach { (day, transactionsInDay) ->
            // header will be inserted at what used to be the end of the list before be added new items
            val headerPos = listItems.count()
            var contributionToBalance = Money(BigDecimal.ZERO, account.currency)

            transactionsInDay.forEach { transaction ->
                listItems.add(
                    TransactionItem(
                        transaction
                    )
                )

                contributionToBalance = contributionToBalance.add(
                    transaction.getContributionToBalance(rates, account.currency).amount
                )
            }
            listItems.add(
                headerPos,
                HeaderItem(
                    day,
                    contributionToBalance
                )
            )
        }

        return listItems
    }

}
