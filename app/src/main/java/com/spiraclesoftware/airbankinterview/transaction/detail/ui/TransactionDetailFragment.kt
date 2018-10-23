package com.spiraclesoftware.airbankinterview.transaction.detail.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.TextViewCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.spiraclesoftware.airbankinterview.R
import com.spiraclesoftware.airbankinterview.common.ui.RetryCallback
import com.spiraclesoftware.airbankinterview.databinding.TransactionDetailFragmentBinding
import com.spiraclesoftware.airbankinterview.transaction.shared.domain.TransactionDirection
import com.spiraclesoftware.core.extensions.viewModelProvider
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class TransactionDetailFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: TransactionDetailViewModel

    private lateinit var binding: TransactionDetailFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = TransactionDetailFragmentBinding.inflate(inflater)

        binding.retryCallback = object : RetryCallback {
            override fun retry() {
                viewModel.retry()
            }
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val params = TransactionDetailFragmentArgs.fromBundle(arguments)

        viewModel = viewModelProvider(viewModelFactory)
        viewModel.setTransactionId(params.transactionId)

        viewModel.transaction.observe(this, Observer { resource ->
            binding.transaction = resource?.data

            val transactionAmountTextAppearance = when (resource.data!!.direction) {
                TransactionDirection.INCOMING -> R.style.TextAppearance_Transaction_Amount_Incoming
                TransactionDirection.OUTGOING -> R.style.TextAppearance_Transaction_Amount_Outgoing
            }
            TextViewCompat.setTextAppearance(binding.transactionAmountView, transactionAmountTextAppearance)
        })

        viewModel.transactionDetail.observe(this, Observer { resource ->
            binding.transactionDetail = resource?.data
            binding.transactionDetailResource = resource
        })
    }
}