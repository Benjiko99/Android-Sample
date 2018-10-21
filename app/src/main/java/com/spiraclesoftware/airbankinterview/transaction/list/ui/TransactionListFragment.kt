package com.spiraclesoftware.airbankinterview.transaction.list.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
import com.spiraclesoftware.airbankinterview.databinding.TransactionListFragmentBinding
import com.spiraclesoftware.airbankinterview.transaction.shared.domain.TransactionDirection
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.transaction__list__fragment.*
import java.math.BigDecimal

class TransactionListFragment : DaggerFragment() {


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
            withOnClickListener { itemView, adapter, item, position ->
                // TODO: Open Transaction Detail
                true
            }
        }

        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = fastItemAdapter
        }

        fastItemAdapter.add(getDummyTransactionItems())
    }

    private fun getDummyTransactionItems(): List<TransactionItem> {
        val items = arrayListOf<TransactionItem>()

        items.add(
            TransactionItem()
                .withTransactionAmount(BigDecimal(1337))
                .withTransactionDirection(TransactionDirection.INCOMING)
        )

        items.add(
            TransactionItem()
                .withTransactionAmount(BigDecimal.TEN)
                .withTransactionDirection(TransactionDirection.OUTGOING)
        )

        return items
    }
}
