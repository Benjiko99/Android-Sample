package com.spiraclesoftware.androidsample.features.rates.converter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.spiraclesoftware.androidsample.databinding.RatesConverterFragmentBinding
import kotlinx.android.synthetic.main.rates__converter__fragment.*
import org.koin.android.viewmodel.ext.android.viewModel

class RatesConverterFragment : Fragment() {

    private val viewModel by viewModel<RatesConverterViewModel>()

    private lateinit var binding: RatesConverterFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = RatesConverterFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.setupWithNavController(findNavController())

        fun setupRecyclerView() {
            val linearLayoutManager = LinearLayoutManager(requireContext())

            recyclerView.apply {
                layoutManager = linearLayoutManager
            }
        }
        setupRecyclerView()
    }
}