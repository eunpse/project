package com.example.myapplication

import android.net.Uri

var scheduleList = ArrayList<Schedule>()

//인텐트 전달용
val SCHEDULE_ID_EXTRA = "scheduleExtra"

class Schedule(
    var id: Int,
    var animal_name: String,
    var animal_reg_num: String,
    var schedule_date: String,
    var schedule_content: String
)