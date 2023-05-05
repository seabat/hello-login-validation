package dev.seabat.android.inputvalidator

import androidx.annotation.StringRes

/**
 * Validation で不適合と判断された場合のメッセージ
 *
 * メッセージは resId と string のどちらかに指定すること
 *
 * @param resId アプリの文字列リソースのID
 * @param string エラーメッセージ
 */
data class ErrorMessage(
    @StringRes val resId: Int? = null,
    val string: String? = null
)