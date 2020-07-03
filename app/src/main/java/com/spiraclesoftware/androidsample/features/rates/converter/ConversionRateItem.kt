package com.spiraclesoftware.androidsample.features.rates.converter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.databinding.Observable
import androidx.databinding.ObservableField
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.mikepenz.fastadapter.binding.AbstractBindingItem
import com.mikepenz.fastadapter.binding.BindingViewHolder
import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.application.GlideRequests
import com.spiraclesoftware.androidsample.application.extensions.countryImageRes
import com.spiraclesoftware.androidsample.application.extensions.findByCurrency
import com.spiraclesoftware.androidsample.databinding.RatesConverterConversionRateItemBinding
import com.spiraclesoftware.androidsample.shared.domain.ConversionRate
import com.spiraclesoftware.androidsample.shared.ui.FocusEventHook
import timber.log.Timber
import java.math.RoundingMode
import java.text.DecimalFormat

class ConversionRateItem(
    val conversionRate: ConversionRate,
    val ratesConverter: RatesConverter,
    val glideRequests: GlideRequests
) : AbstractBindingItem<RatesConverterConversionRateItemBinding>() {

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

    lateinit var binding: RatesConverterConversionRateItemBinding

    override val type: Int
        get() = R.id.rates__converter__conversion_rate_item

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): RatesConverterConversionRateItemBinding {
        return RatesConverterConversionRateItemBinding.inflate(inflater, parent, false)
    }

    override fun bindView(binding: RatesConverterConversionRateItemBinding, payloads: List<Any>) {
        this.binding = binding

        if (payloads.isNotEmpty()) {
            // TODO: Do I need to manually update the item.conversionRate to the one from the payload?
            binding.apply {
                conversionRate = payloads[0] as ConversionRate//item.conversionRate

                if (!inputView.hasFocus()) {
                    this@ConversionRateItem.inputValue.set(decimalFormat.format((payloads[0] as ConversionRate).rate/*item.conversionRate.rate*/))
                    inputValue = this@ConversionRateItem.inputValue
                    notifyChange()
                }
            }
            return
        }

        binding.apply {
            conversionRate = this@ConversionRateItem.conversionRate
            inputValue = this@ConversionRateItem.inputValue
            inputView.filters = arrayOf(DecimalDigitsInputFilter())
            //item.inputValue.addOnPropertyChangedCallback(item.onInputValueChanged)

            if (!inputView.hasFocus()) {
                this@ConversionRateItem.inputValue.set(decimalFormat.format(this@ConversionRateItem.conversionRate.rate))
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

            glideRequests
                .load(this@ConversionRateItem.conversionRate.currency.countryImageRes(binding.root.context))
                .into(countryImageView)

            // TODO: How to get LifecycleOwner?
            this@ConversionRateItem.ratesConverter.adjustedConversionRates.observe(
                binding.root.context as LifecycleOwner,
                Observer {
                    val conversionRate =
                        it.data?.rates?.findByCurrency(this@ConversionRateItem.conversionRate.currency.currencyCode)
                            ?: return@Observer

                    if (!inputView.hasFocus()) {
                        this@ConversionRateItem.inputValue.set(decimalFormat.format(conversionRate.rate))
                    }
                }
            )
        }
    }

    private val decimalFormat = DecimalFormat("#.##").apply {
        roundingMode = RoundingMode.CEILING
    }

    val inputValue = ObservableField<String>("")

    override fun unbindView(binding: RatesConverterConversionRateItemBinding) {
        inputValue.removeOnPropertyChangedCallback(onInputValueChanged)
        glideRequests.clear(binding.countryImageView)
        with(binding) {
            countryImageView.setImageDrawable(null)
            currencyNameView.text = null
            currencyCodeView.text = null
            inputView.text = null
        }
    }

    abstract class InputFocusEventHook : FocusEventHook<ConversionRateItem>() {
        override fun onBind(viewHolder: RecyclerView.ViewHolder) =
            ((viewHolder as BindingViewHolder<*>)
                .binding as RatesConverterConversionRateItemBinding)
                .inputView
    }

    private val onInputValueChanged = object : Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
            Timber.d("InputValue: ${inputValue.get()}")

            // TODO: Infinite loops
            //ratesConverter.adjustValueOfBase()
        }
    }
}
