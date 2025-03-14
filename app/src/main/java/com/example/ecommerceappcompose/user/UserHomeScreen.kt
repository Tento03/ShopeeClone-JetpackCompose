package com.example.ecommerceappcompose.user

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecommerceappcompose.R
import com.example.ecommerceappcompose.model.Shopee

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun UserHomeScreen(modifier: Modifier = Modifier) {
    val shopeeList = listOf(
        Shopee("Pulsa", R.drawable.pulsa),
        Shopee("Food", R.drawable.shopee_food),
        Shopee("Vip", R.drawable.shopee_vip),
        Shopee("Live", R.drawable.shopee_live),
        Shopee("Later", R.drawable.shopee_later),
    )
    var query by remember { mutableStateOf("") }
    val keyboardController= LocalSoftwareKeyboardController.current

    Scaffold(
        topBar = {
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
                        placeholder = { Text("Search Here") }
                    )
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Filled.ShoppingCart,null)
                    }
                    IconButton(onClick = {}) {
                        Icon(Icons.Filled.Email,null)
                    }
                },
            )
        },
        content = {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(16.dp, top = 100.dp)
            ) {
                LazyRow(
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                ) {
                    items(shopeeList) { shopee ->
                        LazyRowHomeCard(shopee)
                    }
                }

                LazyColumn(modifier.fillMaxWidth().padding(16.dp),
                    horizontalAlignment =  Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                    items(shopeeList.chunked(2)){ rowItems->
                        Row(horizontalArrangement = Arrangement.SpaceBetween) {
                            rowItems.forEach { shope->
                                LazyColumnItemCard(shope)
                            }
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun LazyRowHomeCard(shopee: Shopee, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(20.dp),
        colors = CardDefaults.cardColors(Color.Green)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(shopee.icon),
                contentScale = ContentScale.FillBounds,
                contentDescription = null,
                modifier = Modifier.size(80.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = shopee.label,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun LazyColumnItemCard(shopee: Shopee, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .padding(8.dp)
        ,
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(20.dp),
        colors = CardDefaults.cardColors(Color.Green)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(shopee.icon),
                contentScale = ContentScale.FillBounds,
                contentDescription = null,
                modifier = Modifier.size(100.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = shopee.label,
                textAlign = TextAlign.Center
            )
        }
    }
}
