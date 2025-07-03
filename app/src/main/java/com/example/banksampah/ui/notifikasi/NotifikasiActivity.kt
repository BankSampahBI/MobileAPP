package com.example.banksampah.ui.notifikasi

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.banksampah.MainActivity
import com.example.banksampah.R
import com.example.banksampah.ui.model.Notifikasi
import com.example.banksampah.ui.adapter.ListNotifikasiAdapter

class NotifikasiActivity : AppCompatActivity() {

    private lateinit var rvNotifikasi: RecyclerView
    private val list = ArrayList<Notifikasi>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_notifikasi)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        rvNotifikasi = findViewById(R.id.rv_notifikasi)
        rvNotifikasi.setHasFixedSize(true)
        rvNotifikasi.layoutManager = LinearLayoutManager(this)

        list.addAll(getListNotifikasi())
        rvNotifikasi.adapter = ListNotifikasiAdapter(list)

        val btnBack: ImageView = findViewById(R.id.iv_back)
        btnBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getListNotifikasi(): ArrayList<Notifikasi> {
        val message = resources.getStringArray(R.array.message)
        val saldo = resources.getStringArray(R.array.saldo_notif)

        val listNotif = ArrayList<Notifikasi>()
        for (i in message.indices) {
            val notif = Notifikasi(message[i], saldo[i])
            listNotif.add(notif)
        }
        return listNotif
    }
}
