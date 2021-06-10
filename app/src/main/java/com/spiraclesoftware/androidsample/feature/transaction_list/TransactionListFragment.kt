package com.spiraclesoftware.androidsample.feature.transaction_list

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
import com.mikepenz.fastadapter.adapters.GenericModelAdapter
import com.mikepenz.fastadapter.adapters.ModelAdapter
import com.mikepenz.fastadapter.diff.FastAdapterDiffUtil
import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.databinding.TransactionListFragmentBinding
import com.spiraclesoftware.androidsample.domain.entity.TransferDirectionFilter
import com.spiraclesoftware.androidsample.feature.FeatureFlags
import com.spiraclesoftware.androidsample.feature.transaction_list.TransactionListFragmentDirections.Companion.toProfile
import com.spiraclesoftware.androidsample.feature.transaction_list.TransactionListFragmentDirections.Companion.toTransactionDetail
import com.spiraclesoftware.androidsample.feature.transaction_list.TransactionListViewModel.*
import com.spiraclesoftware.androidsample.feature.transaction_list.TransactionListViewState.*
import com.spiraclesoftware.androidsample.feature.transaction_list.item.HeaderItem
import com.spiraclesoftware.androidsample.feature.transaction_list.item.TransactionItem
import com.spiraclesoftware.androidsample.feature.transaction_list.item.model.HeaderModel
import com.spiraclesoftware.androidsample.feature.transaction_list.item.model.TransactionModel
import com.spiraclesoftware.androidsample.framework.core.Model
import com.spiraclesoftware.androidsample.framework.core.StandardFragment
import com.spiraclesoftware.androidsample.framework.extensions.*
import com.spiraclesoftware.androidsample.framework.utils.DelightUI
import com.spiraclesoftware.androidsample.feature.transaction_list.TransactionListViewState as ViewState

class TransactionListFragment :
    StandardFragment<TransactionListFragmentBinding, ViewState, TransactionListViewModel>() {

    override fun provideViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        TransactionListFragmentBinding.inflate(inflater, container, false)

    override fun provideViewModel() = getViewModelFromFactory()

    private lateinit var fastAdapter: GenericFastAdapter
    private lateinit var itemAdapter: GenericModelAdapter<Model>

    private lateinit var collapseActionViewCallback: OnBackPressedCallback

    private fun onTransactionItemClicked(item: TransactionItem) {
        viewModel.openTransactionDetail(item.model.id)
    }

    private fun onFilterItemSelected(position: Int) {
        val filter = TransferDirectionFilter.values()[position]
        viewModel.filterByTransferDirection(filter)
    }

    private fun onSearchQueryChanged(query: String) {
        viewModel.filterByQuery(query)
    }

    private fun onSwipeToRefresh() {
        viewModel.refreshData()
    }

    private fun onRetryClicked() {
        viewModel.retryOnError()
    }

    private fun onLanguageChangeConfirmed() {
        viewModel.confirmLanguageChange()
    }

    private fun onMenuItemClicked(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_profile -> {
                viewModel.openProfile()
                return true
            }
            R.id.action_change_language -> {
                viewModel.changeLanguage()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

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
                FastAdapterDiffUtil[itemAdapter] = viewState.listModels
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
            val ordinal = viewState.filterModel.directionFilter.ordinal
            filterSpinner.setSelection(ordinal)
        }
    }

    override fun onEvent(event: OneShotEvent) {
        when (event) {
            is NavigateToProfileEvent ->
                findNavController().navigate(toProfile())
            is NavigateToTransactionDetailEvent ->
                findNavController().navigate(toTransactionDetail(event.id))
            ShowLanguageChangeConfirmationEvent ->
                showLanguageChangeConfirmation(onConfirmed = ::onLanguageChangeConfirmed)
        }
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
            collapseActionViewCallback.isEnabled = false
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

            binding.toolbar.post {
                // hide other menu items when search is expanded
                menu.forEach { it.isVisible = !isExpanded }

                // hide profile item according to feature flag
                menu.findItem(R.id.action_profile).apply {
                    val isEnabled = FeatureFlags.PROFILE_FEATURE.isEnabled
                    isVisible = isEnabled && !isExpanded
                }
            }
        }

        searchView.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextChange(query: String?): Boolean {
                onSearchQueryChanged(query.orEmpty())
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean = false
        })
    }

    private fun setupFastItemAdapter() {
        itemAdapter = ModelAdapter.models { model: Model ->
            when (model) {
                is HeaderModel -> HeaderItem(model)
                is TransactionModel -> TransactionItem(model)
                else -> throw IllegalStateException()
            }
        }
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
        val adapterItems = viewModel.getFilterStringIds().map { string(it) }

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
        errorLayout.retryButton.setOnClickListener { onRetryClicked() }
    }

}