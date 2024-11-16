package com.example.visionapi

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.CheckBox
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update
import com.example.visionapi.databinding.ActivityCheckAllergyBinding
import java.util.ArrayList

class CheckAllergy : AppCompatActivity() {
    lateinit var binding: ActivityCheckAllergyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCheckAllergyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //화면 표시때 유저데이터 없으면 초기화
        val userDB = UserDatabase.getInstance(applicationContext)
        if(userDB!!.UserDao().getUserNum() <= 0){
            var tmpUser = User(1, "", "","","","","","","","","","","","","","",""
                ,"","","")
            userDB.UserDao().insertUser(tmpUser)
        }

        //화면 불러올 때 기존 선택한 알러지 불러오기
        setAllergyBox(userDB.UserDao().getUser(1))
        Log.e("Allergy", "User allergy loaded")

        //설정해둔 알러지 저장
        binding.BtnSaveInfo.setOnClickListener{
            userDB.UserDao().updateUser(checkAllergyBox())
            Toast.makeText(this, "알러지가 수정되었습니다!", Toast.LENGTH_SHORT)
                .show()
        }

        //뒤로가기
        binding.BtnGoBackMain.setOnClickListener{
            finish()
        }
    }

    fun checkAllergyBox() : User{
        val cb1 = findViewById<CheckBox>(R.id.cb1)
        val cb2 = findViewById<CheckBox>(R.id.cb2)
        val cb3 = findViewById<CheckBox>(R.id.cb3)
        val cb4 = findViewById<CheckBox>(R.id.cb4)
        val cb5 = findViewById<CheckBox>(R.id.cb5)
        val cb6 = findViewById<CheckBox>(R.id.cb6)
        val cb7 = findViewById<CheckBox>(R.id.cb7)
        val cb8 = findViewById<CheckBox>(R.id.cb8)
        val cb9 = findViewById<CheckBox>(R.id.cb9)
        val cb10 = findViewById<CheckBox>(R.id.cb10)
        val cb11 = findViewById<CheckBox>(R.id.cb11)
        val cb12 = findViewById<CheckBox>(R.id.cb12)
        val cb13 = findViewById<CheckBox>(R.id.cb13)
        val cb14 = findViewById<CheckBox>(R.id.cb14)
        val cb15 = findViewById<CheckBox>(R.id.cb15)
        val cb16 = findViewById<CheckBox>(R.id.cb16)
        val cb17 = findViewById<CheckBox>(R.id.cb17)
        val cb18 = findViewById<CheckBox>(R.id.cb18)
        val cb19 = findViewById<CheckBox>(R.id.cb19)



        var cbArray = arrayOf(cb1, cb2, cb3, cb4, cb5, cb6, cb7, cb8, cb9, cb10, cb11, cb12, cb13, cb14, cb15, cb16, cb17, cb18, cb19)
        var alArray = arrayOf("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "")

        for(i in cbArray){
            if(i.isChecked){
                alArray[cbArray.indexOf(i)] = cbArray[cbArray.indexOf(i)].text.toString()
            }
        }

        val u : User = User(1, alArray[0], alArray[1], alArray[2], alArray[3], alArray[4], alArray[5], alArray[6], alArray[7], alArray[8]
            , alArray[9], alArray[10], alArray[11], alArray[12], alArray[13], alArray[14], alArray[15], alArray[16], alArray[17], alArray[18])
        return u
    }

    fun setAllergyBox(user : User){
        val cb1 = findViewById<CheckBox>(R.id.cb1)
        val cb2 = findViewById<CheckBox>(R.id.cb2)
        val cb3 = findViewById<CheckBox>(R.id.cb3)
        val cb4 = findViewById<CheckBox>(R.id.cb4)
        val cb5 = findViewById<CheckBox>(R.id.cb5)
        val cb6 = findViewById<CheckBox>(R.id.cb6)
        val cb7 = findViewById<CheckBox>(R.id.cb7)
        val cb8 = findViewById<CheckBox>(R.id.cb8)
        val cb9 = findViewById<CheckBox>(R.id.cb9)
        val cb10 = findViewById<CheckBox>(R.id.cb10)
        val cb11 = findViewById<CheckBox>(R.id.cb11)
        val cb12 = findViewById<CheckBox>(R.id.cb12)
        val cb13 = findViewById<CheckBox>(R.id.cb13)
        val cb14 = findViewById<CheckBox>(R.id.cb14)
        val cb15 = findViewById<CheckBox>(R.id.cb15)
        val cb16 = findViewById<CheckBox>(R.id.cb16)
        val cb17 = findViewById<CheckBox>(R.id.cb17)
        val cb18 = findViewById<CheckBox>(R.id.cb18)
        val cb19 = findViewById<CheckBox>(R.id.cb19)
        var cbArray = arrayOf(cb1, cb2, cb3, cb4, cb5, cb6, cb7, cb8, cb9, cb10, cb11, cb12, cb13, cb14, cb15, cb16, cb17, cb18, cb19)
        var alArray = arrayOf(user.al1, user.al2, user.al3, user.al4, user.al5, user.al6, user.al7, user.al8, user.al9, user.al10,
            user.al11, user.al12, user.al13, user.al14, user.al15, user.al16, user.al17, user.al18, user.al19)

        for(i in 0..18){
            if(alArray[i] != ""){
                cbArray[i].isChecked = true
            }
        }
    }
}