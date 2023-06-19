package com.example.cafe.datas
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class DetailData @JvmOverloads constructor(
    var hardness : Double?=null,
    var latitude : Double?=null,
)