package com.spiraclesoftware.androidsample.extension

import java.text.Normalizer

private val REGEX_ACCENTS = "\\p{InCombiningDiacriticalMarks}+".toRegex()

/**
 * Checks whether a given string is contained within this string.
 *
 * Ignores case and accents/diacritics.
 */
fun String.lenientContains(other: String): Boolean {
    return this.stripAccents().contains(other.stripAccents(), ignoreCase = true)
}

fun CharSequence.stripAccents(): String {
    val normalized = Normalizer.normalize(this, Normalizer.Form.NFD)
    return REGEX_ACCENTS.replace(normalized, "")
}
