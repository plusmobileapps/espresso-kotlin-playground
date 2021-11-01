# Espresso Extensions

Espresso was originally written in Java which can be verbose and makes heavy use of static functions which do not always show up as suggestions when trying to use code completion. Since these tests are written in Kotlin, [extension functions](https://kotlinlang.org/docs/extensions.html) can be utilized to help improve the developer experience in writing expressive tests that provide helpful code completion. 

## ViewInteraction Extension

One of the most common ways to create a [`ViewInteraction`](https://developer.android.com/reference/androidx/test/espresso/ViewInteraction) with a `View` id is `onView(withId(R.id.some_id))`. To simplify this construction in page objects, an extension function can be added to the `BasePage` interface on an Int (resource id) which will return the `ViewInteraction`. 

```kotlin
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.matcher.ViewMatchers.withId

interface BasePage {

    fun Int.toViewInteraction(): ViewInteraction = 
        onView(withId(this))

}
```

Now the `LoginPage` can take advantage of the extension function for the implementation detail of the page objects. 

```kotlin
class LoginPage : BasePage {

    fun onEmail(): ViewInteraction = R.id.username.toViewInteraction()
    fun onPassword(): ViewInteraction = R.id.password.toViewInteraction()
    fun onSignInOrRegisterButton(): ViewInteraction = R.id.login.toViewInteraction()

}
```

## ViewAction Extensions

There are many of different types of actions that could be taken on a View, this series makes use of the following extension functions on a `ViewInteraction` in order to write more expressive tests. If you need a different type of action, the pattern should still apply simply creating an extension function on `ViewInteraction`. 

### Visibility

```kotlin
fun ViewInteraction.verifyVisible(): ViewInteraction = check(matches(isDisplayed()))
```

The `assertScreen()` function in each page can be implemented using the visibility extension function. 

```kotlin
class LoginUI : BaseUI {

    override fun assertScreen() {
        onEmail().verifyVisible()
        onPassword().verifyVisible()
        onSignInOrRegisterButton().verifyVisible()
    }

}
```

### Text Fields 

```kotlin
fun ViewInteraction.typeText(text: String): ViewInteraction = perform(ViewActions.typeText(text))

fun ViewInteraction.verifyTextFieldError(@StringRes resId: Int): ViewInteraction =
    check(matches(hasErrorText(getString(resId))))

// Helper function for getting strings using application target context
fun getString(@StringRes resId: Int): String =
    InstrumentationRegistry.getInstrumentation().targetContext.getString(resId)
```

One sample usage might be for a test that verifies an error is shown on a password field if not enough characters were entered. 

```kotlin
LoginPage().apply {
    onPassword().typeText("4344")
    onEmail().typeText("andrew@test.com")
    onPassword().verifyTextFieldError(R.string.invalid_password)
}
```

### Clicking

```kotlin
fun ViewInteraction.click(): ViewInteraction = perform(ViewActions.click())
```

Sample: 

```kotlin
LoginPage().onSignInOrRegisterButton().click()
```

### Verify Text

```kotlin
fun ViewInteraction.verifyText(text: String): ViewInteraction = check(matches(withText(text)))
```

Sample: 

```kotlin
LoggedInPage().onWelcomeGreeting().verifyText("Welcome Andrew!")
```

## androidx.test:core-ktx

```groovy
androidTestImplementation "androidx.test:core-ktx:<version>"
```

[Versions](https://mvnrepository.com/artifact/androidx.test/core-ktx)

Sample: 

```kotlin
val scenario = launchActivity<LoginActivity>()
```

## Simple Test

Now that all of the page objects are implemented, the last login test can be simplified further using the new extension functions. 

```kotlin
@Test 
fun successfulLogin() {
    val scenario = launchActivity<LoginActivity>()

    LoginPage().apply {
        onEmail().typeText("andrew@test.com")
        onPassword().typeText("password123")
        onSignInOrRegisterButton().click()
    }

    LoggedInPage().onWelcomeGreeting().verifyText("Welcome Andrew!")
}
```

The test is starting to look better, however each page has to be instantiated and there is no link between pages when writing tests. Thankfully Kotlin has features that will be covered in the next section to create a navigation DSL. 

## Resources

* Source code
    * [Github commit](https://github.com/plusmobileapps/espresso-kotlin-playground/commit/51aaf7fa633369000d8735a5255ccbed4e423f7b)
    * [`3-espresso-extensions` - GitHub Branch](https://github.com/plusmobileapps/espresso-kotlin-playground/tree/3-espresso-extensions)