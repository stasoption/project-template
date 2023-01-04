package com.averin.android.developer.auth.domain

class LoginInteractor(private val loginRepository: LoginRepository) {

    var loginToken: String?
        get() = loginRepository.loginToken
        set(loginToken) {
            loginRepository.loginToken = loginToken
        }

    suspend fun login(username: String, password: String) = loginRepository.login(username, password)
}
