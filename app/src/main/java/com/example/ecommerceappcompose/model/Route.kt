package com.example.ecommerceappcompose.model

sealed class Route(val screen:String) {
    data object Home:Route("Home")
    data object Trend:Route("Trend")
    data object Account:Route("Account")
}
