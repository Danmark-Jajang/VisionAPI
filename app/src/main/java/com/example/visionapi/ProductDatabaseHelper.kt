package com.example.visionapi

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ProductDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "mydatabase.db"
        private const val DATABASE_VERSION = 1

        const val TABLE_PRODUCT = "product"
        const val COLUMN_NAME = "name"
        const val COLUMN_KCAL = "kcal"
        const val COLUMN_TARGET = "target"
        const val COLUMN_TOTAL = "total"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createProductTableQuery = ("CREATE TABLE $TABLE_PRODUCT (" +
                "$COLUMN_NAME TEXT, " +
                "$COLUMN_KCAL TEXT, " +
                "$COLUMN_TARGET TEXT, " +
                "$COLUMN_TOTAL TEXT)"
                )
        db.execSQL(createProductTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PRODUCT")
        onCreate(db)
    }

    // 데이터 삽입 함수
    fun insertProduct(name: String, kcal: String, target: String, total: String) {
        val db = writableDatabase

        val values = ContentValues().apply {
            put(COLUMN_NAME, name)
            put(COLUMN_KCAL, kcal)
            put(COLUMN_TARGET, target)
            put(COLUMN_TOTAL, total)
        }

        db.insert(TABLE_PRODUCT, null, values)
        db.close()
    }

    // 데이터 조회 함수
    fun getAllProducts(): List<Product> {
        val db = readableDatabase
        val cursor = db.query(TABLE_PRODUCT, null, null, null, null, null, null)
        val products = mutableListOf<Product>()

        with(cursor) {
            while (moveToNext()) {
                val name = getString(getColumnIndexOrThrow(COLUMN_NAME))
                val kcal = getString(getColumnIndexOrThrow(COLUMN_KCAL))
                val target = getString(getColumnIndexOrThrow(COLUMN_TARGET))
                val total = getString(getColumnIndexOrThrow(COLUMN_TOTAL))
                products.add(Product(name, kcal, target, total))
            }
        }
        cursor.close()
        db.close()

        return products
    }

    // 데이터 삭제 함수
    fun deleteProduct(name: String) {
        val db = writableDatabase
        db.delete(TABLE_PRODUCT, "$COLUMN_NAME = ?", arrayOf(name))
        db.close()
    }


    // 데이터 클래스 정의
    data class Product(val name: String, val kcal: String, val target: String, val total: String)
}
