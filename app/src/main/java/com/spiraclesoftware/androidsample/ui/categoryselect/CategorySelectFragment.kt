package com.spiraclesoftware.androidsample.ui.categoryselect

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import co.zsmb.rainbowcake.base.OneShotEvent
import com.google.android.material.snackbar.Snackbar
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.GenericFastAdapter
import com.mikepenz.fastadapter.adapters.GenericItemAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.diff.FastAdapterDiffUtil
import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.StandardFragment
import com.spiraclesoftware.androidsample.databinding.CategorySelectFragmentBinding
import com.spiraclesoftware.androidsample.domain.model.TransactionId
import com.spiraclesoftware.androidsample.ui.categoryselect.CategorySelectViewModel.NotifyOfFailureEvent
import com.spiraclesoftware.androidsample.ui.categoryselect.CategorySelectViewModel.NotifyOfSuccessEvent
import com.spiraclesoftware.androidsample.ui.shared.DelightUI
import com.spiraclesoftware.core.extensions.showSnackbar
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf

class CategorySelectFragment :
    StandardFragment<CategorySelectFragmentBinding, CategorySelectViewState, CategorySelectViewModel>() {

    override fun provideViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        CategorySelectFragmentBinding.inflate(inflater, container, false)

    override fun provideViewModel(): CategorySelectViewModel {
        CategorySelectFragmentArgs.fromBundle(requireArguments()).let { args ->
            val id = TransactionId(args.transactionId)
            return getViewModel { parametersOf(id, args.initialCategory) }
        }
    }

    private lateinit var fastAdapter: GenericFastAdapter
    private lateinit var itemAdapter: GenericItemAdapter

    override fun render(viewState: CategorySelectViewState) = with(binding) {
        if (viewState is CategorySelect) {
            FastAdapterDiffUtil[itemAdapter] = viewState.listItems
        }
    }

    override fun onEvent(event: OneShotEvent) {
        when (event) {
            is NotifyOfSuccessEvent ->
                showSnackbar(R.string.category_select__progress_success, Snackbar.LENGTH_SHORT)
            is NotifyOfFailureEvent ->
                showSnackbar(R.string.unknown_error, Snackbar.LENGTH_SHORT)
        }
    }

    private fun onCategoryItemClicked(item: CategoryItem) {
        viewModel.selectCategory(item.category)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        setupFastItemAdapter()
        setupRecyclerView()
    }

    override fun onDestroyView() = with(binding) {
        recyclerView.adapter = null
        super.onDestroyView()
    }

    private fun setupToolbar() = with(binding) {
        toolbar.setupWithNavController(findNavController())
        DelightUI.setupToolbarTitleAppearingOnScroll(toolbar, scrollView)
    }

    private fun setupFastItemAdapter() {
        itemAdapter = ItemAdapter.items()
        fastAdapter = FastAdapter.with(itemAdapter).apply {
            setHasStableIds(true)
        }
        fastAdapter.onClickListener = { _, _, item, _ ->
            if (item is CategoryItem) onCategoryItemClicked(item)
            true
        }
    }

    private fun setupRecyclerView() = with(binding) {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = fastAdapter
        }
    }

}
