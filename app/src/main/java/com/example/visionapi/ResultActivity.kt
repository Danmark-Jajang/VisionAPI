package com.example.visionapi

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.visionapi.databinding.ActivityResultBinding

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

        //문자열 유사도 분석으로 최대한 비슷한 제품 검색
        val name : List<String> = dbHelper.getProductsName()
        val jaroWinkler = JaroWinkler()
        //검색 결과가 serch에 들어갈 예정, 유사한 결과가 없으면 그대로
        var search = ""
        for(n in name){
            //유사도 80%이상일 때 검색
            if(jaroWinkler.similarity(n, text) > 0.8){
                search = n
            }
        }

        //TEST용 CODE
        //search = "이부프로펜"

        //제품 결과 출력
        val p : Product = dbHelper.getProduct(search)
        //제품 유무 검사
        val allist = mutableListOf<String>()
        binding.tvAnalize.text = search + "의 검색결과"
        if(p.name != "1"){
            Log.e("Result", "Product loaded")
            //유저 알러지 정보 조회
            val ua = getUserAllergy()
            Log.e("Result", "User Allergy loaded")

            //사용자 알러지 비교
            var flag = false
            for(u in ua){
                if(u != ""){
                    if(p.target == u){
                        binding.tvResult.text = "알러지가 검출되었습니다!!!!"
                        allist.add(p.target)
                        flag = true
                    }
                }
            }
            if(flag){
                binding.tvAnalize.setBackgroundColor(Color.rgb(251, 177, 162))
            }
        }
        else{
            binding.tvResult.text = "검색된 내용이 없습니다..."
        }

        //자세히 보기 Dialog
        binding.btnDetail.setOnClickListener{
            AlertDialog.Builder(this)
                .setTitle("자세히 보기")
                .setMessage(getMessage(allist))
                .setNegativeButton("닫기", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        //아무것도 하지 않고 닫기
                    }

                })
                .create()
                .show()
        }

        //닫기 버
        binding.btnGoBackCamera.setOnClickListener {
            finish()
        }

    }

    //유저 알러지만 조회
    private fun getUserAllergy() : Array<String> {
        val user = UserDatabase.getInstance(this)!!.UserDao().getUser(1)
        return arrayOf(user.al1, user.al2, user.al3, user.al4, user.al5, user.al6, user.al7, user.al8, user.al9, user.al10,
            user.al11, user.al12, user.al13, user.al14, user.al15, user.al16, user.al17, user.al18, user.al19, user.al20, user.al21)
    }

    fun getMessage(list: MutableList<String>) : String {
        var rtn : String = ""
        if(list.size == 0) return "인식된 내용이 없거나 DB에 없습니다...\n"
        for(n in list){
            rtn += n + "이/가 식별되었습니다\n"
        }
        return rtn
    }
}