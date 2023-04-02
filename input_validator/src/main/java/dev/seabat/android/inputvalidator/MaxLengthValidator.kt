package dev.seabat.android.inputvalidator

class MaxLengthValidator(
    private val password: String,
    private val maxPasswordLength: Int = 1,
    errorMessage: ErrorMessage
) : BaseValidator(errorMessage) {
    override fun validate(): Result<Unit> {
        val isValid = password.length <= maxPasswordLength
        return createResult(isValid)
    }
}