package com.spiraclesoftware.androidsample.features.transaction.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.ColorUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.databinding.TransactionDetailFragmentBinding
import com.spiraclesoftware.androidsample.shared.domain.Transaction
import com.spiraclesoftware.androidsample.shared.domain.TransactionId
import com.spiraclesoftware.androidsample.shared.ui.DateTimeFormat
import com.spiraclesoftware.androidsample.shared.ui.DelightUI
import com.spiraclesoftware.core.data.Resource
import com.spiraclesoftware.core.extensions.color
import com.spiraclesoftware.core.extensions.tintedDrawable
import kotlinx.android.synthetic.main.transaction__detail__fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel

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

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.setupWithNavController(findNavController())

        DelightUI.setupToolbarTitleAppearingOnScroll(toolbar, scrollView)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val params = TransactionDetailFragmentArgs.fromBundle(requireArguments())

        viewModel.setTransactionId(TransactionId(params.transactionId))

        fun subscribeUi() {
            viewModel.transaction.observe(
                viewLifecycleOwner,
                TransactionResourceObserver()
            )
        }
        subscribeUi()
    }

    inner class TransactionResourceObserver : Observer<Resource<Transaction>> {

        override fun onChanged(resource: Resource<Transaction>?) {
            (resource?.data)?.let { transaction ->
                toolbar.title = transaction.name

                setAmountText(transaction.formattedMoney)
                setNameText(transaction.name)
                setDateText(transaction.processingDate.format(DateTimeFormat.PRETTY_DATE_TIME))
                setCategoryIcon(transaction.category.drawableRes, transaction.category.colorRes)
            }
        }
    }

    private fun setAmountText(string: String) {
        binding.amountText = string
    }

    private fun setNameText(string: String) {
        binding.nameText = string
    }

    private fun setDateText(string: String) {
        binding.dateText = string
    }

    private fun setCategoryIcon(drawableRes: Int, tintRes: Int) {
        val tint = color(tintRes)
        val fadedTint = ColorUtils.setAlphaComponent(tint, 255 / 100 * 15)

        binding.iconDrawable = tintedDrawable(drawableRes, tint)
        binding.iconBgDrawable = tintedDrawable(R.drawable.shp_circle, fadedTint)
    }
}