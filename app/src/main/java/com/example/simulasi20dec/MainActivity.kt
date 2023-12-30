package com.example.simulasi20dec

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {
    lateinit var kriminal_rv: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        kriminal_rv = findViewById<RecyclerView>(R.id.krimival_rv)
        val btnAdd = findViewById<AppCompatButton>(R.id.btn_add_kriminal)

        btnAdd.setOnClickListener({
            val intent = Intent(this@MainActivity, AddBuronanActivity::class.java)
            startActivity(intent)
        })

        loadKriminal()
    }

    fun loadKriminal() {
        GlobalScope.launch(Dispatchers.IO) {
            val url = URL("https://658d8f207c48dce947396725.mockapi.io/api/EsemkaPolice/Criminals")
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "GET"

            val result = conn.inputStream.bufferedReader().readLines().toString()

            val json = JSONArray(result).getJSONArray(0)

            GlobalScope.launch(Dispatchers.Main) {
                kriminal_rv.adapter = KriminalAdapter(json)
                kriminal_rv.layoutManager = LinearLayoutManager(applicationContext)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loadKriminal()
    }

    class KriminalAdapter(val kriminals: JSONArray) :
        RecyclerView.Adapter<KriminalAdapter.KriminalViewHolder>() {
        class KriminalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            val namaKriminal: TextView
            val kasusKriminal: TextView
            val btnDetail: AppCompatButton

            init {
                namaKriminal = itemView.findViewById(R.id.nama_kriminal_tv)
                kasusKriminal = itemView.findViewById(R.id.kasus_kriminal_tv)
                btnDetail = itemView.findViewById(R.id.btn_detail_kriminal)
            }

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KriminalViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.criminal_card, parent, false)
            return KriminalViewHolder(view)
        }

        override fun getItemCount(): Int {
            return kriminals.length()
        }

        override fun onBindViewHolder(holder: KriminalViewHolder, position: Int) {
            val data = kriminals.getJSONObject(position)
            holder.namaKriminal.text = data.getString("nama")
            holder.kasusKriminal.text = data.getString("kasus_kriminal")

            holder.btnDetail.setOnClickListener {
                var buronan = 0
                if (data.getBoolean("masih_buronan")) {
                    buronan = 1
                }


                Log.d("cek_buronan", buronan.toString())

                val intent =
                    Intent(holder.itemView.context, DetailCriminalActivity::class.java).apply {
                        putExtra("nama", data.getString("nama"))
                        putExtra("jenis_kelamin", data.getString("jenis_kelamin"))
                        putExtra("masih_buronan", buronan)
                        putExtra("golongan_kasus", data.getString("golongan_kasus"))
                        putExtra("kasus_kriminal", data.getString("kasus_kriminal"))
                        putExtra("kota", data.getString("kota"))
                        putExtra("hukuman", data.getString("hukuman"))
                    }

                holder.itemView.context.startActivity(intent)
            }
        }
    }
}