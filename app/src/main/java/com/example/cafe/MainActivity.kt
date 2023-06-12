package com.example.cafe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.cafe.databinding.ActivityMainBinding
import com.kakao.sdk.common.util.Utility

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        var keyHash = Utility.getKeyHash(this)
        Log.e("키값: ",keyHash.toString())

        // 앱 실행시 로그인 앧티비티로 바로 이동
        startActivity(Intent(this, LoginActivity::class.java))
    }
}