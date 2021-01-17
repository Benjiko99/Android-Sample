package com.spiraclesoftware.androidsample.utils

import androidx.appcompat.widget.Toolbar
import androidx.core.widget.NestedScrollView
import com.spiraclesoftware.androidsample.extensions.titleView
import kotlin.math.max

object DelightUI {

    /**
     * The toolbar's title will start out hidden, and will appear as the user scrolls further.
     */
    fun setupToolbarTitleAppearingOnScroll(
        toolbar: Toolbar,
        scrollView: NestedScrollView,
        appearAtHeight: () -> Int = { toolbar.height }
    ) {
        val toolbarTitleView = toolbar.titleView ?: return
        toolbarTitleView.translationY = -999f

        scrollView.setOnScrollChangeListener(
            NestedScrollView.OnScrollChangeListener { _, _, scrollY, _, _ ->
                val translationY = max(appearAtHeight() - scrollY, 0)
                toolbarTitleView.translationY = translationY.toFloat()
            }
        )
    }

}