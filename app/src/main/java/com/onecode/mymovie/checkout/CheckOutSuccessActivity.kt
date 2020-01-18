package com.onecode.mymovie.checkout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.onecode.mymovie.R
import com.onecode.mymovie.home.HomeActivity
import com.onecode.mymovie.home.tiket.TiketActivity
import kotlinx.android.synthetic.main.activity_check_out_success.*

class CheckOutSuccessActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_out_success)

        btn_home.setOnClickListener {
            val intent = Intent(this@CheckOutSuccessActivity, HomeActivity::class.java)
            startActivity(intent)
        }

        btn_tiket.setOnClickListener {
            val intent = Intent(this@CheckOutSuccessActivity, TiketActivity::class.java)
            startActivity(intent)
        }
    }
}
