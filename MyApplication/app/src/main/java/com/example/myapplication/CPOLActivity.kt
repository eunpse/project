package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar

class CPOLActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cpolactivity)

        //액션바 설정
        val ac: ActionBar? = supportActionBar
        ac?.title = "오픈소스라이센스"
    }
}