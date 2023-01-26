package com.example.myapplication

import android.app.DatePickerDialog
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import java.util.*

class AddScheduleActivity : AppCompatActivity(), AnimalIDListener {
    //DB
    lateinit var myHelper: MyDBHelper

    lateinit var tvAnimalName: TextView
    lateinit var tvTitle: TextView

    lateinit var edScheduleDate : EditText
    lateinit var edScheduleContent: EditText

    lateinit var btnInsertData: Button
    lateinit var btnScheduleDate: Button

    lateinit var regNum: String
    var animalID: Int = -1
    var scheduleID: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_schedule)

        myHelper = MyDBHelper(this)
        tvAnimalName = findViewById<TextView>(R.id.animal_name)
        tvTitle = findViewById<TextView>(R.id.title)

        edScheduleDate = findViewById<EditText>(R.id.edt_schedule_date)
        edScheduleContent = findViewById<EditText>(R.id.edt_schedule_content)

        btnInsertData = findViewById<Button>(R.id.btn_insert_new_data)
        btnScheduleDate = findViewById<Button>(R.id.btn_schedule_date)

        animalID = intent.getIntExtra(ANIMAL_ID_EXTRA, -1)
        tvAnimalName.text = intent.getStringExtra("animal_name")
        regNum = intent.getStringExtra("regNum").toString()

        scheduleID = intent.getIntExtra(SCHEDULE_ID_EXTRA, -1)

        //추가, 수정 액티비티 여부에 따라 액션바 설정
        if(scheduleID != -1){
            setInfo()
            tvTitle.text = "일정 변경"
            //액션바 설정
            val ac: ActionBar? = supportActionBar
            ac?.title = "일정 변경"
        }
        else{
            tvTitle.text = "일정 추가"
            //액션바 설정
            val ac: ActionBar? = supportActionBar
            ac?.title = "일정 추가"
        }

        //날짜 선택
        btnScheduleDate.setOnClickListener {
            var cal: Calendar = Calendar.getInstance()
            val dateSetListener = DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->
                edScheduleDate.setText("${year}/${month+1}/${day}")
            }
            DatePickerDialog(this, dateSetListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(
                Calendar.DAY_OF_MONTH)).show()

        }

        //DB 삽입
        btnInsertData.setOnClickListener{
            //공백 체크
            if(checkInput())
                return@setOnClickListener

            if(scheduleID != -1){
                //데이터 수정
                myHelper.updateScheduleData(scheduleID.toString(), tvAnimalName.text.toString(),
                    regNum, edScheduleDate.text.toString(), edScheduleContent.text.toString())
            }
            else{
                //데이터 삽입
                myHelper.insertScheduleData(tvAnimalName.text.toString(),
                    regNum, edScheduleDate.text.toString(), edScheduleContent.text.toString())
            }

            animalID = intent.getIntExtra(ANIMAL_ID_EXTRA, -1)
            //접종 내역 리스트로 화면 이동
            var intent = Intent(this, ScheduleActivity::class.java)
            intent.putExtra("animal_name",tvAnimalName.text)
            intent.putExtra(ANIMAL_ID_EXTRA, animalID)
            intent.putExtra("animal_reg_num", regNum)
            startActivity(intent)
        }
    }

    //일정 액티비티로 이동
    override fun onOptionsItemSelected(item: MenuItem): Boolean{
        when(item.itemId){
            android.R.id.home ->{
                animalID = intent.getIntExtra(ANIMAL_ID_EXTRA, -1)
                val intent = Intent(this, ScheduleActivity::class.java)
                intent.putExtra(ANIMAL_ID_EXTRA, animalID)
                intent.putExtra("animal_name", tvAnimalName.text)
                intent.putExtra("animal_reg_num", regNum)
                startActivity(intent)
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //공백 체크
    private fun checkInput() : Boolean {
        if(TextUtils.isEmpty(edScheduleDate.text.toString())){
            Toast.makeText(baseContext, "일정 날짜를 입력해주세요", Toast.LENGTH_SHORT).show()
            return true
        }
        if(TextUtils.isEmpty(edScheduleContent.text.toString())){
            Toast.makeText(baseContext, "일정 내용을 적어주세요", Toast.LENGTH_SHORT).show()
            return true
        }

        return false
    }

    //기존 텍스트 설정
    private fun setInfo(){
        val schedule = scheduleFromID(scheduleID)
        if (schedule != null) {
            edScheduleDate.setText(schedule.schedule_date)
        }
        if (schedule != null) {
            edScheduleContent.setText(schedule.schedule_content)
        }
    }
}