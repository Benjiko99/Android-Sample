package com.spiraclesoftware.androidsample.features.rates.converter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.IItem
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.diff.DiffCallback
import com.mikepenz.fastadapter.diff.FastAdapterDiffUtil
import com.mikepenz.fastadapter.utils.ComparableItemListImpl
import com.spiraclesoftware.androidsample.application.GlideApp
import com.spiraclesoftware.androidsample.application.GlideRequests
import com.spiraclesoftware.androidsample.databinding.RatesConverterFragmentBinding
import com.spiraclesoftware.androidsample.shared.domain.ConversionRate
import com.spiraclesoftware.androidsample.shared.domain.ConversionRates
import com.spiraclesoftware.androidsample.shared.domain.CurrencyCode
import com.spiraclesoftware.core.data.Resource
import com.spiraclesoftware.core.data.Status
import kotlinx.android.synthetic.main.rates__converter__fragment.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class RatesConverterFragment : Fragment() {

    private val viewModel by viewModel<RatesConverterViewModel> {
        // TODO: Hardcoded base currency
        parametersOf(CurrencyCode("EUR"))
    }
//    private val ratesConverter by inject<RatesConverter> {
//        parametersOf(lifecycle)
//    }

    private lateinit var binding: RatesConverterFragmentBinding
    private lateinit var itemAdapter: ItemAdapter<ConversionRateItem>
    private lateinit var fastAdapter: FastAdapter<ConversionRateItem>
    private lateinit var itemListImpl: ComparableItemListImpl<ConversionRateItem>
    private lateinit var glideRequests: GlideRequests

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = RatesConverterFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = this

        glideRequests = GlideApp.with(this)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.setupWithNavController(findNavController())

        fun setupFastItemAdapter() {
            itemAdapter = ItemAdapter()
//            lhs.name.getText().toString().compareTo(rhs.name.getText().toString())
            fastAdapter = FastAdapter.with(itemAdapter)

            itemListImpl = ComparableItemListImpl(Comparator { lhs, rhs ->
                lhs.conversionRate.currency.currencyCode.compareTo(rhs.conversionRate.currency.currencyCode)
            })
            itemAdapter = ItemAdapter(itemListImpl)
            fastAdapter = FastAdapter.with(itemAdapter)

            fastAdapter.apply {
                setHasStableIds(true)
                addEventHook(moveFocusedItemToTopEventHook())
            }
        }
        setupFastItemAdapter()

        fun setupRecyclerView() {
            val linearLayoutManager = LinearLayoutManager(requireContext())

            recyclerView.apply {
                layoutManager = linearLayoutManager
                adapter = fastAdapter
            }
        }
        setupRecyclerView()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        fun subscribeUi() {
            /*viewModel.conversionRates.observe(
                viewLifecycleOwner,
                Observer(::bindConversionRatesResource)
            )*/

            // TODO: Set the items only once when entering the screen
            //  Afterwards, just update the data in each item, not the list itself
            viewModel.adjustedConversionRates.observe(
                viewLifecycleOwner,
                Observer(::bindAdjustedConversionRatesResource)
            )
        }
        subscribeUi()

        // Map ConversionRates from the viewModel to the ratesConverter
        // TODO: Umm why doesn't the RatesConverter pull this data from the repo himself?
        //ratesConverter.setConversionRates(viewModel.conversionRates)
    }

    private fun bindAdjustedConversionRatesResource(resource: Resource<ConversionRates>) {
        // TODO: Show loading state or error states
        when (resource.status) {
            Status.SUCCESS -> setConversionRatesToList(resource.data?.rates ?: emptyList())
            else -> setConversionRatesToList(emptyList())
        }
    }

    private fun setConversionRatesToList(rates: List<ConversionRate>) {
        fun toListItems(data: List<ConversionRate>) =
            data.map { rate ->
                ConversionRateItem(rate, glideRequests)
            }

        val listItems = toListItems(rates)
        // TODO: I'll just be doing the sorting in the viewModel
        //  So before he sends me the data to display, he applies all the sorting to it on his end

        // TODO: Orientation change makes us lose the order
        // retain each existing items position in the list,
        // by simply sorting it by its position in the currently active data set
        //.sortedBy { itemAdapter.getAdapterPosition(it.identifier) }

        FastAdapterDiffUtil.set(itemAdapter, listItems, DiffCallbackImpl())
    }

    // TODO: Should be part of the list item class?
    private class DiffCallbackImpl<Item : IItem<*>> : DiffCallback<Item> {
        override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem.identifier == newItem.identifier
        }

        override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem == newItem
        }

        override fun getChangePayload(
            oldItem: Item,
            oldItemPosition: Int,
            newItem: Item,
            newItemPosition: Int
        ): Any? {
            if (oldItem is ConversionRateItem && newItem is ConversionRateItem) {
                if (oldItem.conversionRate != newItem.conversionRate) {
                    return newItem.conversionRate
                }
            }
            return null
        }
    }

    /**
     * Hooks to the input field, listening for a focus change.
     * When the field gains focus, the item is animated with a delay to the top of the list.
     */
    private fun moveFocusedItemToTopEventHook() =
        object : ConversionRateItem.ViewHolder.InputFocusEventHook() {

            val delayBeforeMovingItem = 300L
            var pendingMoveAction: Runnable? = null

            override fun onFocusChange(
                v: View,
                position: Int,
                adapter: FastAdapter<ConversionRateItem>,
                item: ConversionRateItem,
                hasFocus: Boolean
            ) {
                // Item has gained focus and there isn't a pending move action
                if (hasFocus && pendingMoveAction == null) {
                    postMoveAction(position)
                }
                // Item has lost focus while waiting to animate the item
                else {
                    cancelPendingMoveAction()
                }
            }

            private fun postMoveAction(pos: Int) {
                pendingMoveAction = moveAction(recyclerView, pos)
                view?.postDelayed(pendingMoveAction, delayBeforeMovingItem)
            }

            private fun cancelPendingMoveAction() {
                view?.removeCallbacks(pendingMoveAction)
                pendingMoveAction = null
            }

            private fun moveAction(
                recycler: RecyclerView,
                position: Int
            ) = Runnable {
                //                viewModel.moveListItem(position, 0)
                itemAdapter.move(position, 0)

                recycler.post {
                    recycler.smoothScrollToPosition(0)
                }

                pendingMoveAction = null
            }
        }
}