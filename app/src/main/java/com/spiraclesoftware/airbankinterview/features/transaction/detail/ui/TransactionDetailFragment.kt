package com.spiraclesoftware.airbankinterview.features.transaction.detail.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.TextViewCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.spiraclesoftware.airbankinterview.R
import com.spiraclesoftware.airbankinterview.databinding.TransactionDetailFragmentBinding
import com.spiraclesoftware.airbankinterview.features.transaction.detail.ui.TransactionDetailFragment.DataWiring.DataBindings
import com.spiraclesoftware.airbankinterview.features.transaction.detail.ui.TransactionDetailFragment.DataWiring.ViewBindings
import com.spiraclesoftware.airbankinterview.shared.domain.Transaction
import com.spiraclesoftware.airbankinterview.shared.domain.TransactionDetail
import com.spiraclesoftware.airbankinterview.shared.domain.TransactionDirection
import com.spiraclesoftware.airbankinterview.shared.domain.TransactionId
import com.spiraclesoftware.airbankinterview.shared.ui.RetryCallback
import com.spiraclesoftware.core.data.Resource
import com.spiraclesoftware.core.extensions.drawable
import com.spiraclesoftware.core.extensions.string
import com.spiraclesoftware.core.extensions.viewModelProvider
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class TransactionDetailFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: TransactionDetailViewModel

    private val dataWiring = DataWiring()
    private lateinit var binding: TransactionDetailFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = TransactionDetailFragmentBinding.inflate(inflater)
        binding.setLifecycleOwner(this)

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
        viewModel.setTransactionId(TransactionId(params.transactionId))

        fun subscribeUi() {
            viewModel.transaction.observe(
                viewLifecycleOwner,
                Observer(dataWiring::wireTransactionResource)
            )

            viewModel.transactionDetail.observe(
                viewLifecycleOwner,
                Observer(dataWiring::wireTransactionDetailResource)
            )
        }
        subscribeUi()
    }

    /**
     * Delegates data-wiring logic to [DataBindings] and [ViewBindings] so the call-site can be a function reference.
     */
    inner class DataWiring {

        private val dataBindings = DataBindings()
        private val viewBindings = ViewBindings()

        fun wireTransactionResource(resource: Resource<Transaction>) {
            dataBindings.bindTransactionResource(resource)
            viewBindings.bindTransaction(resource.data)
        }

        fun wireTransactionDetailResource(resource: Resource<TransactionDetail>) {
            dataBindings.bindTransactionDetailResource(resource)
        }

        /**
         * Class for nicely organizing data binding code in a separate place.
         */
        inner class DataBindings {

            fun bindTransactionResource(resource: Resource<Transaction>) {
                if (resource.data == null) return
                val transaction = resource.data as Transaction

                binding.transactionAmountText = string(
                    R.string.format__money,
                    transaction.amountInAccountCurrency
                )
                binding.transactionDirectionText = string(transaction.direction.getStringRes())
                binding.transactionDirectionDrawable = drawable(transaction.direction.getDrawableRes())
            }

            fun bindTransactionDetailResource(resource: Resource<TransactionDetail>) {
                binding.transactionDetail = resource.data
                binding.transactionDetailResource = resource
            }
        }

        /**
         * Class for nicely organizing view binding code in a separate place.
         */
        inner class ViewBindings {

            fun bindTransaction(transaction: Transaction?) {
                if (transaction == null) return

                val transactionAmountTextAppearance = when (transaction.direction) {
                    TransactionDirection.INCOMING -> R.style.TextAppearance_Transaction_Amount_Incoming
                    TransactionDirection.OUTGOING -> R.style.TextAppearance_Transaction_Amount_Outgoing
                }
                TextViewCompat.setTextAppearance(binding.transactionAmountView, transactionAmountTextAppearance)
            }
        }
    }
}