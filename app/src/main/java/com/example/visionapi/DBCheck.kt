package com.example.visionapi

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DBCheck : AppCompatActivity() {
    private lateinit var dbHelper: ProductDatabaseHelper
    private lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dbcheck)

        dbHelper = ProductDatabaseHelper(this)
        textView = findViewById(R.id.textView)

        // 데이터 삽입
        insertSampleProducts()

        // 데이터 삭제 및 화면에 출력
        //deleteSampleProduct()
        displayProducts()
    }

    private fun insertSampleProducts() {
        dbHelper.insertProduct("신짱", "156Kcal", "대두", "120g")
        dbHelper.insertProduct("계란과자", "205kcal", "계란", "45g")
        dbHelper.insertProduct("꿀꽈배기", "450Kcal", "우유", "90g")
        dbHelper.insertProduct("새우깡", "515Kcal", "새우", "400g")
    }

    private fun deleteSampleProduct() {
        dbHelper.deleteProduct("신짱")
        dbHelper.deleteProduct("계란과자")
        dbHelper.deleteProduct("꿀꽈배기")
        dbHelper.deleteProduct("새우깡")
    }

    private fun displayProducts() {
        val products = dbHelper.getAllProducts()
        val productListString = StringBuilder()



        products.forEach { product ->
            productListString.append("${product.name}, ${product.kcal}, ${product.target}, ${product.total}\n")
        }
        textView.text = productListString.toString()

    }
}