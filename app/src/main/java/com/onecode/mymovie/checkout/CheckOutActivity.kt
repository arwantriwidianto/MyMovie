package com.onecode.mymovie.checkout

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.app.NotificationCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.onecode.mymovie.R
import com.onecode.mymovie.checkout.adapter.CheckOutAdapter
import com.onecode.mymovie.checkout.model.Checkout
import com.onecode.mymovie.home.HomeActivity
import com.onecode.mymovie.utils.Preferences
import kotlinx.android.synthetic.main.activity_check_out.*
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class CheckOutActivity : AppCompatActivity() {

    private var dataList = ArrayList<Checkout>()
    private var total:Int = 0

    private lateinit var preferences: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_out)

        preferences = Preferences(this)
        dataList = intent.getSerializableExtra("data") as ArrayList<Checkout>


        for (a in dataList.indices){
            total += dataList[a].harga!!.toInt()
        }

        dataList.add(Checkout("Total Harus Dibayar", total.toString()))

        btn_tiket.setOnClickListener {
            val intent = Intent(this@CheckOutActivity, CheckOutSuccessActivity::class.java)
            startActivity(intent)

            showNotif()


        }

        btn_home.setOnClickListener {
            finishAffinity()
            val intent = Intent(this@CheckOutActivity, HomeActivity::class.java)
            startActivity(intent)
        }

        rc_checkout.layoutManager = LinearLayoutManager(this)
        rc_checkout.adapter = CheckOutAdapter(dataList) {
        }

        if(preferences.getValues("saldo")!!.isNotEmpty()){
            val localeID = Locale("in", "ID")
            val formatRupiah = NumberFormat.getCurrencyInstance(localeID)
            tv_saldo.setText(formatRupiah.format(preferences.getValues("saldo")!!.toDouble()))
            btn_tiket.visibility = View.VISIBLE
            keterangan.visibility = View.INVISIBLE
        } else {
            tv_saldo.setText("0")
            btn_tiket.visibility = View.INVISIBLE
            keterangan.visibility = View.VISIBLE
        }
    }

    private fun showNotif(){
        val NOTIFICATION_CHANNEL_ID = "channel_mymovie_notif"
        val context = this.applicationContext
        var notificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
            val channelName = "MYMOVIE Notif Channel"
            val importance = NotificationManager.IMPORTANCE_HIGH

            val mChannel = NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, importance)
            notificationManager.createNotificationChannel(mChannel)
        }

        val mIntent = Intent(this, CheckOutSuccessActivity::class.java)
        val bundle = Bundle()
        bundle.putString("id","id_film")
        mIntent.putExtras(bundle)

        val pendingIntent =
            PendingIntent.getActivity(this, 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
        builder.setContentIntent(pendingIntent)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setLargeIcon(
                BitmapFactory.decodeResource(
                    this.resources, R.mipmap.ic_launcher
                )
            )
            .setTicker("Notif My Movie")
            .setAutoCancel(true)
            .setVibrate(longArrayOf(1000,1000,1000,1000,1000))
            .setLights(Color.RED, 3000,3000)
            .setDefaults(Notification.DEFAULT_SOUND)
            .setContentTitle("Telah Berhasil !")
            .setContentText("MyMovie")

        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(115,builder.build())
    }
}
