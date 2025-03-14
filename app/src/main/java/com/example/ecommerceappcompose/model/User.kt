package com.example.ecommerceappcompose.model

import android.graphics.Bitmap

data class User(val id:String, val username:String, val email:String, val password:String, val gender:String,
                val image:Bitmap?){
    constructor():this("","","","","", null)
}
