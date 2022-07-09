package com.metaplex.sample

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class PhantomConnectActivity : AppCompatActivity() {
    private lateinit var loginWithPhantomBtn : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phantom_connect)

        loginWithPhantomBtn = findViewById(R.id.loginWithPhantomBtn)
        loginWithPhantomBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}