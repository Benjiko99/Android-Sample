package com.spiraclesoftware.androidsample.framework.extensions

import android.text.InputType
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout

val Toolbar.titleView: TextView?
    get() {
        return when {
            // If there's a navigation icon, then title will be the second view
            this.getChildAt(1) is TextView -> this.getChildAt(1)
            // If there's no navigation icon, then title will be the first view
            this.getChildAt(0) is TextView -> this.getChildAt(0)
            // If no title is set, then the title view is not present in the layout
            else -> null
        } as TextView?
    }

fun AdapterView<*>.onItemSelected(
    onNothingSelected: ((AdapterView<*>) -> Unit)? = null,
    onItemSelected: (AdapterView<*>, View?, Int, Long) -> Unit
) {
    this.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

        override fun onNothingSelected(parent: AdapterView<*>) {
            onNothingSelected?.invoke(parent)
        }

        override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
            onItemSelected.invoke(parent, view, position, id)
        }

    }
}

/**
 * Prevents the view from gaining focus when clicked, letting you do things
 * like open a date picker instead of editing the text.
 */
fun TextInputLayout.setupNonEditable(onClick: () -> Unit) {
    editText!!.apply {
        isFocusable = false
        isFocusableInTouchMode = false
        inputType = InputType.TYPE_NULL
        onClick { onClick() }
    }
    onClick { onClick() }
    setStartIconOnClickListener { onClick() }
}

fun Fragment.showToast(resId: Int, duration: Int) {
    Toast.makeText(requireContext(), resId, duration).show()
}

fun Fragment.showToast(text: CharSequence, duration: Int) {
    Toast.makeText(requireContext(), text, duration).show()
}

fun Fragment.showSnackbar(resId: Int, duration: Int) {
    makeSnackbar(resId, duration).show()
}

fun Fragment.showSnackbar(text: CharSequence, duration: Int) {
    makeSnackbar(text, duration).show()
}

fun Fragment.makeSnackbar(resId: Int, duration: Int): Snackbar {
    return Snackbar.make(requireView(), resId, duration)
}

fun Fragment.makeSnackbar(text: CharSequence, duration: Int): Snackbar {
    return Snackbar.make(requireView(), text, duration)
}