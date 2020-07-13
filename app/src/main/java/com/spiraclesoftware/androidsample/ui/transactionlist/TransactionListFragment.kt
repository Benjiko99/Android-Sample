package com.spiraclesoftware.androidsample.ui.transactionlist

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import co.zsmb.rainbowcake.base.OneShotEvent
import co.zsmb.rainbowcake.base.RainbowCakeFragment
import co.zsmb.rainbowcake.koin.getViewModelFromFactory
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.GenericFastAdapter
import com.mikepenz.fastadapter.adapters.GenericItemAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.domain.model.TransferDirectionFilter
import com.spiraclesoftware.androidsample.ui.shared.DelightUI
import com.spiraclesoftware.androidsample.ui.transactionlist.TransactionListFragmentDirections.Companion.toTransactionDetail
import com.spiraclesoftware.androidsample.ui.transactionlist.TransactionListViewModel.NavigateToDetailEvent
import com.spiraclesoftware.core.extensions.onItemSelected
import com.spiraclesoftware.core.extensions.string
import com.spiraclesoftware.core.utils.LanguageSwitcher
import kotlinx.android.synthetic.main.transaction__list__fragment.*

class TransactionListFragment : RainbowCakeFragment<TransactionListViewState, TransactionListViewModel>() {

    override fun provideViewModel() = getViewModelFromFactory()
    override fun getViewResource() = R.layout.transaction__list__fragment

    private lateinit var fastAdapter: GenericFastAdapter
    private lateinit var itemAdapter: GenericItemAdapter

    override fun render(viewState: TransactionListViewState) {
        loadingIndicator.isVisible = viewState is Loading
        errorGroup.isVisible = viewState is NetworkError

        when (viewState) {
            Loading -> {
            }
            is ListReady -> {
                recyclerView.isVisible = true
                itemAdapter.set(viewState.listItems)
            }
            NetworkError -> {
                recyclerView.isVisible = false
                itemAdapter.set(emptyList())
            }
        }
    }

    override fun onEvent(event: OneShotEvent) {
        when (event) {
            is NavigateToDetailEvent -> {
                findNavController().navigate(toTransactionDetail(event.id.value))
            }
        }
    }

    private fun subscribeUi() {
        viewModel.listFilter.observe(
            viewLifecycleOwner,
            Observer { filter ->
                filterSpinner.setSelection(filter.transferDirectionFilter.ordinal)
            }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fun setupToolbar() {
            toolbar.setupWithNavController(findNavController())
            DelightUI.setupToolbarTitleAppearingOnScroll(toolbar, scrollView)

            toolbar.inflateMenu(R.menu.transaction_list_menu)
            toolbar.setOnMenuItemClickListener(::onMenuItemClicked)
        }
        setupToolbar()

        fun setupFastItemAdapter() {
            itemAdapter = ItemAdapter.items()
            fastAdapter = FastAdapter.with(itemAdapter)
            fastAdapter.onClickListener = { _, _, item, _ ->
                when (item) {
                    is TransactionItem -> {
                        viewModel.onListItemClicked(item.transaction.id)
                        true
                    }
                    else -> false
                }
            }
        }
        setupFastItemAdapter()

        fun setupRecyclerView() {
            recyclerView.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = fastAdapter
                itemAnimator = null
            }
        }
        setupRecyclerView()

        fun setupFilterSpinner() {
            val adapterItems = TransferDirectionFilter.values().map { string(it.stringRes) }

            filterSpinner.adapter = ArrayAdapter(
                requireContext(),
                R.layout.filter_spinner_item,
                adapterItems
            ).apply {
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }

            filterSpinner.onItemSelected { _, _, position, _ ->
                val transferDirectionFilter = TransferDirectionFilter.values()[position]
                viewModel.setTransferDirectionFilter(transferDirectionFilter)
            }
        }
        setupFilterSpinner()

        retryButton.setOnClickListener { viewModel.reload() }
    }

    private fun onMenuItemClicked(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_switch_locale -> {
                LanguageSwitcher.toggleLanguageAndRestart(requireContext())
                return true
            }
            R.id.action_refresh -> {
                viewModel.reload()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        subscribeUi()
    }

}
