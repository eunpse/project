package com.example.myapplication

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class MyDBHelper(context: Context) : SQLiteOpenHelper(context, "animalDB", null, 1) {
    lateinit var sqlDB: SQLiteDatabase
    lateinit var contentValues: ContentValues

    override fun onCreate(p0: SQLiteDatabase?) {
        //반려동물 정보
        p0!!.execSQL("CREATE TABLE animalTBL(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "animal_name VARCHAR(20), animal_img VARCHAR(20), animal_reg_num VARCHAR(20)," +
                " animal_gender VARCHAR(20), animal_breed VARCHAR(20), animal_age VARCHAR(20)," +
                " animal_birth VARCHAR(20), animal_feature TEXT);")
        //일정 정보
        p0!!.execSQL("CREATE TABLE animalScheduleTBL(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " animal_name VARCHAR(20), animal_reg_num VARCHAR(20), schedule_date VARCHAR(20)," +
                " schedule_contents TEXT)")
        //접종 정보
        p0!!.execSQL("CREATE TABLE animalInoculationTBL(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " animal_name VARCHAR(20), animal_reg_num VARCHAR(20), animal_img VARCHAR(20)," +
                " inoculation_date VARCHAR(20), inoculation_contents TEXT)")
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0!!.execSQL("DROP TABLE IF EXISTS animalTBL")
        p0!!.execSQL("DROP TABLE IF EXISTS animalScheduleTBL")
        p0!!.execSQL("DROP TABLE IF EXISTS animalInoculationTBL")
        onCreate(p0)
    }

    //새로운 반려동물 정보 입력
    fun insertAnimalInfoData(name:String, img:String, regNum:String, gender:String, breed:String, age:String, birth:String, feature:String){
        sqlDB = this.writableDatabase
        contentValues = ContentValues()
        contentValues.put("animal_name", name)
        contentValues.put("animal_img", img)
        contentValues.put("animal_reg_num", regNum)
        contentValues.put("animal_gender", gender)
        contentValues.put("animal_breed", breed)
        contentValues.put("animal_age", age)
        contentValues.put("animal_birth", birth)
        contentValues.put("animal_feature", feature)
        sqlDB.insert("animalTBL", null, contentValues)
        sqlDB.close()
    }

    //반려동물 정보 삭제
    fun deleteAnimalData(id: String){
        sqlDB = this.writableDatabase
        sqlDB.execSQL("delete from animalTBL where id='$id'")
        sqlDB.execSQL("delete from sqlite_sequence where name='animalTBL'")
        sqlDB.close()
    }

    //반려동물 정보 업데이트
    fun updateAnimalData(id: String, name:String, img:String, regNum:String, gender:String, breed:String, age:String, birth:String, feature:String){
        sqlDB = this.writableDatabase
        contentValues = ContentValues()
        contentValues.put("animal_name", name)
        contentValues.put("animal_img", img)
        contentValues.put("animal_reg_num", regNum)
        contentValues.put("animal_gender", gender)
        contentValues.put("animal_breed", breed)
        contentValues.put("animal_age", age)
        contentValues.put("animal_birth", birth)
        contentValues.put("animal_feature", feature)
        sqlDB.update("animalTBL", contentValues, "id=?", arrayOf(id))
        sqlDB.close()
    }

    //새로운 일정 추가
    fun insertScheduleData(name:String, regNum: String, date: String, contents: String){
        sqlDB = this.writableDatabase
        contentValues = ContentValues()
        contentValues.put("animal_name", name)
        contentValues.put("animal_reg_num", regNum)
        contentValues.put("schedule_date", date)
        contentValues.put("schedule_contents", contents)
        sqlDB.insert("animalScheduleTBL", null, contentValues)
        sqlDB.close()
    }

    //일정 삭제
    fun deleteScheduleData(id: String){
        sqlDB = this.writableDatabase
        sqlDB.execSQL("delete from animalScheduleTBL where id='$id'")
        sqlDB.execSQL("delete from sqlite_sequence where name='animalScheduleTBL'")
        sqlDB.close()
    }

    //일정 수정
    fun updateScheduleData(id: String, name:String, regNum: String, date: String, contents: String){
        sqlDB = this.writableDatabase
        contentValues = ContentValues()
        contentValues.put("animal_name", name)
        contentValues.put("animal_reg_num", regNum)
        contentValues.put("schedule_date", date)
        contentValues.put("schedule_contents", contents)
        sqlDB.update("animalScheduleTBL", contentValues, "id=?", arrayOf(id))
        sqlDB.close()
    }

    //접종 내역 추가
    fun insertInoculationData(name:String, regNum: String, img:String, date:String, contents:String){
        sqlDB = this.writableDatabase
        contentValues = ContentValues()
        contentValues.put("animal_name", name)
        contentValues.put("animal_reg_num", regNum)
        contentValues.put("animal_img", img)
        contentValues.put("inoculation_date", date)
        contentValues.put("inoculation_contents", contents)
        sqlDB.insert("animalInoculationTBL", null, contentValues)
        sqlDB.close()
    }

    //접종 내역 삭제
    fun deleteInoculationData(id: String){
        sqlDB = this.writableDatabase
        sqlDB.execSQL("delete from animalInoculationTBL where id='$id'")
        sqlDB.execSQL("delete from sqlite_sequence where name='animalInoculationTBL'")
        sqlDB.close()
    }

    //접종 내역 수정
    fun updateInoculationData(id: String, name:String, regNum: String, img:String, date:String, contents:String){
        sqlDB = this.writableDatabase
        contentValues = ContentValues()
        contentValues.put("animal_name", name)
        contentValues.put("animal_reg_num", regNum)
        contentValues.put("animal_img", img)
        contentValues.put("inoculation_date", date)
        contentValues.put("inoculation_contents", contents)
        sqlDB.update("animalInoculationTBL", contentValues, "id=?", arrayOf(id))
        sqlDB.close()
    }
}