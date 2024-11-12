package com.example.visionapi

import android.graphics.Color
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

        val name : List<String> = dbHelper.getProductsName()

        val jaroWinkler = JaroWinkler()
        var serch = ""
        for(n in name){
            //유사도
            if(jaroWinkler.similarity(n, text) > 0.8){
                serch = n
            }
        }

        //제품 결과 출력
        var p : Product = dbHelper.getProduct(serch)
        //제품 유무 검사
        if(p.name != "1"){
            binding.tvAnalize.text = p.name
            Log.e("Result", "Product loaded")

            //유저 알러지 정보 조회
            val ua = getUserAllergy()
            Log.e("Result", "User Allergy loaded")

            //사용자 알러지 비교
            var flag = false
            for(u in ua){
                if(u != ""){
                    if(p.target == u){
                        binding.tvResult.text = binding.tvResult.text.toString() + u + " : " + "O\n"
                        flag = true
                    }
                    else{
                        binding.tvResult.text = binding.tvResult.text.toString() + u + " : " + "X\n"
                    }
                }
            }
            if(flag){
                binding.tvAnalize.setBackgroundColor(Color.MAGENTA)
            }
        }
        else{
            binding.tvAnalize.text = "검색된 내용이 없습니다..."
        }



        binding.btnGoBackCamera.setOnClickListener {
            finish()
        }

    }

    //유저 알러지 조회
    fun getUserAllergy() : Array<String> {
        val u = UserDatabase.getInstance(this)!!.UserDao().getUser(1)
        return arrayOf(u.al1, u.al2, u.al3)
    }
}