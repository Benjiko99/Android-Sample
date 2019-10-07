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

    private lateinit var binding: TransactionDetailFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = TransactionDetailFragmentBinding.inflate(inflater)
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
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val params = TransactionDetailFragmentArgs.fromBundle(arguments!!)

        viewModel.setTransactionId(TransactionId(params.transactionId))

        fun subscribeUi() {
            viewModel.transaction.observe(
                viewLifecycleOwner,
                TransactionResourceObserver()
            )

            viewModel.transactionDetail.observe(
                viewLifecycleOwner,
                Observer(::bindTransactionDetailResource)
            )
        }
        subscribeUi()
    }

    inner class TransactionResourceObserver : Observer<Resource<Transaction>> {

        override fun onChanged(resource: Resource<Transaction>?) {
            (resource?.data)?.let { transaction ->
                setTransactionAmountText(transaction.amountInAccountCurrency)
                val direction = transaction.direction
                setTransactionDirectionText(direction.getStringRes())
                setTransactionDirectionIcon(direction.getDrawableRes(), direction.getColorRes())
                setTransactionAmountTextAppearance(direction)
            }
        }

        private fun setTransactionAmountText(stringRes: Int) {
            binding.transactionAmountText = string(R.string.format__money, stringRes)
        }

        private fun setTransactionDirectionText(stringRes: Int) {
            binding.transactionDirectionText = string(stringRes)
        }

        private fun setTransactionDirectionIcon(drawableRes: Int, tintRes: Int) {
            val tintedDrawable =
                ResourceUtils.getTintedDrawable(drawable(drawableRes)!!, color(tintRes))
            binding.transactionDirectionDrawable = tintedDrawable
        }

        private fun setTransactionAmountTextAppearance(direction: TransactionDirection) {
            val transactionAmountTextAppearance = when (direction) {
                TransactionDirection.INCOMING -> R.style.TextAppearance_Transaction_Amount_Incoming
                TransactionDirection.OUTGOING -> R.style.TextAppearance_Transaction_Amount_Outgoing
            }
            TextViewCompat.setTextAppearance(
                binding.transactionAmountView,
                transactionAmountTextAppearance
            )
        }
    }

    private fun bindTransactionDetailResource(resource: Resource<TransactionDetail>) {
        binding.transactionDetail = resource.data
        binding.transactionDetailResource = resource
    }
}