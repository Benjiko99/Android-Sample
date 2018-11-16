package com.spiraclesoftware.airbankinterview.features.transaction.list.ui

import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
import com.spiraclesoftware.airbankinterview.R
import com.spiraclesoftware.airbankinterview.databinding.TransactionListFragmentBinding
import com.spiraclesoftware.airbankinterview.features.transaction.list.data.TransactionDirectionFilter
import com.spiraclesoftware.airbankinterview.features.transaction.list.ui.TransactionListFragmentDirections.actionTransactionListFragmentToTransactionDetailFragment
import com.spiraclesoftware.airbankinterview.shared.domain.Transaction
import com.spiraclesoftware.airbankinterview.shared.ui.RetryCallback
import com.spiraclesoftware.core.extensions.viewModelProvider
import com.spiraclesoftware.core.utils.LanguageSwitcher
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.transaction__list__fragment.*
import javax.inject.Inject

class TransactionListFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: TransactionListViewModel

    private lateinit var binding: TransactionListFragmentBinding
    private lateinit var fastItemAdapter: FastItemAdapter<TransactionItem>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = TransactionListFragmentBinding.inflate(inflater)

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

        fun setupFastItemAdapter() {
            fastItemAdapter = FastItemAdapter()
            fastItemAdapter.apply {
                withSelectable(true)
                withOnClickListener { _, _, item, _ ->
                    findNavController().navigate(
                        actionTransactionListFragmentToTransactionDetailFragment(item.transaction.id)
                    )
                    true
                }
            }
        }
        setupFastItemAdapter()

        fun setupRecyclerView() {
            recyclerView.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = fastItemAdapter
                itemAnimator = null
            }
        }
        setupRecyclerView()

        fun setupFilterSpinner() {

            fun createSpinnerAdapter(): ArrayAdapter<String> {
                return ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    TransactionDirectionFilter.values().map { getString(it.stringRes) }
                ).also {
                    it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                }
            }

            filterSpinner.adapter = createSpinnerAdapter()

            filterSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
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

        viewModel = viewModelProvider(viewModelFactory)

        fun subscribeUi() {
            viewModel.transactions.observe(viewLifecycleOwner, Observer { resource ->
                binding.transactionListResource = resource
                fastItemAdapter.set(toListItems(resource.data))
            })

            viewModel.transactionListFilter.observe(viewLifecycleOwner, Observer { filter ->
                filterSpinner.setSelection(filter.transactionDirectionFilter.ordinal)
            })
        }
        subscribeUi()
    }

    private fun toListItems(data: List<Transaction>?) = data?.map(::TransactionItem) ?: emptyList()

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
