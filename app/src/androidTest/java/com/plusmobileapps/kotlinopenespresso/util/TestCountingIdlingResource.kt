package com.plusmobileapps.kotlinopenespresso.util

import javax.inject.Inject

class TestCountingIdlingResource @Inject constructor(): CountingIdlingResource {

    companion object {
        const val KEY = "CountingIdlingResource"
    }

    val idlingResource = androidx.test.espresso.idling.CountingIdlingResource(KEY)

    override val isIdleNow: Boolean get() = idlingResource.isIdleNow

    override fun increment() {
        idlingResource.increment()
    }

    override fun decrement() {
        idlingResource.decrement()
    }

}