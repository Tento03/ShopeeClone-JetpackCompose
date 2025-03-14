package com.example.ecommerceappcompose.user

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.ecommerceappcompose.R
import com.example.ecommerceappcompose.model.Shopee

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TrendingScreen(navController: NavController,modifier: Modifier=Modifier) {
    var query by remember { mutableStateOf("") }
    val keyboardController= LocalSoftwareKeyboardController.current
    var isSearching by remember { mutableStateOf(false) }

    val shopeeList = listOf(
        Shopee("Pulsa", R.drawable.pulsa),
        Shopee("Food", R.drawable.shopee_food),
        Shopee("Vip", R.drawable.shopee_vip),
        Shopee("Live", R.drawable.shopee_live),
        Shopee("Later", R.drawable.shopee_later),
    )
    
    Scaffold(
        topBar = {
            if (isSearching){
                TopAppBar(
                    title = {
                        OutlinedTextField(
                            value = query,
                            onValueChange = {
                                query=it
                            },
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Search
                            ),
                            keyboardActions = KeyboardActions(
                                onSearch = {
                                    keyboardController?.hide()
                                }
                            ),
                            leadingIcon = { Icon(Icons.Filled.Search,null) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    },
                    actions = {
                        IconButton(onClick = {isSearching=false}) {
                            Icon(Icons.Filled.Close,null)
                        }
                    },
                )
            }
            else{
                TopAppBar(
                    title = {
                        Text("Trending", textAlign = TextAlign.Left, fontWeight = FontWeight.Bold, fontSize = 30.sp)
                    },
                    actions = {
                        IconButton(onClick = {isSearching=true}) {
                            Icon(Icons.Filled.Search,null)
                        }
                        IconButton(onClick = {}) {
                            Icon(Icons.Filled.ShoppingCart,null)
                        }
                    },
                )
            }
        },
        content = {
            LazyColumn(modifier.padding(it).fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                items(shopeeList.chunked(2)){rowItem->
                    Row(horizontalArrangement = Arrangement.SpaceBetween) {
                        rowItem.forEach {
                            TrendingCard(it)
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun TrendingCard(shopee: Shopee,modifier: Modifier = Modifier) {
    Card(modifier.padding(8.dp).size(width = 100.dp, height = 150.dp),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(20.dp),
    ) {
        Column(modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {
            Image(
                painter = painterResource(shopee.icon),
                contentDescription = null,
                contentScale = ContentScale.FillBounds
            )
            Spacer(modifier.height(8.dp))
            Text(shopee.label, textAlign = TextAlign.Center)
        }
    }
}