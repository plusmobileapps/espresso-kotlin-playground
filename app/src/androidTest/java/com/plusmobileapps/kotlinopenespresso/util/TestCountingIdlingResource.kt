package com.plusmobileapps.kotlinopenespresso.util

import javax.inject.Inject

class TestCountingIdlingResource @Inject constructor(): CountingIdlingResource {

    companion object {
        const val KEY = "CountingIdlingResource"
    }

    val instance = androidx.test.espresso.idling.CountingIdlingResource(KEY)

    override val isIdleNow: Boolean get() = instance.isIdleNow

    override fun increment() {
        instance.increment()
    }

    override fun decrement() {
        instance.decrement()
    }

}