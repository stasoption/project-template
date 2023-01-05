package com.averin.android.developer.auth.domain

class LoginInteractor(private val loginRepository: LoginRepository) {

    var gitHubUserName: String?
        get() = loginRepository.gitHubUserName
        set(userName) {
            loginRepository.gitHubUserName = userName
        }

    suspend fun loadProfile(username: String) = loginRepository.loadProfile(username)
}
