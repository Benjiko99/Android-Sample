package com.spiraclesoftware.androidsample.ui.categoryselect

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import co.zsmb.rainbowcake.base.OneShotEvent
import co.zsmb.rainbowcake.base.RainbowCakeFragment
import com.google.android.material.snackbar.Snackbar
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.GenericFastAdapter
import com.mikepenz.fastadapter.adapters.GenericItemAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.diff.FastAdapterDiffUtil
import com.spiraclesoftware.androidsample.R
import com.spiraclesoftware.androidsample.domain.model.TransactionId
import com.spiraclesoftware.androidsample.ui.categoryselect.CategorySelectViewModel.NotifyOfFailureEvent
import com.spiraclesoftware.androidsample.ui.categoryselect.CategorySelectViewModel.NotifyOfSuccessEvent
import com.spiraclesoftware.androidsample.ui.shared.DelightUI
import com.spiraclesoftware.core.extensions.makeSnackbar
import kotlinx.android.synthetic.main.category__select__fragment.*
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber

class CategorySelectFragment : RainbowCakeFragment<CategorySelectViewState, CategorySelectViewModel>() {

    override fun provideViewModel(): CategorySelectViewModel {
        CategorySelectFragmentArgs.fromBundle(requireArguments()).let { args ->
            val id = TransactionId(args.transactionId)
            return getViewModel { parametersOf(id, args.initialCategory) }
        }
    }

    override fun getViewResource() = R.layout.category__select__fragment

    private lateinit var fastAdapter: GenericFastAdapter
    private lateinit var itemAdapter: GenericItemAdapter

    private lateinit var onBackPressedCallback: OnBackPressedCallback
    private var isProcessing = false
    private val progressSnackbar: Snackbar by lazy {
        makeSnackbar("", Snackbar.LENGTH_INDEFINITE)
    }

    override fun render(viewState: CategorySelectViewState) {
        if (viewState is CategorySelect) {
            onBackPressedCallback.isEnabled = viewState.isProcessing

            val isProcessingChanged = isProcessing != viewState.isProcessing
            isProcessing = viewState.isProcessing

            if (
                (progressSnackbar.isShown && isProcessing && isProcessingChanged) ||
                (isProcessing && !progressSnackbar.isShown)
            ) {
                progressSnackbar.apply {
                    setText(R.string.category__select__progress_processing)
                    duration = Snackbar.LENGTH_INDEFINITE
                    show()
                }
            }

            FastAdapterDiffUtil[itemAdapter] = viewState.listItems
        }
    }

    override fun onEvent(event: OneShotEvent) {
        when (event) {
            is NotifyOfSuccessEvent ->
                progressSnackbar.apply {
                    setText(R.string.category__select__progress_success)
                    duration = Snackbar.LENGTH_LONG
                    show()
                }
            is NotifyOfFailureEvent ->
                progressSnackbar.apply {
                    setText(R.string.unknown_error)
                    duration = Snackbar.LENGTH_LONG
                    show()
                }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fun setupToolbar() {
            toolbar.setupWithNavController(findNavController())
            DelightUI.setupToolbarTitleAppearingOnScroll(toolbar, scrollView)

            toolbar.setNavigationOnClickListener {
                if (!isProcessing) requireActivity().onBackPressed()
            }
        }
        setupToolbar()

        fun setupFastItemAdapter() {
            itemAdapter = ItemAdapter.items()
            fastAdapter = FastAdapter.with(itemAdapter).apply {
                setHasStableIds(true)
            }
            fastAdapter.onClickListener = { _, _, item, _ ->
                when (item) {
                    is CategoryItem -> {
                        viewModel.selectCategory(item.category)
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
            }
        }
        setupRecyclerView()
    }

    override fun onDestroyView() {
        recyclerView.adapter = null
        super.onDestroyView()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        onBackPressedCallback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            Timber.d("Preventing leaving the screen while job is running.")
        }
    }

}
