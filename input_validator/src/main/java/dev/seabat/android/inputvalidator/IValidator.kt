package dev.seabat.android.inputvalidator

interface IValidator {
    fun validate() : Result<Unit>
}