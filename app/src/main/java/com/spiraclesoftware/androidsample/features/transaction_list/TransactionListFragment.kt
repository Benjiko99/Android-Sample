package com.spiraclesoftware.androidsample.features.transaction_list

import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.core.view.forEach
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
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
import com.spiraclesoftware.androidsample.databinding.TransactionListFragmentBinding
import com.spiraclesoftware.androidsample.extensions.*
import com.spiraclesoftware.androidsample.features.transaction_list.TransactionListViewModel.*
import com.spiraclesoftware.androidsample.framework.StandardFragment
import com.spiraclesoftware.androidsample.utils.DelightUI
import com.spiraclesoftware.androidsample.features.transaction_list.TransactionListViewState as ViewState

class TransactionListFragment :
    StandardFragment<TransactionListFragmentBinding, ViewState, TransactionListViewModel>() {

    override fun provideViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        TransactionListFragmentBinding.inflate(inflater, container, false)

    override fun provideViewModel() = getViewModelFromFactory()

    private lateinit var fastAdapter: GenericFastAdapter
    private lateinit var itemAdapter: GenericItemAdapter

    private lateinit var collapseActionViewCallback: OnBackPressedCallback

    override fun render(viewState: ViewState) {
        renderRecyclerView(viewState)
        renderEmptyState(viewState)
        renderErrorLayout(viewState)
        renderSwipeRefreshLayout(viewState)
        renderFilterSpinner(viewState)
    }

    private fun renderRecyclerView(viewState: ViewState): Unit = with(binding) {
        when (viewState) {
            is Content -> {
                recyclerView.isVisible = viewState.emptyState == null
                FastAdapterDiffUtil[itemAdapter] = viewState.listItems
            }
            is Error -> {
                recyclerView.isVisible = false
                itemAdapter.set(emptyList())
            }
        }
    }

    private fun renderEmptyState(viewState: ViewState): Unit = with(binding) {
        when (viewState) {
            is Content -> {
                emptyStateLayout.apply {
                    root.isVisible = viewState.emptyState != null
                    image = drawable(viewState.emptyState?.image)
                    caption = stringOrNull(viewState.emptyState?.caption)
                    message = stringOrNull(viewState.emptyState?.message)
                }
            }
            is Error -> {
                emptyStateLayout.root.isVisible = false
            }
        }
    }

    private fun renderErrorLayout(viewState: ViewState): Unit = with(binding) {
        errorLayout.root.isVisible = viewState is Error

        if (viewState is Error) {
            errorLayout.errorMessageView.text = viewState.message
        }
    }

    private fun renderSwipeRefreshLayout(viewState: ViewState): Unit = with(binding) {
        swipeRefreshLayout.isRefreshing = viewState is Loading
    }

    private fun renderFilterSpinner(viewState: ViewState): Unit = with(binding) {
        filterSpinner.isEnabled = viewState !is Error

        if (viewState is Content) {
            filterSpinner.setSelection(viewState.directionFilter.ordinal)
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireActivity().onBackPressedDispatcher.apply {
            val lifecycle = this@TransactionListFragment as LifecycleOwner

            collapseActionViewCallback = addCallback(lifecycle) { collapseActionView() }
        }
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

    private fun collapseActionView() = with(binding) {
        if (toolbar.hasExpandedActionView()) {
            toolbar.menu.forEach { if (it.isActionViewExpanded) it.collapseActionView() }
        }
    }

    private fun setupToolbar() = with(binding) {
        toolbar.setupWithNavController(findNavController())

        DelightUI.setupToolbarTitleAppearingOnScroll(toolbar, scrollView) {
            headerView.height
        }

        toolbar.inflateMenu(R.menu.transaction_list_menu)
        toolbar.setOnMenuItemClickListener(::onMenuItemClicked)

        setupSearchView(toolbar.menu)
    }

    private fun setupSearchView(menu: Menu) {
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        searchView.queryHint = string(R.string.action_search_hint)

        searchItem.onActionExpanded { isExpanded ->
            collapseActionViewCallback.isEnabled = isExpanded
        }

        searchView.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextChange(query: String?): Boolean {
                viewModel.setSearchQuery(query.orEmpty())
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean = false
        })
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