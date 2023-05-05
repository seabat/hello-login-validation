package dev.seabat.android.inputvalidator

import org.junit.Assert
import org.junit.Test

class BaseValidatorTest {

    @Test
    fun `validationを複数指定し、すべて合格`() {
        val username = "hogehoge@example.com"
        val result = BaseValidator.validate(
            EmptyValidator(username, ErrorMessage(string = "ユーザー名を入力してください")),
            EmailValidator(username, ErrorMessage(string = "E-mailではありません"))
        ).onSuccess {
            assert(true)
        }.onFailure {
            assert(false)
        }
        Assert.assertEquals(true, result.isSuccess)
    }

    @Test
    fun `validationを複数指定し、EmptyValidator に不合格`() {
        val username = ""
        val result = BaseValidator.validate(
            EmptyValidator(username, ErrorMessage(string = "ユーザー名を入力してください")),
            EmailValidator(username, ErrorMessage(string = "E-mailではありません"))
        ).onSuccess {
            assert(false)
        }.onFailure {
            Assert.assertEquals(
                "ユーザー名を入力してください",
                (it as ValidationException).error.string)
        }
        Assert.assertEquals(true, result.isFailure)
    }

    @Test
    fun `validationを複数指定し、EmailValidator に不合格`() {
        val username = "xxx"
        val result = BaseValidator.validate(
            EmptyValidator(username, ErrorMessage(string = "ユーザー名を入力してください")),
            EmailValidator(username, ErrorMessage(string = "E-mailではありません"))
        ).onSuccess {
            assert(false)
        }.onFailure {
            Assert.assertEquals(
                "E-mailではありません",
                (it as ValidationException).error.string)
        }
        Assert.assertEquals(true, result.isFailure)
    }

    @Test
    fun `validationを複数指定し、両方不合格の場合は先に指定した方のエラーとなる`() {
        val username = ""
        BaseValidator.validate(
            EmptyValidator(username, ErrorMessage(string = "ユーザー名を入力してください")),
            EmailValidator(username, ErrorMessage(string = "E-mailではありません")),
        ).onSuccess {
            assert(false)
        }.onFailure {
            Assert.assertEquals(
                "ユーザー名を入力してください",
                (it as ValidationException).error.string)
        }
        BaseValidator.validate(
            EmailValidator(username, ErrorMessage(string = "E-mailではありません")),
            EmptyValidator(username, ErrorMessage(string = "ユーザー名を入力してください")),
        ).onSuccess {
            assert(false)
        }.onFailure {
            Assert.assertEquals(
                "E-mailではありません",
                (it as ValidationException).error.string)
        }
    }
}