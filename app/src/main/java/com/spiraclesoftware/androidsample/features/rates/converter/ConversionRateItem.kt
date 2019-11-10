package com.spiraclesoftware.androidsample.features.rates.converter

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.application.GlideRequests
import com.spiraclesoftware.androidsample.application.extensions.countryImageRes
import com.spiraclesoftware.androidsample.databinding.RatesConverterConversionRateItemBinding
import com.spiraclesoftware.androidsample.shared.domain.ConversionRate
import com.spiraclesoftware.core.ui.FocusEventHook
import kotlinx.android.synthetic.main.rates__converter__conversion_rate_item.view.*

class ConversionRateItem(
    val conversionRate: ConversionRate,
    val glideRequests: GlideRequests
) : AbstractItem<ConversionRateItem.ViewHolder>() {


    override val type: Int
        get() = R.id.rates__converter__conversion_rate_item

    override val layoutRes: Int
        get() = R.layout.rates__converter__conversion_rate_item

    override fun getViewHolder(v: View): ViewHolder = ViewHolder(v)

    class ViewHolder(view: View) : FastAdapter.ViewHolder<ConversionRateItem>(view) {

        val binding = (DataBindingUtil.getBinding(view)
            ?: RatesConverterConversionRateItemBinding.bind(view))!!

        override fun bindView(item: ConversionRateItem, payloads: MutableList<Any>) {
            binding.apply {
                val currency = item.conversionRate.currency

                currencyName = currency.displayName
                currencyCode = currency.currencyCode

                inputView.setText(item.conversionRate.rate.toString())

                item.glideRequests
                    .load(currency.countryImageRes(binding.root.context))
                    .into(countryImageView)
            }
        }

        override fun unbindView(item: ConversionRateItem) {
            with(binding.root) {
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
}
