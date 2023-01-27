package com.example.myapplication

import android.net.Uri

var inoculationList = ArrayList<Inoculation>()

//인텐트 전달용
val INOCULATION_ID_EXTRA = "inoculationExtra"

class Inoculation(
    var id: Int,
    var animal_name: String,
    var animal_reg_num: String,
    var animal_img: Uri,
    var inoculation_date: String,
    var inoculation_content: String
)