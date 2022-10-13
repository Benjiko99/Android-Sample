package com.spiraclesoftware.androidsample.view

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.appbar.MaterialToolbar
import com.spiraclesoftware.androidsample.R

class Toolbar : MaterialToolbar {

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, R.attr.toolbarStyle)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

}