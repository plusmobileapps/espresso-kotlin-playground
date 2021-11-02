# Mock Ktor Testing

![](img/login-mock-network.png)

## Counting Idling Resource 

```kotlin
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
    private val idlingResource: CountingIdlingResource
) : ViewModel() {

    fun login(email: String, password: String) {
        idlingResource.increment()
        viewModelScope.launch {
            // delay is just for local development to ensure espresso waits for job to finish
            delay(2_000)

            // can be launched in a separate asynchronous job
            val result = loginRepository.login(email, password)

            when (result) {
                is Result.Error -> LoginResult(errorString = result.exception.message)
                is Result.Success -> LoginResult(success = LoggedInUserView(displayName = result.data.displayName))
            }.let { _loginResult.value = it }

            idlingResource.decrement()
        }
    }
}
```

## Resources 

* [Add ktor, kotlinx.serialization, and mock network test commit](https://github.com/plusmobileapps/espresso-kotlin-playground/commit/00c08f724dc5b6f00cd72c1536a65ca3c0d45d0a)
* [Add CI/CD workflow commit](https://github.com/plusmobileapps/espresso-kotlin-playground/pull/16/commits/2a72dec230bb0840a1734555004ec6814e11e581)
* [Add counting idling resource to fix flaky test commit](https://github.com/plusmobileapps/espresso-kotlin-playground/commit/99a9ff7cc887c7fb73b112adaebb9a4aa56e5ddc)
* [Coroutine idling resource workaround](https://github.com/Kotlin/kotlinx.coroutines/issues/242#issuecomment-561503344) and [commit](https://github.com/plusmobileapps/espresso-kotlin-playground/commit/b539a4bcc0f25455fbcc267d4b12d02635475c65)