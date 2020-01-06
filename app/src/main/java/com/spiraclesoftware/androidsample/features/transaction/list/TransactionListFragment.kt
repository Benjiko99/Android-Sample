package com.spiraclesoftware.androidsample.features.transaction.list

import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.databinding.TransactionListFragmentBinding
import com.spiraclesoftware.androidsample.features.transaction.list.TransactionListFragmentDirections.toTransactionDetail
import com.spiraclesoftware.androidsample.shared.domain.Transaction
import com.spiraclesoftware.androidsample.shared.domain.TransactionDirectionFilter
import com.spiraclesoftware.androidsample.shared.domain.TransactionListFilter
import com.spiraclesoftware.androidsample.shared.ui.RetryCallback
import com.spiraclesoftware.core.data.EventObserver
import com.spiraclesoftware.core.data.Resource
import com.spiraclesoftware.core.extensions.string
import com.spiraclesoftware.core.utils.LanguageSwitcher
import kotlinx.android.synthetic.main.transaction__list__fragment.*
import org.koin.android.viewmodel.ext.android.viewModel

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

    // NOTE: We're setting up our views here instead of in onCreateView() because we're using Kotlin Synthetic Properties
    // to bind our views which requires the fragment's view to be attached.
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as? AppCompatActivity)?.setSupportActionBar(toolbar)
        toolbar.setupWithNavController(findNavController())

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
                addItemDecoration(
                    DividerItemDecoration(
                        requireContext(),
                        linearLayoutManager.orientation
                    )
                )
            }
        }
        setupRecyclerView()

        fun setupFilterSpinner() {

            fun createSpinnerAdapter(): ArrayAdapter<String> {
                return ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    TransactionDirectionFilter.values().map { string(it.stringRes) }
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
                    val transactionDirectionFilter = TransactionDirectionFilter.values()[position]
                    viewModel.setTransactionDirectionFilter(transactionDirectionFilter)
                }
            }
        }
        setupFilterSpinner()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setHasOptionsMenu(true)

        fun subscribeUi() {
            viewModel.transactions.observe(
                viewLifecycleOwner,
                Observer(::bindTransactionsResource)
            )

            viewModel.transactionListFilter.observe(
                viewLifecycleOwner,
                Observer(::bindTransactionListFilter)
            )

            viewModel.navigateToDetailAction.observe(this, EventObserver { transactionId ->
                findNavController().navigate(toTransactionDetail(transactionId.value))
            })

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
        filterSpinner.setSelection(filter.transactionDirectionFilter.ordinal)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_switch_locale -> {
                LanguageSwitcher.toggleLanguageAndRestart(requireContext())
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.transaction_list_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
}
