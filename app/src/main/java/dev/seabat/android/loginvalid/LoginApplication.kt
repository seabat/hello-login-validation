package dev.seabat.android.loginvalid

import android.app.Application
import dev.seabat.android.loginvalid.data.LoginDataSource
import dev.seabat.android.loginvalid.data.LoginRepository

class LoginApplication : Application() {
    lateinit var loginRepository : LoginRepository

    override fun onCreate() {
        super.onCreate()
        loginRepository = LoginRepository(LoginDataSource())
    }

}