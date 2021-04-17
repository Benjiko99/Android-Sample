package com.spiraclesoftware.androidsample.extension

/** Convenience for callbacks/listeners whose return value indicates an event was consumed. */
inline fun consume(f: () -> Unit): Boolean {
    f()
    return true
}

/**
 * Perform setup on multiple objects at once.
 * ```
 * applyToMany(viewA, viewB) {
 *     isEnabled = condition
 * }
 ```
 */
inline fun <T> applyToMany(vararg items: T, block: T.() -> Unit) {
    items.forEach { it.block() }
}