package com.spiraclesoftware.airbankinterview.transaction.list.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
import com.spiraclesoftware.airbankinterview.databinding.TransactionListFragmentBinding
import com.spiraclesoftware.airbankinterview.transaction.list.domain.Transaction
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fastItemAdapter = FastItemAdapter()
        fastItemAdapter.apply {
            withSelectable(true)
            withOnClickListener { _, _, _, _ ->
                // TODO: Open Transaction Detail
                true
            }
        }

        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = fastItemAdapter
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = viewModelProvider(viewModelFactory)

        viewModel.transactions.observe(this, Observer { resource ->
            binding.transactionListResource = resource
            fastItemAdapter.set(resource.data?.toListItems() ?: arrayListOf())
        })
    }

    private fun List<Transaction>.toListItems(): List<TransactionItem> = map { it.toListItem() }

    private fun Transaction.toListItem() = TransactionItem()
        .withTransactionAmount(amountInAccountCurrency)
        .withTransactionDirection(direction)
}
