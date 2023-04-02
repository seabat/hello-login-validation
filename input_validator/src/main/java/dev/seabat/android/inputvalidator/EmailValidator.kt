package dev.seabat.android.inputvalidator

import android.text.TextUtils

class EmailValidator(
    private val email: String,
    errorMessage: ErrorMessage
) : BaseValidator(errorMessage) {
    override fun validate(): Result<Unit> {
        val isValid =
            !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                .matches()
        return createResult(isValid)
    }
}