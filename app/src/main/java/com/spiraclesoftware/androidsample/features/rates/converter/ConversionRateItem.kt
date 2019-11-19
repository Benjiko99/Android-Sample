package com.spiraclesoftware.androidsample.features.rates.converter

import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.databinding.DataBindingUtil
import androidx.databinding.Observable
import androidx.databinding.ObservableField
import androidx.recyclerview.widget.RecyclerView
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.application.GlideRequests
import com.spiraclesoftware.androidsample.application.extensions.countryImageRes
import com.spiraclesoftware.androidsample.databinding.RatesConverterConversionRateItemBinding
import com.spiraclesoftware.androidsample.shared.domain.ConversionRate
import com.spiraclesoftware.androidsample.shared.domain.currencyCode
import com.spiraclesoftware.core.ui.FocusEventHook
import kotlinx.android.synthetic.main.rates__converter__conversion_rate_item.view.*
import timber.log.Timber
import java.math.RoundingMode
import java.text.DecimalFormat

class ConversionRateItem(
    val conversionRate: ConversionRate,
//    val ratesConverter: RatesConverter,
    val glideRequests: GlideRequests
) : AbstractItem<ConversionRateItem.ViewHolder>() {

    // TODO
    override var identifier: Long = conversionRate.getUniqueId().hashCode().toLong()

    // TODO: The contents are not the same, which is going to trigger a new ViewHolder to be created each time data changes
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        //if (!super.equals(other)) return false

        other as ConversionRateItem
        if (conversionRate != other.conversionRate) return false
        return identifier == other.identifier
//        return true
    }

    // TODO
    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + conversionRate.hashCode()
        return result
    }

    override val type: Int
        get() = R.id.rates__converter__conversion_rate_item

    override val layoutRes: Int
        get() = R.layout.rates__converter__conversion_rate_item

    override fun getViewHolder(v: View): ViewHolder = ViewHolder(v)

    val inputValue = ObservableField<String>("")

    class ViewHolder(view: View) : FastAdapter.ViewHolder<ConversionRateItem>(view) {

        val binding = (DataBindingUtil.getBinding(view)
            ?: RatesConverterConversionRateItemBinding.bind(view))!!

        private val decimalFormat = DecimalFormat("#.##").apply {
            roundingMode = RoundingMode.CEILING
        }

        override fun bindView(item: ConversionRateItem, payloads: MutableList<Any>) {
            if (payloads.isNotEmpty()) {
                // TODO: Do I need to manually update the item.conversionRate to the one from the payload?
                binding.apply {
                    conversionRate = payloads[0] as ConversionRate//item.conversionRate

                    if (!inputView.hasFocus()) {
                        item.inputValue.set(decimalFormat.format((payloads[0] as ConversionRate).rate/*item.conversionRate.rate*/))
                        inputValue = item.inputValue
                        notifyChange()
                    }
                }
                return
            }

            binding.apply {
                conversionRate = item.conversionRate
                inputValue = item.inputValue
                inputView.filters = arrayOf(DecimalDigitsInputFilter())
                //item.inputValue.addOnPropertyChangedCallback(item.onInputValueChanged)

                if (!inputView.hasFocus()) {
                    item.inputValue.set(decimalFormat.format(item.conversionRate.rate))
                }

                inputView.setOnEditorActionListener { _, actionId, _ ->
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        // TODO
                        /*item.ratesConverter.adjustValueOfBase(
                            item.conversionRate.currency,
                            item.inputValue.get()?.toFloatOrNull() ?: 0f
                        )*/
                    }
                    true
                }

                item.glideRequests
                    .load(item.conversionRate.currency.countryImageRes(binding.root.context))
                    .into(countryImageView)

                // TODO: How to get LifecycleOwner?
                /*item.ratesConverter.adjustedConversionRates.observe(
                    view.context as LifecycleOwner,
                    Observer {
                        val conversionRate = it.rates.findByCurrency(item.currency.currencyCode)
                            ?: return@Observer

                        if (!inputView.hasFocus()) {
                            item.inputValue.set(decimalFormat.format(conversionRate.rate))
                        }
                    }
                )*/
            }
        }

        override fun unbindView(item: ConversionRateItem) {
            with(binding.root) {
                item.inputValue.removeOnPropertyChangedCallback(item.onInputValueChanged)
                item.glideRequests.clear(countryImageView)
                countryImageView.setImageDrawable(null)
                currencyNameView.text = null
                currencyCodeView.text = null
                inputView.text = null
            }
        }

        abstract class InputFocusEventHook : FocusEventHook<ConversionRateItem>() {
            override fun onBind(viewHolder: RecyclerView.ViewHolder) =
                (viewHolder as? ViewHolder)?.binding?.inputView
        }
    }

    private val onInputValueChanged = object : Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
            Timber.d("InputValue: ${inputValue.get()}")

            // TODO: Infinite loops
            //ratesConverter.adjustValueOfBase()
        }
    }
}
