package com.junli.retrofit_demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.junli.retrofit_demo.network.BoutiqueNormalUserApi
import com.junli.retrofit_demo.network.RetrofitHttpClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private var btnRetrofitGet: View? = null
    private var btnRetrofitPost: View? = null
    private var retrofitTextView: TextView? = null
    private val boutiqueNormalUserApiService =
        RetrofitHttpClient.create(BoutiqueNormalUserApi::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        viewEvent()


    }

    private fun viewEvent() {
        btnRetrofitGet?.setOnClickListener {
            runBlocking {
                updateRetrofit()
            }
        }
    }

    private suspend fun updateRetrofit() {
        val normalUser = boutiqueNormalUserApiService.getNormalUser(1L)
        retrofitTextView?.text = normalUser.toString()
    }

    private fun initView() {
        btnRetrofitGet = findViewById<View>(R.id.retrofit_get_btn)
        btnRetrofitPost = findViewById<View>(R.id.retrofit_post_btn)
        retrofitTextView = findViewById(R.id.retrofit_textview)

    }
}
