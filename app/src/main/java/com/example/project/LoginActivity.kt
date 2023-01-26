package com.example.project

import android.content.ContentValues.TAG
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //로그인 버튼 클릭
        login_button.setOnClickListener{
            //editText로부터 입력된 값 가져오기
            var id = username.text.toString()
            var pw = password.text.toString()

            //쉐어드로부터 저장된 id, pw 가져오기
            val sharedPreferences = getSharedPreferences("user info", Context.MODE_PRIVATE)
            val savedID = sharedPreferences.getString("id", "")
            val savedPW = sharedPreferences.getString("pw","")

            //유저가 입력한 id, pw 값과 쉐어드로 불러온 id, pw값 비교
            if(id == savedID && pw == savedPW) {
                //로그인 성공 다이얼로그
                dialog("success")

                //main 화면으로 이동
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else {
                //로그인 실패 다이얼로그
                dialog("fail")
            }
        }

        //회원가입 버튼 클릭
        register_button.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    //다이얼로그 메소드
    private fun dialog(type: String) {
        var dialog = AlertDialog.Builder(this)

        if(type.equals("success")){
            dialog.setTitle("로그인 성공")
            dialog.setMessage("로그인 성공!")
        } else if(type.equals("fail")) {
            dialog.setTitle("로그인 실패")
            dialog.setMessage("아이디와 비밀번호를 확인해주세요")
        }

        var dialog_listner = object: DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int) {
                when(which){
                    DialogInterface.BUTTON_POSITIVE ->
                        Log.d(TAG,"")
                }
            }
        }

        dialog.setPositiveButton("확인", dialog_listner)
        dialog.show()
    }
}