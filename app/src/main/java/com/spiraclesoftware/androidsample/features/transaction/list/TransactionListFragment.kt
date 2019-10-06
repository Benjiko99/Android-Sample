package com.spiraclesoftware.androidsample.features.transaction.list

import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.databinding.TransactionListFragmentBinding
import com.spiraclesoftware.androidsample.features.transaction.list.TransactionListFragment.DataWiring.DataBindings
import com.spiraclesoftware.androidsample.features.transaction.list.TransactionListFragment.DataWiring.ViewBindings
import com.spiraclesoftware.androidsample.features.transaction.list.ui.TransactionListFragmentDirections
import com.spiraclesoftware.androidsample.shared.domain.Transaction
import com.spiraclesoftware.androidsample.shared.domain.TransactionDirectionFilter
import com.spiraclesoftware.androidsample.shared.domain.TransactionListFilter
import com.spiraclesoftware.androidsample.shared.ui.RetryCallback
import com.spiraclesoftware.core.data.Resource
import com.spiraclesoftware.core.extensions.string
import com.spiraclesoftware.core.utils.LanguageSwitcher
import kotlinx.android.synthetic.main.transaction__list__fragment.*
import org.koin.android.viewmodel.ext.android.viewModel

class TransactionListFragment : Fragment() {

    private val viewModel by viewModel<TransactionListViewModel>()

    private val dataWiring = DataWiring()
    private lateinit var binding: TransactionListFragmentBinding
    private lateinit var fastItemAdapter: FastItemAdapter<TransactionItem>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = TransactionListFragmentBinding.inflate(inflater)
        binding.setLifecycleOwner(this)

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

        toolbar.setupWithNavController(findNavController())

        fun setupFastItemAdapter() {
            fastItemAdapter = FastItemAdapter()
            fastItemAdapter.apply {
                withSelectable(true)
                withOnClickListener { _, _, item, _ ->
                    findNavController().navigate(
                        TransactionListFragmentDirections.ActionTransactionListFragmentToTransactionDetailFragment(
                            item.transaction.id.value
                        )
                    )
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
                Observer(dataWiring::wireTransactionsResource)
            )

            viewModel.transactionListFilter.observe(
                viewLifecycleOwner,
                Observer(dataWiring::wireTransactionListFilter)
            )
        }
        subscribeUi()
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

    /**
     * Delegates data-wiring logic to [DataBindings] and [ViewBindings] so the call-site can be a function reference.
     */
    inner class DataWiring {

        private val dataBindings = DataBindings()
        private val viewBindings = ViewBindings()

        fun wireTransactionsResource(resource: Resource<List<Transaction>>) {
            dataBindings.bindTransactionsResource(resource)
            viewBindings.bindTransactions(resource.data)
        }

        fun wireTransactionListFilter(filter: TransactionListFilter) {
            viewBindings.bindTransactionListFilter(filter)
        }

        /**
         * Class for nicely organizing data binding code in a separate place.
         */
        inner class DataBindings {

            fun bindTransactionsResource(resource: Resource<List<Transaction>>) {
                binding.transactionListResource = resource
            }
        }

        /**
         * Class for nicely organizing view binding code in a separate place.
         */
        inner class ViewBindings {

            fun bindTransactions(transactions: List<Transaction>?) {
                fun toListItems(data: List<Transaction>?) =
                    data?.map(::TransactionItem) ?: emptyList()

                fastItemAdapter.set(toListItems(transactions))
            }

            fun bindTransactionListFilter(filter: TransactionListFilter) {
                filterSpinner.setSelection(filter.transactionDirectionFilter.ordinal)
            }
        }
    }
}
