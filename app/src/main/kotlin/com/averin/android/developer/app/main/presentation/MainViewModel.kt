package com.averin.android.developer.app.main.presentation

import com.averin.android.developer.auth.domain.LoginInteractor
import com.averin.android.developer.baseui.presentation.BaseViewModel

class MainViewModel(private val loginInteractor: LoginInteractor) : BaseViewModel() {
    val loginToken: String?
        get() = loginInteractor.loginToken
}
