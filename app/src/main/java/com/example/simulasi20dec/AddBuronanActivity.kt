package com.example.simulasi20dec

import android.app.Activity
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import com.google.android.material.color.utilities.DislikeAnalyzer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.DataOutputStream
import java.net.HttpURLConnection
import java.net.URL

class AddBuronanActivity : AppCompatActivity() {

    lateinit var buronanRbg: RadioGroup
    lateinit var kelaminRbg: RadioGroup
    lateinit var nama: EditText
    lateinit var golongan: EditText
    lateinit var kasus: EditText
    lateinit var kota: EditText
    lateinit var hukuman: EditText
    lateinit var kelamin: RadioGroup
    lateinit var buronan: RadioGroup


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_buronan)

        nama = findViewById<EditText>(R.id.b_nama)
        golongan = findViewById<EditText>(R.id.b_golongan)
        kasus = findViewById<EditText>(R.id.b_kasus)
        kota = findViewById<EditText>(R.id.b_kota)
        hukuman = findViewById<EditText>(R.id.b_hukuman)
        kelamin = findViewById(R.id.b_rg_kelamin)
        buronan = findViewById(R.id.b_rg_buronan)

        val btnSubmit = findViewById<AppCompatButton>(R.id.b_btn_submit)
        val btnKembali = findViewById<AppCompatButton>(R.id.b_btn_kembali)

        btnKembali.setOnClickListener {
            finish()
        }

        btnSubmit.setOnClickListener {
            if (nama.text.toString()
                    .isNullOrEmpty() || kelamin.checkedRadioButtonId == -1 || buronan.checkedRadioButtonId == -1 || golongan.text.toString()
                    .isNullOrEmpty() || kasus.text.toString()
                    .isNullOrEmpty() || kota.text.toString()
                    .isNullOrEmpty() || hukuman.text.toString().isNullOrEmpty()
            ) {
                val alert = AlertDialog.Builder(this@AddBuronanActivity)
                    .setTitle("Gagal !")
                    .setMessage("Semua input wajib di isi !")
                    .setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
                        dialog.dismiss()
                    })
                    .setCancelable(false)
                    .show()

                return@setOnClickListener
            }

            kelaminRbg = findViewById<RadioGroup>(R.id.b_rg_kelamin)
            var kelaminStr = ""
            if (kelaminRbg.checkedRadioButtonId != -1) {
                val checkedKelamin = findViewById<RadioButton>(kelaminRbg.checkedRadioButtonId)
                kelaminStr = checkedKelamin.text.toString()
            }


            buronanRbg = findViewById<RadioGroup>(R.id.b_rg_buronan)
            var buronanCheck = false
            val checkedBuronan = findViewById<RadioButton>(buronanRbg.checkedRadioButtonId)

            if (checkedBuronan.text == "Buronan") {
                buronanCheck = true
            }


            GlobalScope.launch(Dispatchers.IO) {
                val url =
                    URL("https://658d8f207c48dce947396725.mockapi.io/api/EsemkaPolice/Criminals")
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "POST"
                conn.setRequestProperty("Content-Type", "Application/json")

                val json = JSONObject().apply {
                    put("nama", nama.text.toString())
                    put("jenis_kelamin", kelaminStr)
                    put("masih_buronan", buronanCheck)
                    put("golongan_kasus", golongan.text.toString())
                    put("kasus_kriminal", kasus.text.toString())
                    put("kota", kota.text.toString())
                    put("hukuman", hukuman.text.toString())
                }

                Log.d("cek_data", json.toString())

                val output = DataOutputStream(conn.outputStream)
                output.write(json.toString().toByteArray())
                output.flush()

                val result = conn.inputStream.bufferedReader().readLines().toString()
                Log.d("response_api", result)
                val code = conn.responseCode

                GlobalScope.launch(Dispatchers.Main) {
                    if (code == 201) {
                        alertSuccess()
                        resetState()
                    } else {
                        alertError()
                    }
                }

            }
        }
    }

    fun alertSuccess() {
        AlertDialog.Builder(this)
            .setTitle("Suksess")
            .setMessage("Buronan berhasil di tambahkan !")
            .setCancelable(false)
            .setPositiveButton(
                "OK",
                DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() })
            .show()
    }

    fun alertError() {
        AlertDialog.Builder(this)
            .setTitle("Gagal")
            .setMessage("Buronan gagal di tambahkan !")
            .setCancelable(false)
            .setPositiveButton(
                "OK",
                DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() })
            .show()
    }

    fun resetState() {
        buronanRbg.clearCheck()
        kelaminRbg.clearCheck()
        nama.setText(null)
        golongan.setText(null)
        kasus.setText(null)
        kota.setText(null)
        hukuman.setText(null)
    }
}