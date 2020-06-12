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
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.databinding.TransactionListFragmentBinding
import com.spiraclesoftware.androidsample.features.transaction.list.TransactionListFragmentDirections.Companion.toTransactionDetail
import com.spiraclesoftware.androidsample.shared.domain.Transaction
import com.spiraclesoftware.androidsample.shared.domain.TransactionListFilter
import com.spiraclesoftware.androidsample.shared.domain.TransferDirectionFilter
import com.spiraclesoftware.androidsample.shared.ui.DelightUI
import com.spiraclesoftware.androidsample.shared.ui.RetryCallback
import com.spiraclesoftware.core.data.EventObserver
import com.spiraclesoftware.core.data.Resource
import com.spiraclesoftware.core.extensions.string
import com.spiraclesoftware.core.utils.LanguageSwitcher
import kotlinx.android.synthetic.main.transaction__list__fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class TransactionListFragment : Fragment() {

    private val viewModel by viewModel<TransactionListViewModel>()

    private lateinit var binding: TransactionListFragmentBinding
    private lateinit var fastItemAdapter: FastItemAdapter<TransactionItem>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = TransactionListFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.retryCallback = object : RetryCallback {
            override fun retry() {
                viewModel.retry()
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
            fastItemAdapter = FastItemAdapter()
            fastItemAdapter.apply {
                withSelectable(true)
                withOnClickListener { _, _, item, _ ->
                    viewModel.openTransactionDetail(item.transaction.id)
                    true
                }
            }
        }
        setupFastItemAdapter()

        fun setupRecyclerView() {
            val linearLayoutManager = LinearLayoutManager(requireContext())

            recyclerView.apply {
                layoutManager = linearLayoutManager
                adapter = fastItemAdapter
                itemAnimator = null
            }
        }
        setupRecyclerView()

        fun setupFilterSpinner() {

            fun createSpinnerAdapter(): ArrayAdapter<String> {
                return ArrayAdapter(
                    requireContext(),
                    R.layout.stylized_spinner_item,
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
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        fun subscribeUi() {
            viewModel.transactions.observe(
                viewLifecycleOwner,
                Observer(::bindTransactionsResource)
            )

            viewModel.transactionListFilter.observe(
                viewLifecycleOwner,
                Observer(::bindTransactionListFilter)
            )

            viewModel.navigateToDetailAction.observe(
                viewLifecycleOwner,
                EventObserver { transactionId ->
                    findNavController().navigate(toTransactionDetail(transactionId.value))
                }
            )
        }
        subscribeUi()
    }

    private fun bindTransactionsResource(resource: Resource<List<Transaction>>) {
        binding.transactionListResource = resource
        setTransactions(resource.data)
    }

    private fun setTransactions(transactions: List<Transaction>?) {
        fun toListItems(data: List<Transaction>?) =
            data?.map(::TransactionItem) ?: emptyList()

        fastItemAdapter.set(toListItems(transactions))
    }

    private fun bindTransactionListFilter(filter: TransactionListFilter) {
        filterSpinner.setSelection(filter.transferDirectionFilter.ordinal)
    }

    private fun onMenuItemClicked(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_switch_locale -> {
                LanguageSwitcher.toggleLanguageAndRestart(requireContext())
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }
}
