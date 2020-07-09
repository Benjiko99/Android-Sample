package com.spiraclesoftware.androidsample.features.transaction.detail

import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.graphics.ColorUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.GenericFastAdapter
import com.mikepenz.fastadapter.adapters.GenericItemAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.databinding.TransactionDetailFragmentBinding
import com.spiraclesoftware.androidsample.features.transaction.detail.cards.Card
import com.spiraclesoftware.androidsample.shared.domain.Transaction
import com.spiraclesoftware.androidsample.shared.domain.TransactionId
import com.spiraclesoftware.androidsample.shared.domain.TransactionStatusCode
import com.spiraclesoftware.androidsample.shared.ui.DateTimeFormat
import com.spiraclesoftware.androidsample.shared.ui.DelightUI
import com.spiraclesoftware.core.data.NullableEventObserver
import com.spiraclesoftware.core.data.Resource
import com.spiraclesoftware.core.extensions.color
import com.spiraclesoftware.core.extensions.dpToPx
import com.spiraclesoftware.core.extensions.tintedDrawable
import io.cabriole.decorator.LinearMarginDecoration
import kotlinx.android.synthetic.main.transaction__detail__fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class TransactionDetailFragment : Fragment() {

    private val viewModel by viewModel<TransactionDetailViewModel>()

    private lateinit var binding: TransactionDetailFragmentBinding
    private lateinit var fastAdapter: GenericFastAdapter
    private lateinit var itemAdapter: GenericItemAdapter

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

        fun setupFastItemAdapter() {
            itemAdapter = ItemAdapter.items()
            fastAdapter = FastAdapter.with(itemAdapter)
            fastAdapter.attachDefaultListeners = false
        }
        setupFastItemAdapter()

        fun setupRecyclerView() {
            val linearLayoutManager = LinearLayoutManager(requireContext())

            recyclerView.apply {
                layoutManager = linearLayoutManager
                adapter = fastAdapter
                itemAnimator = null
                addItemDecoration(
                    LinearMarginDecoration.createVertical(
                        verticalMargin = dpToPx(16)
                    )
                )
            }
        }
        setupRecyclerView()
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

            viewModel.cards.observe(
                viewLifecycleOwner,
                CardsObserver()
            )

            viewModel.notifyFeatureNotImplementedAction.observe(
                viewLifecycleOwner,
                NullableEventObserver {
                    Toast.makeText(requireContext(), R.string.not_implemented, Toast.LENGTH_SHORT).show()
                }
            )
        }
        subscribeUi()
    }

    inner class CardsObserver : Observer<List<Card>> {

        override fun onChanged(cards: List<Card>?) {
            itemAdapter.set(cards?.toListItems() ?: emptyList())
        }

        private fun List<Card>.toListItems() = map { card ->
            val itemData = card.toItemData(requireContext(), viewModel.transaction.value!!.data!!)

            CardItem(itemData).apply {
                withActionClickHandler(viewModel::onCardActionClicked)
            }
        }
    }

    inner class TransactionResourceObserver : Observer<Resource<Transaction>> {

        lateinit var transaction: Transaction

        override fun onChanged(resource: Resource<Transaction>?) {
            (resource?.data)?.let { transaction ->
                this.transaction = transaction

                toolbar.title = transaction.name

                bindAmountText()
                bindNameText()
                bindDateText()
                bindCategoryIcon()
            }
        }

        private fun bindAmountText() {
            binding.amountText = transaction.formattedMoney
            if (!transaction.contributesToBalance()) {
                binding.amountView.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            }
        }

        private fun bindNameText() {
            binding.nameText = transaction.name
        }

        private fun bindDateText() {
            binding.dateText = transaction.processingDate.format(DateTimeFormat.PRETTY_DATE_TIME)
        }

        private fun bindCategoryIcon() {
            val tint: Int
            val category = transaction.category

            if (transaction.statusCode == TransactionStatusCode.SUCCESSFUL) {
                tint = color(category.colorRes)
                binding.iconDrawable = tintedDrawable(category.drawableRes, tint)
            } else {
                tint = color(R.color.transaction_status__declined)
                binding.iconDrawable = tintedDrawable(R.drawable.ic_status_declined, tint)
            }

            val fadedTint = ColorUtils.setAlphaComponent(tint, 255 / 100 * 15)
            binding.iconBgDrawable = tintedDrawable(R.drawable.shp_circle, fadedTint)
        }
    }
}