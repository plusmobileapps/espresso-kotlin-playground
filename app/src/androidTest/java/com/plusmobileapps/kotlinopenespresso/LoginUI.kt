package com.plusmobileapps.kotlinopenespresso

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.matcher.ViewMatchers.withId

object LoginUI {
    
    fun onEmail(): ViewInteraction = onView(withId(R.id.username))
    fun onPassword(): ViewInteraction = onView(withId(R.id.password))
    fun onSignInOrRegisterButton(): ViewInteraction = onView(withId(R.id.login))
    fun onWelcomeGreeting(): ViewInteraction = onView(withId(R.id.loggen_in_greeting))

}