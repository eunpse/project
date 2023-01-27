package com.example.myapplication

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    lateinit var btnMove: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnMove = findViewById<Button>(R.id.move_btn)

        btnMove.setOnClickListener {
            //저장소 권한 설정
            if(ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                getPermission()
            }
            else{
                var intent = Intent(this, SettingActivity::class.java)
                startActivity(intent)
            }
        }
    }

    //저장소 권한 얻기
    private fun getPermission(){
        //허용안함을 눌렀던 경우
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)){
            var dlg = AlertDialog.Builder(this)
            dlg.setTitle("권한이 필요한 이유")
            dlg.setMessage("사진 정보를 얻기 위해서는 외부 저장소 권한이 필수로 필요합니다")
            dlg.setPositiveButton("확인"){ _, _ ->
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1000)}
            dlg.setNegativeButton("취소", null)
            dlg.show()
        }
        //권한 부여
        else{
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1000)
        }
    }
}