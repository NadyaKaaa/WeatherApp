package com.nadyaka.weatherapp

import android.os.Bundle
import android.os.StrictMode
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        floatingSearch.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val imn: InputMethodManager =
                    getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imn.hideSoftInputFromWindow(currentFocus!!.rootView.windowToken, 0)
                api_key(java.lang.String.valueOf(searchET.getText()))
            }
        })
    }

    private fun api_key(City: String) {
        val client = OkHttpClient()
        val request: Request = Request.Builder()
            .url("https://api.openweathermap.org/data/2.5/weather?q=$City&appid=6475017a0663b31917b51bf8b35cbbdb&units=metric")
            .get()
            .build()
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        try {
            val response: Response = client.newCall(request).execute()
            client.newCall(request).enqueue(object : Callback {

                override fun onFailure(call: Call, e: IOException) {
                }

                override fun onResponse(call: Call, response: Response) {
                    val responseData: String = response.body?.string() ?: ""
                    try {
                        val json = JSONObject(responseData)
                        val array = json.getJSONArray("weather")
                        val `object` = array.getJSONObject(0)
                        val description = `object`.getString("description")
                        val icons = `object`.getString("icon")
                        val temp1 = json.getJSONObject("main")
                        val Temperature = temp1.getDouble("temp")
                        setText(town, City)
                        val temps = Math.round(Temperature).toString() + " °С"
                        setText(temp, temps)
                        setText(desc, description)
                        setImage(weatherImage, icons)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            })
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun setText(text: TextView, value: String) {
        runOnUiThread { text.text = value }
    }

    private fun setImage(imageView: ImageView, value: String) {
        runOnUiThread {
            when (value) {
                "01d" -> imageView.setImageDrawable(resources.getDrawable(R.drawable.icon1))
                "01n" -> imageView.setImageDrawable(resources.getDrawable(R.drawable.icon1))
                "02d" -> imageView.setImageDrawable(resources.getDrawable(R.drawable.icon2))
                "02n" -> imageView.setImageDrawable(resources.getDrawable(R.drawable.icon2))
                "03d" -> imageView.setImageDrawable(resources.getDrawable(R.drawable.icon3))
                "03n" -> imageView.setImageDrawable(resources.getDrawable(R.drawable.icon3))
                "04d" -> imageView.setImageDrawable(resources.getDrawable(R.drawable.icon4))
                "04n" -> imageView.setImageDrawable(resources.getDrawable(R.drawable.icon4))
                "09d" -> imageView.setImageDrawable(resources.getDrawable(R.drawable.icon5))
                "09n" -> imageView.setImageDrawable(resources.getDrawable(R.drawable.icon5))
                "10d" -> imageView.setImageDrawable(resources.getDrawable(R.drawable.icon6))
                "10n" -> imageView.setImageDrawable(resources.getDrawable(R.drawable.icon6))
                "11d" -> imageView.setImageDrawable(resources.getDrawable(R.drawable.icon7))
                "11n" -> imageView.setImageDrawable(resources.getDrawable(R.drawable.icon7))
                else -> imageView.setImageDrawable(resources.getDrawable(R.drawable.weather))
            }
        }
    }
}