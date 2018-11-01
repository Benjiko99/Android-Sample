package com.spiraclesoftware.core.testing

// NOTE: File exists also in debug/ variant with additional OpenClass annotation, to make final classes open for testing.

/**
 * Annotate a class with [OpenForTesting] if you want it to be extendable in debug builds.
 *
 * This is the no-op version of this annotation. See this file in debug/ variant for the real implementation.
 */
@Target(AnnotationTarget.CLASS)
annotation class OpenForTesting