package com.example.visionapi

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.visionapi.databinding.ActivityResultBinding
import java.util.Random

class ResultActivity : AppCompatActivity() {
    lateinit var binding: ActivityResultBinding
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

        var text : String = intent.getStringExtra("text").toString()
        binding.tvResult.text = text

        var u = getUserAllergy()
        var r = getProductAllergy()

        if(u.al1==r[0]){
            binding.tvResult.text = binding.tvResult.text as String + '\n' + "1 true"
        }
        else binding.tvResult.text = binding.tvResult.text as String + '\n' + "1 false"
        if(u.al2==r[1]){
            binding.tvResult.text = binding.tvResult.text as String + '\n' + "2 true"
        }
        else binding.tvResult.text = binding.tvResult.text as String + '\n' + "2 false"
        if(u.al3==r[2]){
            binding.tvResult.text = binding.tvResult.text as String + '\n' + "3 true"
        }
        else binding.tvResult.text = binding.tvResult.text as String + '\n' + "3 false"


        binding.btnGoBackCamera.setOnClickListener {
            finish()
        }

    }

    fun getUserAllergy() : User{
        return UserDatabase.getInstance(applicationContext)!!.UserDao().getUser(1)
    }

    fun getProductAllergy(): Array<Boolean> {
        val random = Random()
        return arrayOf(random.nextBoolean(), random.nextBoolean(), random.nextBoolean())
    }
}