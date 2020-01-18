package com.onecode.mymovie.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.onecode.mymovie.R
import com.onecode.mymovie.sign.SignInActivity
import kotlinx.android.synthetic.main.activity_onboarding_three.*

class OnboardingThreeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding_three)

        btn_home.setOnClickListener {

            finishAffinity()

            val intent = Intent(this@OnboardingThreeActivity,
                SignInActivity::class.java)
            startActivity(intent)
        }
    }
}
