package com.spiraclesoftware.airbankinterview.transaction.list.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
import com.spiraclesoftware.airbankinterview.databinding.TransactionListFragmentBinding
import com.spiraclesoftware.airbankinterview.shared.ui.RetryCallback
import com.spiraclesoftware.airbankinterview.transaction.list.data.TransactionDirectionFilter
import com.spiraclesoftware.airbankinterview.transaction.list.domain.Transaction
import com.spiraclesoftware.airbankinterview.transaction.list.ui.TransactionListFragmentDirections.actionTransactionListFragmentToTransactionDetailFragment
import com.spiraclesoftware.core.extensions.viewModelProvider
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

        fun setupRecyclerView() {
            recyclerView.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = fastItemAdapter
                itemAnimator = null
            }
        }

        fun setupFilterSpinner() {

            fun createSpinnerAdapter(): ArrayAdapter<String> = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                TransactionDirectionFilter.values().map { getString(it.stringRes) }
            ).also {
                it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
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

        setupFastItemAdapter()
        setupRecyclerView()
        setupFilterSpinner()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = viewModelProvider(viewModelFactory)

        subscribeUi()
    }

    private fun subscribeUi() {
        viewModel.transactions.observe(this, Observer { resource ->
            binding.transactionListResource = resource
            fastItemAdapter.set(toListItems(resource.data))
        })
    }

    private fun toListItems(data: List<Transaction>?) = data?.map(::TransactionItem) ?: emptyList()
}
