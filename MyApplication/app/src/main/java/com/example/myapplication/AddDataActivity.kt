package com.example.myapplication

import android.Manifest
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import java.util.Calendar

class AddDataActivity : AppCompatActivity(), AnimalIDListener  {

    //DB
    lateinit var myHelper: MyDBHelper
    lateinit var sqlDB: SQLiteDatabase

    lateinit var edtName: EditText

    //img
    lateinit var preImg: ImageView
    lateinit var uploadImgBtn: Button
    //이미지 uri
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

    lateinit var edtRegNum: EditText
    lateinit var edtBreed: EditText
    lateinit var edtAge: EditText
    lateinit var edtBirth: EditText
    lateinit var btnBirth: Button
    lateinit var edtFeature: EditText

    lateinit var rGroup: RadioGroup
    lateinit var rbMale: RadioButton
    lateinit var rbFemale: RadioButton
    lateinit var chNeutral: CheckBox
    lateinit var btnInsertData: Button

    var animal_gender: String = ""
    var strGender: String = ""
    var animalID: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_data)

        edtName = findViewById<EditText>(R.id.edt_name)

        preImg = findViewById<ImageView>(R.id.preview_img)
        uploadImgBtn = findViewById<Button>(R.id.btn_upload_img)

        edtRegNum = findViewById<EditText>(R.id.edt_reg_num)
        edtBreed = findViewById<EditText>(R.id.edt_animal_breed)
        edtAge = findViewById<EditText>(R.id.edt_age)

        edtBirth = findViewById<EditText>(R.id.edt_birth)
        btnBirth = findViewById<Button>(R.id.btn_birth)

        edtFeature = findViewById<EditText>(R.id.edt_feature)

        rGroup = findViewById<RadioGroup>(R.id.radio_group_gender)
        rbMale = findViewById<RadioButton>(R.id.rb_male)
        rbFemale = findViewById<RadioButton>(R.id.rb_female)
        chNeutral = findViewById<CheckBox>(R.id.ch_neutral)

        btnInsertData = findViewById<Button>(R.id.btn_insert_new_data)

        myHelper = MyDBHelper(this)
        animalID = intent.getIntExtra(ANIMAL_ID_EXTRA, -1)

        //추가, 수정 액티비티 여부에 따라 액션바 설정
        if(animalID != -1){

            //액션바 설정
            val ac: ActionBar? = supportActionBar
            ac?.title = "반려동물 프로필 변경"

            setInfo()
        }
        else{
            //액션바 설정
            val ac: ActionBar? = supportActionBar
            ac?.title = "반려동물 추가"
        }

        //이미지 가져오기
        uploadImgBtn.setOnClickListener {
            var intent: Intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.type = "image/*"
            startForResult.launch(Intent.createChooser(intent, "Img"))
        }

        //성별 선택
        rGroup.setOnCheckedChangeListener{ _, checkedId ->
            when(checkedId) {
                R.id.rb_male -> animal_gender = "남자"
                R.id.rb_female -> animal_gender = "여자"
            }
        }

        //중성화 여부 설정
        chNeutral.setOnClickListener {
            if(chNeutral.isChecked){
                strGender = " (중성화)"
            }
            else{
                strGender = ""
            }
        }

        //날짜 설정
        btnBirth.setOnClickListener {
            var cal: Calendar = Calendar.getInstance()
            val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
                edtBirth.setText("${year}/${month+1}/${day}")
            }
            DatePickerDialog(this, dateSetListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()

        }

        //삽입
        btnInsertData.setOnClickListener{
            //공백 체크
            if(checkInput())
                return@setOnClickListener

            if(animalID != -1){
                myHelper.updateAnimalData(animalID.toString(), edtName.text.toString(), uri.toString(),
                    edtRegNum.text.toString(), animal_gender+strGender, edtBreed.text.toString(), edtAge.text.toString(),
                    edtBirth.text.toString(), edtFeature.text.toString())
                //반려동물 세부정보에 보여지는 화면 수정
                editInfo(animalID)
                //반려동물 세부정보 화면 이동
                var intent = Intent(this, AnimalDetailActivity::class.java)
                intent.putExtra(ANIMAL_ID_EXTRA, animalID)
                startActivity(intent)
            }
            else{
                //데이터 삽입
                myHelper.insertAnimalInfoData(edtName.text.toString(), uri.toString(),
                    edtRegNum.text.toString(), animal_gender+strGender, edtBreed.text.toString(), edtAge.text.toString(),
                    edtBirth.text.toString(), edtFeature.text.toString())

                //setting으로 화면 이동
                var intent = Intent(this, SettingActivity::class.java)
                intent.putExtra(ANIMAL_ID_EXTRA, animalID)
                startActivity(intent)
            }
        }
    }

    private fun setInfo() {
        val animal = animalFromID(animalID)

        //기존 텍스트로 설정
        if(animal != null){
            //이미지 설정
            if(animal.animal_img.toString() != "") {
                uri = animal.animal_img
                preImg.setImageURI(uri)
            }
            else
                preImg.setImageResource(R.mipmap.ic_launcher_default_animal)

            edtName.setText(animal.animal_name)
            edtRegNum.setText(animal.animal_reg_num)
            edtBreed.setText(animal.animal_breed)
            edtAge.setText(animal.animal_age)
            edtBirth.setText(animal.animal_birth)
            edtFeature.setText(animal.animal_feature)

            animal_gender = animal.animal_gender

            var gender = animal_gender.split(" ")

            when(gender[0]){
                "남자" -> rGroup.check(R.id.rb_male)
                "여자" -> rGroup.check(R.id.rb_female)
            }

            if(gender.size != 1){
                if(gender[1] == "(중성화)"){
                    chNeutral.isChecked = true
                }
            }

        }
    }

    //공백 체크
    private fun checkInput() : Boolean {
        if(TextUtils.isEmpty(edtName.text.toString())){
            Toast.makeText(baseContext, "반려동물의 이름을 입력해주세요", Toast.LENGTH_SHORT).show()
            return true
        }
        if(TextUtils.isEmpty(edtRegNum.text.toString())){
            Toast.makeText(baseContext, "반려동물의 등록번호를 입력해주세요", Toast.LENGTH_SHORT).show()
            return true
        }
        if(edtRegNum.length() < 12){
            Toast.makeText(baseContext, "잘못된 입력입니다.\n등록번호 12자리를 입력해주세요", Toast.LENGTH_SHORT).show()
            return true
        }
        if(!rbFemale.isChecked && !rbMale.isChecked){
            Toast.makeText(baseContext, "반려동물의 성별을 입력해주세요", Toast.LENGTH_SHORT).show()
            return true
        }
        if(TextUtils.isEmpty(edtBreed.text.toString())){
            Toast.makeText(baseContext, "반려동물의 품종을 입력해주세요", Toast.LENGTH_SHORT).show()
            return true
        }
        if(TextUtils.isEmpty(edtAge.text.toString())){
            Toast.makeText(baseContext, "반려동물의 나이를 입력해주세요", Toast.LENGTH_SHORT).show()
            return true
        }
        if(TextUtils.isEmpty(edtBirth.text.toString())){
            Toast.makeText(baseContext, "반려동물의 생일을 입력해주세요", Toast.LENGTH_SHORT).show()
            return true
        }
        if(TextUtils.isEmpty(edtFeature.text.toString())){
            Toast.makeText(baseContext, "반려동물의 특징을 입력해주세요", Toast.LENGTH_SHORT).show()
            return true
        }

        return false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home ->{
                //수정인 경우 반려동물 프로필로 이동
                if(animalID != -1){
                    val intent = Intent(this, AnimalDetailActivity::class.java)
                    intent.putExtra(ANIMAL_ID_EXTRA, animalID)
                    startActivity(intent)
                }
                //추가인 경우 반려동물 리스트로 이동
                else{
                    val intent = Intent(this, SettingActivity::class.java)
                    startActivity(intent)
                }
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //반려동물 프로필 변화 내용 반영
    private fun editInfo(id_num: Int) {
        sqlDB = myHelper.readableDatabase
        var cursor: Cursor

        var id: Int = id_num
        var strNames: String = ""
        var strImgs: String = ""
        var strRegNums: String = ""
        var strGender: String = ""
        var strBreed: String = ""
        var strAge: String = ""
        var strBirth: String = ""
        var strFeature: String = ""

        //DB에 있는 정보 가져옴
        cursor = sqlDB.rawQuery("SELECT id, animal_name, animal_img, animal_reg_num, animal_gender," +
                " animal_breed, animal_age, animal_birth, animal_feature FROM animalTBL;", null)

        animalList.clear()
        //animal 객체로 생성해서 리스트에 저장
        while(cursor.moveToNext()){
            //create animal instance
            id = cursor.getInt(0)
            strNames = cursor.getString(1)
            strImgs = cursor.getString(2)
            strRegNums = cursor.getString(3)
            strGender = cursor.getString(4)
            strBreed = cursor.getString(5)
            strAge = cursor.getString(6)
            strBirth = cursor.getString(7)
            strFeature = cursor.getString(8)

            animalList.add(Animal(id, strNames, Uri.parse(strImgs), strRegNums, strBreed, strAge, strGender, strBirth, strFeature))
        }
        cursor.close()
        sqlDB.close()
    }
}