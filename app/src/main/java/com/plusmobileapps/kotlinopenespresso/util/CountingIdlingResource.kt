package com.plusmobileapps.kotlinopenespresso.util

import javax.inject.Inject

interface CountingIdlingResource {
    val isIdleNow: Boolean
    fun increment()
    fun decrement()
}

/**
 * No-op implementation of counting idling resource so production app does not actually do anything
 * with espresso
 */
class CountingIdlingResourceStub @Inject constructor() : CountingIdlingResource {
    override val isIdleNow: Boolean
        get() = TODO("Not yet implemented")

    override fun increment() {
    }

    override fun decrement() {
    }
}