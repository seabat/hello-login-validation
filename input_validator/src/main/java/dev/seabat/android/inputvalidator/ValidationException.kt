package dev.seabat.android.inputvalidator

class ValidationException(val error: ErrorMessage) : Exception(error.string)