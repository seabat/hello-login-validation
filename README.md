# hello-login-validation
ログイン画面におけるテキスト入力データの Validation サンプル。  

  
[How to Easily Validate User Inputs on Android?](https://medium.com/huawei-developers/how-to-easily-validate-user-inputs-on-android-80c8e5744de7) を参考にユーザー入力データの validation 処理を実装。

### 構成

* app モジュール  
  Android Studio の Login Activity テンプレート がベース。  
* input_validator モジュール  
  ユーザー入力データの validation 処理。

### Validation

* 空文字: EmptyValidation
* e-mail: EmailValidation
* min 文字数: MinLengthValidation
* max 文字数: MaxLengthValidation 

### サンプルコード

``` kotlin
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.SavedStateHandle

import dev.seabat.android.inputvalidator.BaseValidator
import dev.seabat.android.inputvalidator.EmailValidator
import dev.seabat.android.inputvalidator.EmptyValidator
import dev.seabat.android.inputvalidator.ErrorMessage
import dev.seabat.android.inputvalidator.MinLengthValidator
import dev.seabat.android.inputvalidator.ValidationException

class LoginViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _usernameError = MutableLiveData<Int?>()
    val usernameError: LiveData<Int?>
        get() = _usernameError

    private val _passwordError = MutableLiveData<Int?>()
    val passwordError: LiveData<Int?>
        get() = _passwordError

    fun loginDataChanged(username: String, password: String) {
        val usernameResult = BaseValidator.validate(
            EmptyValidator(username, ErrorMessage(resId = R.string.empty_username)),
            EmailValidator(username, ErrorMessage(resId = R.string.not_email_username))
        ).onFailure {
            _usernameError.value = (it as ValidationException).error.resId
        }

        val passwordResult = BaseValidator.validate(
            EmptyValidator(password, ErrorMessage(resId = R.string.empty_password)),
            MinLengthValidator(password, 5, ErrorMessage(resId = R.string.less_length_password))
        ).onFailure {
            _passwordError.value = (it as ValidationException).error.resId
        }
    }
}

```






