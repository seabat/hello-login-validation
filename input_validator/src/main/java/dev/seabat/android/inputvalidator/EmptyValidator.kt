package dev.seabat.android.inputvalidator

class EmptyValidator(
    private val input: String,
    errorMessage: ErrorMessage
) : BaseValidator(errorMessage) {
    override fun validate(): Result<Unit> {
        val isValid = input.isNotEmpty()
        return createResult(isValid)
    }
}