package com.situdai.giftledger

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AlphaAnimation
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val logoView = findViewById<ImageView>(R.id.splashLogo)
        val titleView = findViewById<TextView>(R.id.splashTitle)
        val subtitleView = findViewById<TextView>(R.id.splashSubtitle)

        // 入场动画：淡入 + 放大
        val fadeIn = AlphaAnimation(0f, 1f).apply {
            duration = 800
            interpolator = DecelerateInterpolator()
        }

        logoView.startAnimation(fadeIn)
        titleView.startAnimation(fadeIn)
        subtitleView.startAnimation(fadeIn)

        // 延迟跳转
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }, 1200)
    }

    override fun onBackPressed() {
        // 禁止在 Splash 页返回
    }
}
