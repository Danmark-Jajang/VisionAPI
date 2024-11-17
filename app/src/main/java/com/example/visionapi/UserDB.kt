package com.example.visionapi

import android.content.Context
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


@Entity
data class User(
    @PrimaryKey val uid : Int,
    @ColumnInfo(name = "대두") val al1 : String,
    @ColumnInfo(name = "새우") val al2 : String,
    @ColumnInfo(name = "계란") val al3 : String,
    @ColumnInfo(name = "소고기") val al4 : String,
    @ColumnInfo(name = "돼지고기") val al5 : String,
    @ColumnInfo(name = "닭고기") val al6 : String,
    @ColumnInfo(name = "게") val al7 : String,
    @ColumnInfo(name = "오징어") val al8 : String,
    @ColumnInfo(name = "고등어") val al9 : String,
    @ColumnInfo(name = "조개") val al10 : String,
    @ColumnInfo(name = "우유") val al11 : String,
    @ColumnInfo(name = "땅콩") val al12 : String,
    @ColumnInfo(name = "호두") val al13 : String,
    @ColumnInfo(name = "잣") val al14 : String,
    @ColumnInfo(name = "복숭아") val al15 : String,
    @ColumnInfo(name = "토마토") val al16 : String,
    @ColumnInfo(name = "밀") val al17 : String,
    @ColumnInfo(name = "메밀") val al18 : String,
    @ColumnInfo(name = "아황산류") val al19 : String,
    @ColumnInfo(name = "아세트아미노펜") val al20 : String,
    @ColumnInfo(name = "나프록센") val al21 : String,
)

@Dao
interface UserDao{
    @Query("SELECT COUNT(*) FROM User")
    fun getUserNum() : Int

    @Insert
    fun insertUser(user : User)

    @Update
    fun updateUser(user : User)

    @Query("SELECT * FROM User WHERE uid=:uid")
    fun getUser(uid : Int) : User

    @Query("SELECT uid FROM User")
    fun getUsers() : Array<Int>
}

@Database(entities = [User::class], version = 1)
abstract class UserDatabase : RoomDatabase(){
    abstract fun UserDao() : UserDao

    companion object{
        private var instance : UserDatabase? = null
        @Synchronized
        fun getInstance(context : Context) : UserDatabase?{
            if (instance == null){
                synchronized(UserDatabase::class){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        UserDatabase::class.java,
                        "user-database"
                    ).allowMainThreadQueries()
                        .build()
                }
            }
            return instance
        }
    }
}