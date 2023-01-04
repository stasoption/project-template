package com.averin.android.developer.app.splash.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.averin.android.developer.R
import com.averin.android.developer.app.main.presentation.MainActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity(R.layout.ac_splash) {
    private val viewModel: SplashViewModel by viewModel()
    private var timer: CountDownTimer? = null
    private var timerFinished: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.navigateToNextActivity.observe(this, createNavigationObserver())
    }

    override fun onResume() {
        super.onResume()
        cancelTimer()
        timer = createSplashTimer().start()
    }

    override fun onPause() {
        cancelTimer()
        super.onPause()
    }

    private fun createSplashTimer(): CountDownTimer {
        return object : CountDownTimer(SPLASH_DURATION.toLong(), SPLASH_DURATION.toLong()) {
            override fun onTick(millisUntilFinished: Long) {
                // nothing
            }
            override fun onFinish() {
                timerFinished = true
                openNextActivity()
            }
        }
    }

    private fun openNextActivity() {
        if (timerFinished) {
            viewModel.timerFinish()
        }
    }

    private fun cancelTimer() {
        timerFinished = false
        timer?.cancel()
        timer = null
    }

    private fun createNavigationObserver(): Observer<Boolean> {
        return Observer { isAuthorized ->
            startActivity(MainActivity.makeIntent(this, isAuthorized))
            supportFinishAfterTransition()
        }
    }

    companion object {
        private const val SPLASH_DURATION = 1000
    }
}
