package com.example.ecommerceappcompose.model

import android.graphics.Bitmap

data class Product(val id:String,val productName:String, val productDescription:String,
                   val image:String){
    constructor():this("","","", "")
}
