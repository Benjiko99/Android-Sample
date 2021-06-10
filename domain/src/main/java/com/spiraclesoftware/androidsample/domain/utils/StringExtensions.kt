package com.spiraclesoftware.androidsample.domain.utils

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

/** Used for identifiers of RecyclerView items. */
fun CharSequence.to64BitHash(): Long {
    var result = -0x340d631b7bdddcdbL
    val len = this.length
    for (i in 0 until len) {
        result = result xor this[i].toLong()
        result *= 0x100000001b3L
    }
    return result
}