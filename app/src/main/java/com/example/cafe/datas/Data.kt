package com.example.cafe.datas

import com.google.firebase.database.IgnoreExtraProperties


// DB를 불러오기위한 데이터클래스
@IgnoreExtraProperties
data class Data@JvmOverloads constructor(
    //var hardness : Double?=null,
   // var latitude : Double?=null,
    var newAddress : String?=null,
    //var oldAddress : String?=null,
    var storeName : String?=null
)
