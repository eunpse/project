package com.example.myapplication

import android.app.DatePickerDialog
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBar
import java.util.*

class AddInoculationActivity : AppCompatActivity(), AnimalIDListener {
    //DB
    lateinit var myHelper: MyDBHelper

    lateinit var tvAnimalName: TextView
    lateinit var tvTitle: TextView

    //이미지
    lateinit var preImg: ImageView
    lateinit var btnImg: Button
    var uri: Uri = Uri.parse("")
    //이미지 가져오기
    val startForResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
            result: ActivityResult ->
        if(result.resultCode == RESULT_OK){
            uri = result.data?.data!!
            //uri 권한 유지
            var takeFlag = Intent.FLAG_GRANT_READ_URI_PERMISSION or
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            contentResolver.takePersistableUriPermission(uri, takeFlag)
            //미리보기 사진 설정
            preImg.setImageURI(uri)
        }
    }

    lateinit var edInoculationDate : EditText
    lateinit var edInoculationContent: EditText

    lateinit var btnInsertData: Button
    lateinit var btnInoculationDate: Button

    lateinit var regNum: String
    var animalID: Int = -1
    var inoculationID: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_inoculation)

        tvAnimalName = findViewById<TextView>(R.id.animal_name)
        tvTitle = findViewById<TextView>(R.id.title)

        preImg = findViewById<ImageView>(R.id.pre_img)
        btnImg = findViewById<Button>(R.id.load_img_btn)

        edInoculationDate = findViewById<EditText>(R.id.edt_inoculation_date)
        edInoculationContent = findViewById<EditText>(R.id.edt_inoculation_content)

        btnInsertData = findViewById<Button>(R.id.btn_insert_new_data)
        btnInoculationDate = findViewById(R.id.btn_inoculation_date)

        myHelper = MyDBHelper(this)

        animalID = intent.getIntExtra(ANIMAL_ID_EXTRA, -1)
        tvAnimalName.text = intent.getStringExtra("animal_name")
        regNum = intent.getStringExtra("regNum").toString()
        inoculationID = intent.getIntExtra(INOCULATION_ID_EXTRA, -1)

        //추가, 수정 액티비티 여부에 따라 액션바 설정
        if(inoculationID != -1){
            setInfo()
            tvTitle.text = "접종 내역 변경"
            //액션바 설정
            val ac: ActionBar? = supportActionBar
            ac?.title = "접종 내역 변경"
        }
        else{
            tvTitle.text = "접종 내역 추가"
            //액션바 설정
            val ac: ActionBar? = supportActionBar
            ac?.title = "접종 내역 추가"
        }

        //이미지 업로드
        btnImg.setOnClickListener {
            var intent: Intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.type = "image/*"
            startForResult.launch(Intent.createChooser(intent, "Img"))
        }

        //날짜 선택
        btnInoculationDate.setOnClickListener {
            var cal: Calendar = Calendar.getInstance()
            val dateSetListener = DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->
                edInoculationDate.setText("${year}/${month+1}/${day}")
            }
            DatePickerDialog(this, dateSetListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(
                Calendar.DAY_OF_MONTH)).show()

        }

        //DB 삽입
        btnInsertData.setOnClickListener{
            //공백 체크
            if(checkInput())
                return@setOnClickListener


            if(inoculationID != -1){
                //데이터 수정
                myHelper.updateInoculationData(inoculationID.toString(), tvAnimalName.text.toString(),
                    regNum, uri.toString(), edInoculationDate.text.toString(), edInoculationContent.text.toString())
            }
            else{
                //데이터 삽입
                myHelper.insertInoculationData(tvAnimalName.text.toString(),
                    regNum, uri.toString(),
                    edInoculationDate.text.toString(), edInoculationContent.text.toString())
            }

            animalID = intent.getIntExtra(ANIMAL_ID_EXTRA, -1)
            //접종 내역 리스트로 화면 이동
            var intent = Intent(this, InoculationHistoryActivity::class.java)
            intent.putExtra("animal_name",tvAnimalName.text)
            intent.putExtra(ANIMAL_ID_EXTRA, animalID)
            intent.putExtra("animal_reg_num", regNum)
            startActivity(intent)
        }
    }

    //기존 텍스트 설정
    private fun setInfo() {
        val inoculation = inoculationFromID(inoculationID)

        if (inoculation != null) {
            if(inoculation.animal_img.toString() != "") {
                uri = inoculation.animal_img
                preImg.setImageURI(uri)
            }
            else
                preImg.setImageResource(R.mipmap.ic_launcher_default_animal)
        }

        if (inoculation != null) {
            edInoculationDate.setText(inoculation.inoculation_date)
        }
        if (inoculation != null) {
            edInoculationContent.setText(inoculation.inoculation_content)
        }
    }

    //접종 내역 리스트 액티비티로 이동
    override fun onOptionsItemSelected(item: MenuItem): Boolean{
        when(item.itemId){
            android.R.id.home ->{
                animalID = intent.getIntExtra(ANIMAL_ID_EXTRA, -1)
                val intent = Intent(this, InoculationHistoryActivity::class.java)
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
        if(TextUtils.isEmpty(edInoculationDate.text.toString())){
            Toast.makeText(baseContext, "접종 날짜를 입력해주세요", Toast.LENGTH_SHORT).show()
            return true
        }
        if(TextUtils.isEmpty(edInoculationContent.text.toString())){
            Toast.makeText(baseContext, "접종 내용을 적어주세요", Toast.LENGTH_SHORT).show()
            return true
        }

        return false
    }
}