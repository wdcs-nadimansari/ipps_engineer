package com.webclues.IPPSEngineer.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.webclues.IPPSEngineer.R
import com.webclues.IPPSEngineer.utility.Content
import com.webclues.IPPSEngineer.utility.Static.Companion.SPLASH_SCREEN_TIME_OUT
import com.webclues.IPPSEngineer.utility.TinyDb

class SplashActivity : AppCompatActivity() {

    val handler: Handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        handler.postDelayed(Runnable {

            NavigateToInfo()

        }, SPLASH_SCREEN_TIME_OUT.toLong())


    }

    private fun NavigateToInfo() {


        if (TinyDb(this@SplashActivity).getBoolean(Content.IS_LOGIN)) {

            startActivity(Intent(this, MainActivity::class.java))
            finish()

        } else {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

    }

}
