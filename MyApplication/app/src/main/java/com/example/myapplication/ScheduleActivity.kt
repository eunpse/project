package com.example.myapplication

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ScheduleActivity : AppCompatActivity(), ScheduleAdapter.ScheduleListener {
    //DB
    lateinit var myHelper: MyDBHelper
    lateinit var sqlDB: SQLiteDatabase

    //관련된 일정의 반려동물 이름
    lateinit var tvAnimalName: TextView
    //일정 추가 버튼
    lateinit var addScheduleBtn: FloatingActionButton

    //반려동물 리스트 출력을 위한 변수
    lateinit var rView: RecyclerView
    lateinit var vAdpater: RecyclerView.Adapter<*>
    lateinit var vManager: RecyclerView.LayoutManager

    var animalID: Int = -1
    var scheduleID: Int = -1
    lateinit var regNum: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule)

        //액션바 설정
        val ac: ActionBar? = supportActionBar
        ac?.title = "일정 리스트"

        myHelper = MyDBHelper(this)

        tvAnimalName = findViewById<TextView>(R.id.animal_name)
        addScheduleBtn = findViewById<FloatingActionButton>(R.id.new_schedule_insert)

        tvAnimalName.text = intent.getStringExtra("animal_name")
        regNum = intent.getStringExtra("animal_reg_num").toString()

        //화면에 필요한 구성 설정
        sqlDB = myHelper.readableDatabase
        var cursor: Cursor

        cursor = sqlDB.rawQuery("SELECT * FROM animalTBL WHERE id;", null)
        animalID = intent.getIntExtra(ANIMAL_ID_EXTRA, -1)
        scheduleID = intent.getIntExtra(SCHEDULE_ID_EXTRA, -1)

        cursor.close()
        sqlDB.close()

        //새로운 일정 추가
        addScheduleBtn.setOnClickListener {
            var intent = Intent(this, AddScheduleActivity::class.java)
            intent.putExtra("animal_name", tvAnimalName.text)
            intent.putExtra(ANIMAL_ID_EXTRA, animalID)
            intent.putExtra("regNum", regNum)
            startActivity(intent)
        }

        //접종 리스트 표시
        showScheduleList()
        init()
    }

    //일정 리스트에 출력될 데이터 설정
    private fun showScheduleList(){
        sqlDB = myHelper.readableDatabase
        var cursor: Cursor

        var id: Int
        var strNames: String = ""
        var strRegNums: String = ""
        var strScheduleDate: String = ""
        var strScheduleContent: String = ""

        //선택한 반려동물의 DB에 있는 정보 가져옴
        cursor = sqlDB.rawQuery("SELECT id, animal_name, animal_reg_num, schedule_date," +
                " schedule_contents FROM animalScheduleTBL WHERE animal_reg_num=$regNum;", null)

        scheduleList.clear()
        //schedule 객체로 생성해서 리스트에 저장
        while(cursor.moveToNext()){
            id = cursor.getInt(0)
            strNames = cursor.getString(1)
            strRegNums = cursor.getString(2)
            strScheduleDate = cursor.getString(3)
            strScheduleContent = cursor.getString(4)

            scheduleList.add(Schedule(id, strNames, strRegNums, strScheduleDate, strScheduleContent))
        }
        cursor.close()
        sqlDB.close()
    }
    //어댑터 뷰 설정
    private fun init() {
        vManager = GridLayoutManager(this, 2)

        vAdpater = ScheduleAdapter(scheduleList, animalID, this)

        rView = findViewById<RecyclerView?>(R.id.inoculation_recyclerview).apply {
            setHasFixedSize(true)
            layoutManager = vManager
            adapter = vAdpater
        }
    }

    //뒤로가기 했을때 이전 데이터 유지를 위해
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home ->{
                val intent = Intent(this, AnimalDetailActivity::class.java)
                intent.putExtra(ANIMAL_ID_EXTRA, animalID)
                startActivity(intent)
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //일정 클릭 시 기능
    override fun onClick(schedule: Schedule) {
        Toast.makeText(applicationContext, "Click", Toast.LENGTH_SHORT).show()
    }

    //뒤로가기 눌렀을때 반려동물 프로필로 이동
    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, AnimalDetailActivity::class.java)
        intent.putExtra(ANIMAL_ID_EXTRA, animalID)
        startActivity(intent)
    }
}