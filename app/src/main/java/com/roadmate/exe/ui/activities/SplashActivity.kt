package com.roadmate.exe.ui.activities

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.roadmate.exe.R
import com.roadmate.exe.constants.AppConstants.Companion.APP_STARTUP_DELAY
import com.roadmate.exe.extensions.doIfTrue
import com.roadmate.exe.extensions.elseDo
import com.roadmate.exe.extensions.startActivity
import com.roadmate.exe.preference.UserDetails

class SplashActivity : AppCompatActivity() {

    private fun moveToNextScreen(){
        Handler().postDelayed({
            UserDetails().isLoggedIn.doIfTrue {
                this.startActivity<ExecutiveHomeActivity>()
                this.finish()
            }elseDo {
                this.startActivity<LoginActivity>()
                this.finish()
            }
        }, APP_STARTUP_DELAY)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        supportActionBar!!.hide()

        moveToNextScreen()
    }
}