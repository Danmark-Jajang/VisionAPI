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
            var tmpUser = User(1, "", "","")
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
        val cb1 = findViewById<CheckBox>(R.id.CbAllergy1)
        val cb2 = findViewById<CheckBox>(R.id.CbAllergy2)
        val cb3 = findViewById<CheckBox>(R.id.CbAllergy3)
        var cbArray = arrayOf(cb1, cb2, cb3)
        var alArray = arrayOf("", "", "")

        for(i in cbArray){
            if(i.isChecked){
                alArray[cbArray.indexOf(i)] = cbArray[cbArray.indexOf(i)].text.toString()
            }
        }

        val u : User = User(1, alArray[0], alArray[1], alArray[2])
        return u
    }

    fun setAllergyBox(user : User){
        val cb1 = findViewById<CheckBox>(R.id.CbAllergy1)
        val cb2 = findViewById<CheckBox>(R.id.CbAllergy2)
        val cb3 = findViewById<CheckBox>(R.id.CbAllergy3)
        var cbArray = arrayOf(cb1, cb2, cb3)
        var alArray = arrayOf(user.al1, user.al2, user.al3)

        for(i in 0..2){
            if(alArray[i] != ""){
                cbArray[i].isChecked = true
            }
        }
    }
}