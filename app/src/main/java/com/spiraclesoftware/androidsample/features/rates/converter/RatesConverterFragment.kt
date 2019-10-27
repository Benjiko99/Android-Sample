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
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
import com.spiraclesoftware.androidsample.application.GlideApp
import com.spiraclesoftware.androidsample.application.GlideRequests
import com.spiraclesoftware.androidsample.databinding.RatesConverterFragmentBinding
import com.spiraclesoftware.androidsample.shared.domain.ConversionRate
import com.spiraclesoftware.androidsample.shared.domain.ConversionRates
import com.spiraclesoftware.androidsample.shared.domain.CurrencyCode
import com.spiraclesoftware.core.data.Resource
import kotlinx.android.synthetic.main.rates__converter__fragment.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class RatesConverterFragment : Fragment() {

    private val viewModel by viewModel<RatesConverterViewModel> {
        parametersOf(CurrencyCode("EUR"))
    }

    private lateinit var binding: RatesConverterFragmentBinding
    private lateinit var fastItemAdapter: FastItemAdapter<ConversionRateItem>
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
            fastItemAdapter = FastItemAdapter()
        }
        setupFastItemAdapter()

        fun setupRecyclerView() {
            val linearLayoutManager = LinearLayoutManager(requireContext())

            recyclerView.apply {
                layoutManager = linearLayoutManager
                adapter = fastItemAdapter
            }
        }
        setupRecyclerView()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        fun subscribeUi() {
            viewModel.conversionRates.observe(
                viewLifecycleOwner,
                Observer(::bindConversionRatesResource)
            )
        }
        subscribeUi()
    }

    private fun bindConversionRatesResource(resource: Resource<ConversionRates>) {
        setConversionRates(resource.data?.rates)
    }

    private fun setConversionRates(rates: List<ConversionRate>?) {
        fun toListItems(data: List<ConversionRate>?) =
            data?.map { ConversionRateItem(it, glideRequests) } ?: emptyList()

        fastItemAdapter.set(toListItems(rates))
    }
}