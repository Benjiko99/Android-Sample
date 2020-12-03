package com.spiraclesoftware.androidsample.ui.transactionlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import co.zsmb.rainbowcake.base.OneShotEvent
import co.zsmb.rainbowcake.koin.getViewModelFromFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.GenericFastAdapter
import com.mikepenz.fastadapter.adapters.GenericItemAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.diff.FastAdapterDiffUtil
import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.StandardFragment
import com.spiraclesoftware.androidsample.databinding.TransactionListFragmentBinding
import com.spiraclesoftware.androidsample.domain.model.TransferDirectionFilter
import com.spiraclesoftware.androidsample.ui.shared.DelightUI
import com.spiraclesoftware.androidsample.ui.transactionlist.TransactionListViewModel.NavigateEvent
import com.spiraclesoftware.androidsample.ui.transactionlist.TransactionListViewModel.ShowLanguageChangeConfirmationEvent
import com.spiraclesoftware.core.extensions.onItemSelected
import com.spiraclesoftware.core.extensions.string
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
class TransactionListFragment :
    StandardFragment<TransactionListFragmentBinding, TransactionListViewState, TransactionListViewModel>() {

    override fun provideViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        TransactionListFragmentBinding.inflate(inflater, container, false)

    override fun provideViewModel() = getViewModelFromFactory()

    private lateinit var fastAdapter: GenericFastAdapter
    private lateinit var itemAdapter: GenericItemAdapter

    override fun render(viewState: TransactionListViewState): Unit = with(binding) {
        swipeRefreshLayout.isRefreshing = viewState is Loading
        errorLayout.root.isVisible = viewState is Error
        filterSpinner.isEnabled = viewState is ListReady

        when (viewState) {
            is ListReady -> {
                recyclerView.isVisible = true

                FastAdapterDiffUtil[itemAdapter] = viewState.listItems
                filterSpinner.setSelection(viewState.listFilter.directionFilter.ordinal)
            }
            Error -> {
                recyclerView.isVisible = false

                errorLayout.errorMessageView.text = string(R.string.network_error)
                itemAdapter.set(emptyList())
            }
        }
    }

    override fun onEvent(event: OneShotEvent) {
        when (event) {
            is NavigateEvent ->
                findNavController().navigate(event.navDirections)
            ShowLanguageChangeConfirmationEvent ->
                showLanguageChangeConfirmation(onConfirmed = { viewModel.toggleLanguage() })
        }
    }

    private fun onTransactionItemClicked(item: TransactionItem) {
        viewModel.onListItemClicked(item.transaction.id)
    }

    private fun onFilterItemSelected(position: Int) {
        val filter = TransferDirectionFilter.values()[position]
        viewModel.setTransferDirectionFilter(filter)
    }

    private fun onSwipeToRefresh() {
        viewModel.refreshTransactions()
    }

    private fun onRetryOnError() {
        viewModel.refreshTransactions()
    }

    private fun onMenuItemClicked(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_change_language -> {
                viewModel.onLanguageChangeClicked()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        setupFastItemAdapter()
        setupRecyclerView()
        setupFilterSpinner()
        setupSwipeRefreshLayout()
        setupErrorLayout()
    }

    override fun onDestroyView() = with(binding) {
        recyclerView.adapter = null
        super.onDestroyView()
    }

    private fun showLanguageChangeConfirmation(onConfirmed: () -> Unit) {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage(R.string.change_language_dialog__message)
            .setNegativeButton(R.string.change_language_dialog__cancel) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(R.string.change_language_dialog__confirm) { _, _ ->
                onConfirmed()
            }.show()
    }

    private fun setupToolbar() = with(binding) {
        toolbar.setupWithNavController(findNavController())
        DelightUI.setupToolbarTitleAppearingOnScroll(toolbar, scrollView) {
            headerView.height
        }

        toolbar.inflateMenu(R.menu.transaction_list_menu)
        toolbar.setOnMenuItemClickListener(::onMenuItemClicked)
    }

    private fun setupFastItemAdapter() {
        itemAdapter = ItemAdapter.items()
        fastAdapter = FastAdapter.with(itemAdapter).apply {
            setHasStableIds(true)
        }
        fastAdapter.onClickListener = { _, _, item, _ ->
            if (item is TransactionItem) onTransactionItemClicked(item)
            true
        }
    }

    private fun setupRecyclerView() = with(binding) {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = fastAdapter
        }
    }

    private fun setupFilterSpinner() = with(binding) {
        val adapterItems = TransferDirectionFilter.values().map { string(it.stringRes) }

        filterSpinner.adapter = ArrayAdapter(
            requireContext(),
            R.layout.filter_spinner_item,
            adapterItems
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        filterSpinner.onItemSelected { _, _, position, _ -> onFilterItemSelected(position) }
    }

    private fun setupSwipeRefreshLayout() = with(binding) {
        swipeRefreshLayout.scrollUpChild = scrollView
        swipeRefreshLayout.setProgressViewOffset(true, 120, 360)
        swipeRefreshLayout.setOnRefreshListener { onSwipeToRefresh() }
    }

    private fun setupErrorLayout() = with(binding) {
        errorLayout.retryButton.setOnClickListener { onRetryOnError() }
    }

}