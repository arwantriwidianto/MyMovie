package com.onecode.mymovie.home.dashboard


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.database.*
import com.onecode.mymovie.DetailActivity

import com.onecode.mymovie.R
import com.onecode.mymovie.home.adapter.ComingSoonAdapter
import com.onecode.mymovie.home.adapter.NowPlayingAdapter
import com.onecode.mymovie.home.model.Film
import com.onecode.mymovie.utils.Preferences
import kotlinx.android.synthetic.main.fragment_dashboard.*
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class DashboardFragment : Fragment() {

//    database sementara
    private lateinit var preferences: Preferences
//    database dari firebase
    lateinit var mDatabase: DatabaseReference

//    penampung untuk data nya nanti
    private var dataList = ArrayList<Film>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

//        initialisasi pertama
        preferences = Preferences(activity!!.applicationContext)
        mDatabase = FirebaseDatabase.getInstance().getReference("Film")

//        mengambil value dari database sementara
//        menampilkan nama
        tv_nama.setText(preferences.getValues("nama"))
//        menapilkan saldo
        if (!preferences.getValues("saldo").equals("")){
            currecy(preferences.getValues("saldo")!!.toDouble(), tv_saldo)
        }

//        menapilkan gambar user
        Glide.with(this)
            .load(preferences.getValues("url"))
            .apply(RequestOptions.circleCropTransform())
            .into(iv_profile)

//        recycle view
        rv_now_playing.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rv_coming_soon.layoutManager = LinearLayoutManager(context!!.applicationContext)
        getData()

    }

    private fun getData() {
        mDatabase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                dataList.clear()
                for (getdataSnapshot in dataSnapshot.getChildren()) {

                    val film = getdataSnapshot.getValue(Film::class.java!!)
                    dataList.add(film!!)
                }

                rv_now_playing.adapter =
                    NowPlayingAdapter(dataList) {
                        val intent =
                            Intent(context, DetailActivity::class.java).putExtra("data", it)
                        startActivity(intent)
                    }

                rv_coming_soon.adapter =
                    ComingSoonAdapter(dataList) {
                        val intent =
                            Intent(context, DetailActivity::class.java).putExtra("data", it)
                        startActivity(intent)
                    }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, ""+error.message, Toast.LENGTH_LONG).show()
            }
        })
    }

//    format mata uang, dari firebase dirubah
    private fun currecy(harga:Double, textView: TextView) {
        val localeID = Locale("in", "ID")
        val formatRupiah = NumberFormat.getCurrencyInstance(localeID)
        textView.setText(formatRupiah.format(harga as Double))

    }


}
