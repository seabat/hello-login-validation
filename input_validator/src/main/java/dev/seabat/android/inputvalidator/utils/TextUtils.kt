package dev.seabat.android.inputvalidator.utils

/**
 * android.text.TextUtils の代替クラス
 *
 * android.text.TextUtils は Android フレームワークの API であるため Unit テストできない。
 * 使用すると Unit テスト実行時に「Method isEmpty in android.text.TextUtils not mocked.」というエラーが発生する。
 * なのでこのクラスを代用する。
 */
class TextUtils {
    companion object {
        fun isEmpty(str: CharSequence?): Boolean {
            return str == null || str.isEmpty()
        }
    }
}