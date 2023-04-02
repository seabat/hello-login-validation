package dev.seabat.android.inputvalidator

abstract class BaseValidator(private val errorMessage: ErrorMessage) : IValidator {
    companion object {
        fun validate(vararg validators: IValidator): Result<Unit> {
            validators.forEach { validator ->
                validator.validate()
                    .onSuccess { /* Do nothing */}
                    .onFailure { throwable ->
                        val error = (throwable as ValidationException).error
                        return Result.failure(ValidationException(error))
                    }
            }
            return Result.success(Unit)
        }
    }
    protected fun createResult(isValid: Boolean): Result<Unit> {
        return if (isValid) {
            Result.success(Unit)
        } else {
            Result.failure(ValidationException(errorMessage))
        }
    }
}