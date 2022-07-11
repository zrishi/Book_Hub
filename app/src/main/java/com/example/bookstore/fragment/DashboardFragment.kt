package com.example.bookstore.fragment


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.bookstore.R
import com.example.bookstore.adapter.DashBoardRecyclerAdapter
import com.example.bookstore.model.Book
import com.example.bookstore.util.ConnectionManager
import org.json.JSONException


class DashboardFragment : Fragment() {

    private lateinit var recyclerDashBoard: RecyclerView
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var recyclerAdapter: DashBoardRecyclerAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var progressLayout: RelativeLayout


    private val bookList = arrayListOf<Book>()

    @SuppressLint("NewApi")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        recyclerDashBoard = view.findViewById(R.id.recyclerViewDashBoard)
        layoutManager = LinearLayoutManager(activity)

        progressBar = view.findViewById(R.id.progressBar)
        progressLayout = view.findViewById(R.id.progressLayout)

        progressLayout.visibility = View.VISIBLE

        val queue = Volley.newRequestQueue(activity as Context)

        val url = "http://13.235.250.119/v1/book/fetch_books/"

        if (ConnectionManager().checkConnectivity(activity as Context)) {
            val jsonObjectRequest =
                object : JsonObjectRequest(Method.GET, url, null, Response.Listener {

                     try {

                         progressLayout.visibility = View.GONE

                         val success = it.getBoolean("success")

                         if (success) {
                             val data = it.getJSONArray("data")
                             for (i in 0 until data.length()) {
                                 val bookJsonObject = data.getJSONObject(i)
                                 val bookObject = Book(
                                     bookJsonObject.getString("book_id"),
                                     bookJsonObject.getString("name"),
                                     bookJsonObject.getString("author"),
                                     bookJsonObject.getString("rating"),
                                     bookJsonObject.getString("price"),
                                     bookJsonObject.getString("image")
                                 )
                                 bookList.add(bookObject)
                                 recyclerAdapter =
                                     DashBoardRecyclerAdapter(activity as Context, bookList)

                                 recyclerDashBoard.adapter = recyclerAdapter
                                 recyclerDashBoard.layoutManager = layoutManager


                             }
                         } else {
                             Toast.makeText(
                                 activity as Context,
                                 "Some Error Occurred!!",
                                 Toast.LENGTH_SHORT
                             )
                                 .show()
                         }
                     } catch (e: JSONException) {
                         Toast.makeText(
                             activity as Context,
                             "Some Unexpected Error Occurred!!",
                             Toast.LENGTH_SHORT
                         )
                             .show()
                     }


                }, Response.ErrorListener {

                    Toast.makeText(
                        activity as Context,
                        "Some Error Occurred!!",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-type"] = "application/json"
                        headers["token"] = "6810b285fdec0e"
                        return headers
                    }
                }

            queue.add(jsonObjectRequest)

        } else {
            val builder = AlertDialog.Builder(activity as Context)
            builder.setMessage("Internet Connection not Found")
            builder.setPositiveButton(R.string.open_settings) { _, _ ->
                val settingIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingIntent)
                activity?.finish()
            }
            builder.setNegativeButton(R.string.exit) { _, _ ->
                ActivityCompat.finishAffinity(activity as Activity)
            }
            builder.create()
            builder.show()

        }
        return view
    }

}