package dev.seabat.android.inputvalidator

class MinLengthValidator(
    private val password: String,
    private val minPasswordLength: Int = 1,
    errorMessage: ErrorMessage
) : BaseValidator(errorMessage) {
    override fun validate(): Result<Unit> {
        val isValid = password.length >= minPasswordLength
        return createResult(isValid)
    }
}