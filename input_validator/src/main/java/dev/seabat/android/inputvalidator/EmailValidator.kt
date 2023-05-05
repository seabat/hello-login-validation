package dev.seabat.android.inputvalidator

import dev.seabat.android.inputvalidator.utils.TextUtils
import java.util.regex.Pattern

/**
 * android.util.Patterns.EMAIL_ADDRESS は　Unit テストで使用できない(=実行時に nullとしてはんていされる)ため、
 * 同プロパティ値をここに直接定義する。
 */
val EMAIL_ADDRESS = Pattern.compile(
    "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
            "\\@" +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
            "(" +
            "\\." +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
            ")+"
)
class EmailValidator(
    private val email: String,
    errorMessage: ErrorMessage
) : BaseValidator(errorMessage) {
    override fun validate(): Result<Unit> {
        val isValid =
            !TextUtils.isEmpty(email) && EMAIL_ADDRESS.matcher(email)
                .matches()
        return createResult(isValid)
    }
}