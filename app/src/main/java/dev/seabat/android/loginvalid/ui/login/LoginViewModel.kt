package dev.seabat.android.loginvalid.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import dev.seabat.android.inputvalidator.BaseValidator
import dev.seabat.android.inputvalidator.EmailValidator
import dev.seabat.android.inputvalidator.EmptyValidator
import dev.seabat.android.inputvalidator.ErrorMessage
import dev.seabat.android.inputvalidator.MinLengthValidator
import dev.seabat.android.inputvalidator.ValidationException
import dev.seabat.android.loginvalid.data.LoginRepository
import dev.seabat.android.loginvalid.data.Result

import dev.seabat.android.loginvalid.R

class LoginViewModel(
    private val loginRepository: LoginRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _usernameError = MutableLiveData<Int?>()
    val usernameError: LiveData<Int?>
        get() = _usernameError

    private val _passwordError = MutableLiveData<Int?>()
    val passwordError: LiveData<Int?>
        get() = _passwordError

    private val _loginEnable = MutableLiveData<Boolean>(false)
    val loginEnable: LiveData<Boolean>
        get() = _loginEnable

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult>
        get() = _loginResult


    companion object {
        val EXTRA_LOGIN_REPOSITORY_KEY = object : CreationExtras.Key<LoginRepository> {}

        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                // Get the Application object from extras
                val application = checkNotNull(extras[APPLICATION_KEY])
                // Create a SavedStateHandle for this ViewModel from extras
                val savedStateHandle = extras.createSavedStateHandle()
                // viewModels(extrasProducer,) で受け取った extras からオブジェクトを取り出す
                val loginRepository = extras[EXTRA_LOGIN_REPOSITORY_KEY]!!

                return LoginViewModel(loginRepository, savedStateHandle) as T
            }
        }
    }

    fun login(username: String, password: String) {
        // can be launched in a separate asynchronous job
        val result = loginRepository.login(username, password)

        if (result is Result.Success) {
            _loginResult.value =
                LoginResult(success = LoggedInUserView(displayName = result.data.displayName))
        } else {
            _loginResult.value = LoginResult(error = R.string.login_failed)
        }
    }

    fun loginDataChanged(username: String, password: String) {
        val usernameResult = BaseValidator.validate(
            EmptyValidator(username, ErrorMessage(resId = R.string.invalid_username)),
            EmailValidator(username, ErrorMessage(resId = R.string.invalid_username))
        )
        val passwordResult = BaseValidator.validate(
            EmptyValidator(password, ErrorMessage(resId = R.string.invalid_password)),
            MinLengthValidator(password, 5, ErrorMessage(resId = R.string.invalid_password))
        )

        if (usernameResult.isSuccess && passwordResult.isSuccess) {
            _loginEnable.value = true
            return
        }
        if (usernameResult.isFailure) {
            _usernameError.value = (usernameResult.exceptionOrNull() as ValidationException).error.resId
        } else {
            _usernameError.value = null
        }
        if (passwordResult.isFailure) {
            _passwordError.value = (passwordResult.exceptionOrNull() as ValidationException).error.resId
        } else {
            _passwordError.value = null
        }
    }
}