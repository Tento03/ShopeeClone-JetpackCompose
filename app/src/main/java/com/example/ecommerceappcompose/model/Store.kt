package com.example.ecommerceappcompose.model

import android.graphics.Bitmap

data class Store(val id:String, val storeName:String, val description:String,
                 val image:Bitmap?){
    constructor():this("","","", null)
}
