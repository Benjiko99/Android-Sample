package com.spiraclesoftware.androidsample.extension

/** Convenience for callbacks/listeners whose return value indicates an event was consumed. */
inline fun consume(f: () -> Unit): Boolean {
    f()
    return true
}

inline fun <T> applyToMany(vararg items: T, block: T.() -> Unit) {
    items.forEach { it.block() }
}