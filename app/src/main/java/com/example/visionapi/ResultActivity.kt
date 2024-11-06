package com.example.visionapi

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.visionapi.databinding.ActivityResultBinding
import java.util.ArrayList
import java.util.Random

class ResultActivity : AppCompatActivity() {
    lateinit var binding: ActivityResultBinding
    lateinit var dbHelper: ProductDatabaseHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //Camera Activity에서 가져온 String 가져오기
        val text : String = intent.getStringExtra("text").toString()
        dbHelper = ProductDatabaseHelper(this)
        binding.tvResult.text = text

        var p : Product = dbHelper.getProduct("새우깡")
        Toast.makeText(this, p.name, Toast.LENGTH_SHORT).show()
        Log.e("Result", "Product loaded")
        val ua = getUserAllergy()
        Log.e("Result", "User Allergy loaded")

        when(p.target){
            "대두" -> if(ua[0]){binding.tvResult.text = binding.tvResult.text.toString() + "대두알러지 조심!\n"}
            "새우" -> if(ua[1]){binding.tvResult.text = binding.tvResult.text.toString() + "새우알러지 조심!\n"}
            "계란" -> if(ua[2]){binding.tvResult.text = binding.tvResult.text.toString() + "계란알러지 조심!\n"}
            else -> {binding.tvResult.text = binding.tvResult.text.toString() + "알러지 걱정 없습니다\n"}
        }


        binding.btnGoBackCamera.setOnClickListener {
            finish()
        }

    }

    fun getUserAllergy() : Array<Boolean> {
        val u = UserDatabase.getInstance(this)!!.UserDao().getUser(1)
        return arrayOf(u.al1, u.al2, u.al3)
    }
}