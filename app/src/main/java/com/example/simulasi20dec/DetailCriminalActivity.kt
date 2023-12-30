package com.example.simulasi20dec

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton

class DetailCriminalActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_criminal)


        val nama = findViewById<TextView>(R.id.dk_nama_kriminal)
        val jk = findViewById<TextView>(R.id.dk_jenis_kelamin)
        val buronan = findViewById<TextView>(R.id.dk_buronan)
        val golongan = findViewById<TextView>(R.id.dk_golongan_kasus)
        val kasus = findViewById<TextView>(R.id.dk_kasus_kriminal)
        val kota = findViewById<TextView>(R.id.dk_kota)
        val hukuman = findViewById<TextView>(R.id.dk_hukuman)
        val btn_kembali = findViewById<AppCompatButton>(R.id.dk_btn_back)

        btn_kembali.setOnClickListener {
            finish()
        }

        if (intent.getIntExtra("buronan", 0).toString() == "0") {
            buronan.setBackgroundResource(R.drawable.bg_bukan_buron)
            buronan.setTextColor(resources.getColor(R.color.white_2))
            buronan.text = "Bukan Buronan"
        } else {
            buronan.setBackgroundResource(R.drawable.bg_buronan)
            buronan.setTextColor(resources.getColor(R.color.white_2))
            buronan.text = "Buronan"
        }

        nama.text = intent.getStringExtra("nama").toString()
        jk.text = intent.getStringExtra("jenis_kelamin").toString()
        golongan.text = intent.getStringExtra("golongan_kasus").toString().capitalize()
        kasus.text = intent.getStringExtra("kasus_kriminal").toString()
        kota.text = intent.getStringExtra("kota").toString()
        hukuman.text = intent.getStringExtra("hukuman").toString()


    }
}