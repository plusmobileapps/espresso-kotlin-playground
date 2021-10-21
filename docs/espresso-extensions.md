# Espresso Extensions

```kotlin
inline fun <reified T : Activity> launchActivity(): ActivityScenario<T> =
    ActivityScenario.launch(T::class.java)

fun ViewInteraction.typeText(text: String): ViewInteraction = perform(ViewActions.typeText(text))

fun ViewInteraction.click(): ViewInteraction = perform(ViewActions.click())

fun ViewInteraction.verifyText(text: String): ViewInteraction = check(matches(withText(text)))

fun ViewInteraction.verifyVisible(): ViewInteraction = check(matches(isDisplayed()))

fun ViewInteraction.verifyError(@StringRes resId: Int): ViewInteraction =
    check(matches(hasErrorText(getString(resId))))

fun getString(@StringRes resId: Int): String =
    InstrumentationRegistry.getInstrumentation().targetContext.getString(resId)
```

```kotlin
interface BaseUI {

    fun Int.toViewInteraction(): ViewInteraction = onView(withId(this))

}
```

```kotlin
class LoginUI : BaseUI {

    override fun assertScreen() {
        onUsername().verifyVisible()
        onPassword().verifyVisible()
        onSignInOrRegisterButton().verifyVisible()
    }

    fun onUsername(): ViewInteraction = R.id.username.toViewInteraction()
    fun onPassword(): ViewInteraction = R.id.password.toViewInteraction()
    fun onSignInOrRegisterButton(): ViewInteraction = R.id.login.toViewInteraction()

    fun enterInfo(username: String, password: String) {
        onUsername().typeText(username)
        onPassword().typeText(password)
    }

}
```