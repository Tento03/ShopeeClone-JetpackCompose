package com.example.ecommerceappcompose

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ecommerceappcompose.auth.LoginScreen
import com.example.ecommerceappcompose.auth.RegisterScreen
import com.example.ecommerceappcompose.model.Route
import com.example.ecommerceappcompose.ui.theme.EcommerceAppComposeTheme
import com.example.ecommerceappcompose.user.AccountScreen
import com.example.ecommerceappcompose.user.AddProductScreen
import com.example.ecommerceappcompose.user.AddShopScreen
import com.example.ecommerceappcompose.user.BottomNavigation
import com.example.ecommerceappcompose.user.TrendingScreen
import com.example.ecommerceappcompose.user.UserHomeScreen
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EcommerceAppComposeTheme {
                val context= LocalContext.current
                val isLogin= isLogin(context)
                val navController= rememberNavController()
                FirebaseApp.initializeApp(context)

                Scaffold(
                    bottomBar = { BottomNavigation(navController) }) {
                    NavHost(navController, startDestination = if (isLogin) "Home" else "Login", modifier = Modifier.padding(it)
                    ){
                        composable(Route.Home.screen){
                            UserHomeScreen()
                        }
                        composable("SellerHome"){

                        }
                        composable("Login"){
                            LoginScreen(navController)
                        }
                        composable("Register"){
                            RegisterScreen(navController)
                        }
                        composable(Route.Trend.screen){
                            TrendingScreen(navController)
                        }
                        composable(Route.Account.screen){
                            AccountScreen(navController)
                        }
                        composable("AddShop"){
                            AddShopScreen(navController)
                        }
                        composable("AddProduct"){
                            AddProductScreen(navController)
                        }
                    }
                }
            }
        }
    }
}

fun isLogin(context: Context):Boolean{
    val sharedPreferences=context.getSharedPreferences("Login_Pref",Context.MODE_PRIVATE)
    return sharedPreferences.getBoolean("isLogin",false)
}


