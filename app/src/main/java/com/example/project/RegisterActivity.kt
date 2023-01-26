package com.example.project

import android.content.ContentValues.TAG
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        //회원가입 버튼 클릭
        register_button.setOnClickListener {
            var isExistBlank = false
            var isPwSame = false

            val id = username.text.toString()
            val pw = password.text.toString()
            val pw_check = password_check.text.toString()

            //유저가 항목을 다 채우지 않았을 경우
            //비밀번호와 비밀번호 확인이 일치하지 않을 경우
            if(id.isEmpty() || pw.isEmpty() || pw_check.isEmpty()){
                isExistBlank = true
            } else {
                if(pw == pw_check) {
                    isPwSame = true
                }
            }

            if(!isExistBlank && isPwSame){
                //회원가입 성공 토스트 메세지 띄우기
                Toast.makeText(this, "회원가입 성공", Toast.LENGTH_SHORT).show()

                //유저가 입력한 id, pw를 쉐어드에 저장
                val sharedPreferences = getSharedPreferences("user info", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putString("id", id)
                editor.putString("pw", pw)
                editor.apply()

                //로그인 화면으로 이동
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            } else {
                //상태에 따라 다른 다이얼로그 띄워주기
                if(isExistBlank){
                    dialog("blank")
                } else if(!isPwSame){
                    dialog("not same")
                }
            }
        }
    }

    private fun dialog(type: String) {
        val dialog = AlertDialog.Builder(this)

        //작성 안 한 항목이 있을 경우
        //입력한 비밀번호가 다를 경우
        if(type.equals("blank")){
            dialog.setTitle("회원가입 실패")
            dialog.setMessage("입력란을 모두 작성해주세요")
        } else if(type.equals("not same")) {
            dialog.setTitle("회원가입 실패")
            dialog.setMessage("비밀번호가 다릅니다")
        }

        val dialog_listner = object: DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int) {
                when(which){
                    DialogInterface.BUTTON_POSITIVE ->
                        Log.d(TAG, "다이얼로그")
                }
            }
        }
        dialog.setPositiveButton("확인", dialog_listner)
        dialog.show()
    }
}