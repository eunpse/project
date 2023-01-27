package com.example.myapplication

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AnimalDetailActivity : AppCompatActivity(), AnimalIDListener {
    //DB
    lateinit var myHelper: MyDBHelper
    lateinit var sqlDB: SQLiteDatabase

    lateinit var imgAnimal: ImageView
    lateinit var tvName: TextView
    lateinit var tvRegNum: TextView
    lateinit var tvBreed: TextView
    lateinit var tvAge: TextView
    lateinit var tvGender: TextView
    lateinit var tvBirth: TextView
    lateinit var tvFeature: TextView

    lateinit var btnInoculationHistory: Button
    lateinit var btnSchedule: Button
    lateinit var btnRemove: FloatingActionButton
    lateinit var btnEdit: FloatingActionButton

    var animalID: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animal_detail)

        //액션바 설정
        val ac: ActionBar? = supportActionBar
        ac?.title = "반려동물 프로필"

        myHelper = MyDBHelper(this)

        imgAnimal = findViewById<ImageView>(R.id.animal_img)
        tvName = findViewById<TextView>(R.id.animal_name)
        tvRegNum = findViewById<TextView>(R.id.animal_reg_num)
        tvBreed = findViewById<TextView>(R.id.animal_breed)
        tvAge = findViewById<TextView>(R.id.animal_age)
        tvGender = findViewById<TextView>(R.id.animal_gender)
        tvBirth = findViewById<TextView>(R.id.animal_birth)
        tvFeature = findViewById<TextView>(R.id.animal_feature)

        btnInoculationHistory = findViewById<Button>(R.id.inoculation_history_btn)
        btnSchedule = findViewById<Button>(R.id.schedule_btn)
        btnRemove = findViewById<FloatingActionButton>(R.id.btn_remove)
        btnEdit = findViewById<FloatingActionButton>(R.id.btn_edit)

        sqlDB = myHelper.readableDatabase
        var cursor: Cursor

        cursor = sqlDB.rawQuery("SELECT * FROM animalTBL WHERE id;", null)

        animalID = intent.getIntExtra(ANIMAL_ID_EXTRA, -1)
        val animal = animalFromID(animalID)
        //미리보기 이미지 설정
        if(animal != null){
            if(animal.animal_img.toString() != "")
                imgAnimal.setImageURI(animal.animal_img)
            else
                imgAnimal.setImageResource(R.mipmap.ic_launcher_default_animal)
            //텍스트 설정
            tvName.text = animal.animal_name
            tvRegNum.text = animal.animal_reg_num
            tvBreed.text = animal.animal_breed
            tvAge.text = animal.animal_age
            tvGender.text = animal.animal_gender
            tvBirth.text = animal.animal_birth
            tvFeature.text = animal.animal_feature
        }

        cursor.close()
        sqlDB.close()

        //접종 내역 리스트 액티비티로 이동
        btnInoculationHistory.setOnClickListener {
            var intent = Intent(this, InoculationHistoryActivity::class.java)
            intent.putExtra("animal_name",tvName.text)
            intent.putExtra("animal_reg_num", tvRegNum.text)
            intent.putExtra(ANIMAL_ID_EXTRA, animalID)
            startActivity(intent)
        }

        //일정 리스트 액티비티로 이동
        btnSchedule.setOnClickListener {
            var intent = Intent(this, ScheduleActivity::class.java)
            intent.putExtra("animal_name",tvName.text)
            intent.putExtra("animal_reg_num", tvRegNum.text)
            intent.putExtra(ANIMAL_ID_EXTRA, animalID)
            startActivity(intent)
        }

        //동물 정보 삭제
        btnRemove.setOnClickListener {
            myHelper.deleteAnimalData(animal?.id.toString())

            var intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
        }

        //동물 정보 수정
        btnEdit.setOnClickListener {
            var intent = Intent(this, AddDataActivity::class.java)
            intent.putExtra(ANIMAL_ID_EXTRA, animalID)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        var intent = Intent(this, SettingActivity::class.java)
        startActivity(intent)
    }
}