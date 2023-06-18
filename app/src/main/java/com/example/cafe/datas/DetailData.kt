package com.example.cafe.datas
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class DetailData @JvmOverloads constructor(
    var hardness : Double?=null,
    var latitude : Double?=null,
    var newAddress : String?=null,
    var oldAddress : String?=null,
    var storeName : String?=null
)