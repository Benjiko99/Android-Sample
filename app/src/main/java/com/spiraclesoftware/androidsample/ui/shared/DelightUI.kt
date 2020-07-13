package com.spiraclesoftware.androidsample.ui.shared

import androidx.appcompat.widget.Toolbar
import androidx.core.widget.NestedScrollView
import com.spiraclesoftware.core.extensions.titleView
import kotlin.math.max

object DelightUI {

    /**
     * The toolbar's title will start out hidden, and will appear as the user scrolls further.
     */
    fun setupToolbarTitleAppearingOnScroll(toolbar: Toolbar, scrollView: NestedScrollView) {
        val toolbarTitleView = toolbar.titleView ?: return
        toolbarTitleView.translationY = -999f

        scrollView.setOnScrollChangeListener(
            NestedScrollView.OnScrollChangeListener { _, _, scrollY, _, _ ->
                val translationY = max(toolbar.height - scrollY, 0)
                toolbarTitleView.translationY = translationY.toFloat()
            }
        )
    }
}