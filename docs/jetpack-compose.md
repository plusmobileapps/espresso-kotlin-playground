## Update the Navigation Function

```kotlin
inline fun <reified T : BasePage> BasePage.navigateToPageWithClick(
    semanticsNodeInteraction: SemanticsNodeInteraction,
    block: PageScope<T>
): T {
    semanticsNodeInteraction.performClick()
    return T::class.java.newInstance().apply {
        this.composeTestRule = this@navigateToPageWithClick.composeTestRule
        assertScreen()
        block()
    }
}

inline fun <reified T : BasePage> ComposeTestRule.startOnPage(block: PageScope<T> = {}): T =
    T::class.java.newInstance().apply {
        this.composeTestRule = this@startOnPage
        assertScreen()
        block()
    }
```

## Convert BasePage to Abstract Class

```kotlin
abstract class BasePage {

    lateinit var composeTestRule: ComposeTestRule

    abstract fun assertScreen()

    fun Int.toViewInteraction(): ViewInteraction = onView(withId(this))

}
```

## Update Pages

```kotlin
class LoginPage : BasePage() {

    override fun assertScreen() {
        onSignInOrRegisterButton().assertIsDisplayed()
    }

    fun onSignInOrRegisterButton(): SemanticsNodeInteraction {
        val text =
            InstrumentationRegistry.getInstrumentation().targetContext.getString(R.string.action_sign_in)
        return composeTestRule.onNodeWithText(text = text)
    }

    fun goToLoggedInPage(block: PageScope<LoggedInPage>): LoggedInPage =
        navigateToPageWithClick(onSignInOrRegisterButton(), block)

}
```

Since the `navigateToPageWithClick` has the same name as the other version which takes a `ViewInteraction`, it technically doesn't need to be updated!

## Update Test

```kotlin
class MockkLoginTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<LoginActivity>()

    @Test
    fun successfulLogin() {
        // omitted code

        //use extension function on ComposeTestRule to start the navigation
        composeTestRule.startOnPage<LoginPage> {
            enterInfo("andrew@example.com", "password")
        }.goToLoggedInPage {
            onWelcomeGreeting().verifyText("Welcome Andrew!")
        }.goToSettings()
    }

}
```

## Replace View With Compose

```xml
<androidx.compose.ui.platform.ComposeView
    android:id="@+id/login_button"
    ... />
```

```kotlin
binding.loginButton.setContent {
    val state = loginViewModel.loginFormState.observeAsState()
    Button(onClick = {
        loading.visibility = View.VISIBLE
        loginViewModel.login(username.text.toString(), password.text.toString())
    }, enabled = state.value?.isDataValid ?: false) {
        Text(text = stringResource(id = R.string.action_sign_in))
    }
}
```

## Resources

* [Github commit of source code](https://github.com/plusmobileapps/espresso-kotlin-playground/commit/54241737b69617a3b8f67ae74b035499a0fa1930)
* [Jetpack compose reactive stream dependencies](https://developer.android.com/jetpack/compose/libraries#streams)
* [Compose Testing Documentation](https://developer.android.com/jetpack/compose/testing#espresso-interop)
* [Adding Jetpack Compose to existing project documentation](https://developer.android.com/jetpack/compose/interop/adding)