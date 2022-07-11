package com.example.bookstore.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.bookstore.R
import com.example.bookstore.util.ConnectionManager
import com.squareup.picasso.Picasso
import org.json.JSONObject

class DescriptionActivity : AppCompatActivity() {

    private lateinit var txtBookName: TextView
    private lateinit var txtBookAuthor: TextView
    private lateinit var txtBookPrice: TextView
    private lateinit var txtBookRating: TextView
    private lateinit var imgBookImage: ImageView
    private lateinit var txtBookDesc: TextView
    private lateinit var btnAddToFav: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var progressLayout: RelativeLayout

    private var bookId: String? = "100"

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description)

        init()

        if (intent != null) {
            bookId = intent.getStringExtra("book_id")
        } else {
            finish()
            Toast.makeText(
                this,
                "Some Unexpected Error Occurred!!",
                Toast.LENGTH_SHORT
            )
                .show()
        }

        if (bookId == "100") {
            finish()
            Toast.makeText(
                this,
                "Some Unexpected Error Occurred!!",
                Toast.LENGTH_SHORT
            )
                .show()
        }

        val queue = Volley.newRequestQueue(this)

        val url = "http://13.235.250.119/v1/book/get_book/"

        val jsonParams = JSONObject()
        jsonParams.put("book_id", bookId)

        if (ConnectionManager().checkConnectivity(this)) {
            val jsonRequest =
                object : JsonObjectRequest(Method.POST, url, jsonParams, Response.Listener {

                    try {
                        val success = it.getBoolean("success")
                        if (success) {
                            val bookJsonObject = it.getJSONObject("book_data")
                            progressLayout.visibility = View.GONE
                            progressBar.visibility = View.GONE

                            Picasso.get().load(bookJsonObject.getString("image"))
                                .error(R.drawable.default_book_cover).into(imgBookImage)
                            txtBookName.text = bookJsonObject.getString("name")
                            txtBookAuthor.text = bookJsonObject.getString("author")
                            txtBookRating.text = bookJsonObject.getString("rating")
                            txtBookPrice.text = bookJsonObject.getString("price")
                            txtBookDesc.text = bookJsonObject.getString("description")

                        } else {
                            Toast.makeText(
                                this,
                                "Some Error Occurred!!",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(
                            this,
                            "Some Unexpected Error Occurred!!",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }, Response.ErrorListener
                {

                }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-type"] = "application/json"
                        headers["token"] = "6810b285fdec0e"
                        return headers
                    }
                }
            queue.add(jsonRequest)
        }else{
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Internet Connection not Found")
            builder.setPositiveButton(R.string.open_settings) { _, _ ->
                val settingIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingIntent)
                finish()
            }
            builder.setNegativeButton(R.string.exit) { _, _ ->
                ActivityCompat.finishAffinity(this)
            }
            builder.create()
            builder.show()
        }
    }

    private fun init() {
        txtBookName = findViewById(R.id.txtBookName)
        txtBookAuthor = findViewById(R.id.txtBookAuthor)
        txtBookPrice = findViewById(R.id.txtBookPrice)
        txtBookRating = findViewById(R.id.txtBookRating)
        imgBookImage = findViewById(R.id.imgBookImage)
        txtBookDesc = findViewById(R.id.txtBookDesc)
        btnAddToFav = findViewById(R.id.btnAddToFav)
        progressBar = findViewById(R.id.progressBar)
        progressBar.visibility = View.VISIBLE
        progressLayout = findViewById(R.id.progressLayout)
        progressLayout.visibility = View.VISIBLE
    }
}