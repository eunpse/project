package com.example.myapplication

import android.net.Uri

var animalList = ArrayList<Animal>()

//인텐트 전달용
val ANIMAL_ID_EXTRA = "animalExtra"

class Animal (
    var id: Int,
    var animal_name: String,
    var animal_img: Uri,
    var animal_reg_num: String,
    var animal_breed: String,
    var animal_age: String,
    var animal_gender: String,
    var animal_birth: String,
    var animal_feature: String
)