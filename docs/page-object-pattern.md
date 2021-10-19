# Page Object Pattern

The page object pattern is a very popular design pattern when writing automation tests as it helps abstract out the logic of interacting with a page in the UI from the test

Looking at the following UI, what might be some objects on the Login page object? 

??? answer
    * username 
    * password
    * sign in or register button

![](img/login-success.gif)

## BaseUI 

```kotlin
interface BaseUI {

    fun assertScreen()

    fun Int.toViewInteraction(): ViewInteraction = onView(withId(this))

}
```

## LoginUI

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

    fun submitAndGoToResultUI(block: ScopedUI<ResultUI>): ResultUI =
        navigateToWithClick(onSignInOrRegisterButton(), block)
}
```

## Resources

[Page object pattern by Martin Fowler](https://martinfowler.com/bliki/PageObject.html)
