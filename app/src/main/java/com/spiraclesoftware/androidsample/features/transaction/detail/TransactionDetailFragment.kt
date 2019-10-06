package com.spiraclesoftware.androidsample.features.transaction.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.TextViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.databinding.TransactionDetailFragmentBinding
import com.spiraclesoftware.androidsample.features.transaction.detail.TransactionDetailFragment.DataWiring.DataBindings
import com.spiraclesoftware.androidsample.features.transaction.detail.TransactionDetailFragment.DataWiring.ViewBindings
import com.spiraclesoftware.androidsample.features.transaction.detail.ui.TransactionDetailFragmentArgs
import com.spiraclesoftware.androidsample.shared.domain.Transaction
import com.spiraclesoftware.androidsample.shared.domain.TransactionDetail
import com.spiraclesoftware.androidsample.shared.domain.TransactionDirection
import com.spiraclesoftware.androidsample.shared.domain.TransactionId
import com.spiraclesoftware.androidsample.shared.ui.RetryCallback
import com.spiraclesoftware.core.data.Resource
import com.spiraclesoftware.core.extensions.color
import com.spiraclesoftware.core.extensions.drawable
import com.spiraclesoftware.core.extensions.string
import com.spiraclesoftware.core.utils.ResourceUtils
import kotlinx.android.synthetic.main.transaction__detail__fragment.*
import org.koin.android.viewmodel.ext.android.viewModel

class TransactionDetailFragment : Fragment() {

    private val viewModel by viewModel<TransactionDetailViewModel>()

    private val dataWiring = DataWiring()
    private lateinit var binding: TransactionDetailFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = TransactionDetailFragmentBinding.inflate(inflater)
        binding.setLifecycleOwner(this)

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
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val params = TransactionDetailFragmentArgs.fromBundle(arguments!!)

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

                fun setTransactionAmountText() {
                    binding.transactionAmountText = string(
                        R.string.format__money,
                        transaction.amountInAccountCurrency
                    )
                }
                setTransactionAmountText()

                fun setTransactionDirectionText() {
                    binding.transactionDirectionText = string(transaction.direction.getStringRes())
                }
                setTransactionDirectionText()

                fun setTransactionDirectionIcon() {
                    val tintedDrawable = ResourceUtils.getTintedDrawable(
                        drawable(transaction.direction.getDrawableRes())!!,
                        color(transaction.direction.getColorRes())
                    )
                    binding.transactionDirectionDrawable = tintedDrawable
                }
                setTransactionDirectionIcon()
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
                TextViewCompat.setTextAppearance(
                    binding.transactionAmountView,
                    transactionAmountTextAppearance
                )
            }
        }
    }
}