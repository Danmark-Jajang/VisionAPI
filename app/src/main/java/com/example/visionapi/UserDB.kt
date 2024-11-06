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
    @ColumnInfo(name = "대두") val al1 : Boolean,
    @ColumnInfo(name = "새우") val al2 : Boolean,
    @ColumnInfo(name = "계란") val al3 : Boolean
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