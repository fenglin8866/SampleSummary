package com.xxh.summary

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        Log.i("xxh1234","onCreate")
        findViewById<Button>(R.id.button).setOnClickListener {
            Log.i("xxh1234","StartA")
            getPushIsInstall()
            Log.i("xxh1234","EndB")
        }
    }

    fun getPushIsInstall() {
        try {
            Log.i("xxh1234","Start")
            val PROVIDER_AUTHORITY = "com.zte.nubrowser.install";
            val PROVIDER_URI = Uri.parse("content://" + PROVIDER_AUTHORITY);
            val data = Bundle()
            val result = getContentResolver().call(PROVIDER_URI, "isBrowserInstall", null, data);
            if(result!=null){
                val isInstall = result.getBoolean("status",false)
                Log.i("xxh1234",isInstall.toString())
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.i("xxh1234",e.message.toString())
        }
    }
}