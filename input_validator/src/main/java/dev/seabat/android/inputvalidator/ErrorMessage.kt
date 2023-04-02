package dev.seabat.android.inputvalidator

data class ErrorMessage(
    val resId: Int? = null, // アプリのリソースID
    val string: String? = null // エラーメッセージ
)