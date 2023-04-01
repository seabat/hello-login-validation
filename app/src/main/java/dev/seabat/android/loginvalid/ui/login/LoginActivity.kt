package dev.seabat.android.loginvalid.ui.login

import android.app.Activity
import androidx.lifecycle.Observer
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.viewmodel.MutableCreationExtras
import dev.seabat.android.loginvalid.LoginApplication
import dev.seabat.android.loginvalid.databinding.ActivityLoginBinding

import dev.seabat.android.loginvalid.R
import dev.seabat.android.loginvalid.ui.login.LoginViewModel.Companion.EXTRA_LOGIN_REPOSITORY_KEY

class LoginActivity : AppCompatActivity() {

    private val viewModel: LoginViewModel by viewModels(
        extrasProducer = {
            MutableCreationExtras(defaultViewModelCreationExtras).apply {
                set(EXTRA_LOGIN_REPOSITORY_KEY, (application as LoginApplication).loginRepository)
            }
        },
        factoryProducer = { LoginViewModel.Factory }
    )

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        observeLiveData()
        setListener()
    }

    private fun observeLiveData() {
        val username = binding.edittextUsername
        val password = binding.edittextPassword
        val login = binding.buttonLogin
        val loading = binding.progressbarLoading

        viewModel.loginFormState.observe(this@LoginActivity, Observer {
            it ?: return@Observer

            // disable login button unless both username / password is valid
            login.isEnabled = it.isDataValid

            if (it.usernameError != null) {
                username.error = getString(it.usernameError)
            }
            if (it.passwordError != null) {
                password.error = getString(it.passwordError)
            }
        })

        viewModel.loginResult.observe(this@LoginActivity, Observer {
            it ?: return@Observer

            loading.visibility = View.GONE
            if (it.error != null) {
                showLoginFailed(it.error)
            }
            if (it.success != null) {
                updateUiWithUser(it.success)
            }
            setResult(Activity.RESULT_OK)

            //Complete and destroy login activity once successful
            finish()
        })
    }

    private fun setListener() {
        val username = binding.edittextUsername
        val password = binding.edittextPassword
        val login = binding.buttonLogin
        val loading = binding.progressbarLoading

        username.afterTextChanged {
            viewModel.loginDataChanged(
                username.text.toString(),
                password.text.toString()
            )
        }

        password.also {
            it.afterTextChanged {
                viewModel.loginDataChanged(
                    username.text.toString(),
                    password.text.toString()
                )
            }
            it.setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        viewModel.login(
                            username.text.toString(),
                            password.text.toString()
                        )
                }
                false
            }
        }

        login.setOnClickListener {
            loading.visibility = View.VISIBLE
            viewModel.login(username.text.toString(), password.text.toString())
        }
    }

    private fun updateUiWithUser(model: LoggedInUserView) {
        val welcome = getString(R.string.welcome)
        val displayName = model.displayName
        // TODO : initiate successful logged in experience
        Toast.makeText(
            applicationContext,
            "$welcome $displayName",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }
}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}