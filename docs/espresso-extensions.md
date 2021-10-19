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