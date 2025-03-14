package com.example.ecommerceappcompose.user

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore.Images.Media
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.ecommerceappcompose.R
import com.example.ecommerceappcompose.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AccountScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        TextButton(onClick = { navController.navigate("AddProduct") }) {
                            Text("Mulai Jual", color = Color.Black, fontSize = 20.sp)
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {navController.navigate("AddProduct")}) {
                        Icon(Icons.Filled.AccountCircle, null)
                    }
                },
                modifier = Modifier.background(Color.Green),
                actions = {
                    IconButton(onClick = {navController.navigate("AddShop")}) {
                        Icon(Icons.Filled.Settings, null)
                    }
                    IconButton(onClick = {}) {
                        Icon(Icons.Filled.ShoppingCart, null)
                    }
                    IconButton(onClick = {}) {
                        Icon(Icons.Filled.Email, null)
                    }
                },
            )
        },
        content = {
            val context = LocalContext.current
            var uri by remember { mutableStateOf<Uri?>(null) }
            var bitmap by remember { mutableStateOf<Bitmap?>(null) }

            // Activity Result Launcher to select an image from the gallery
            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.GetContent()
            ) { selectedUri ->
                selectedUri?.let {
                    uri = it
                    bitmap = if (Build.VERSION.SDK_INT < 28) {
                        Media.getBitmap(context.contentResolver, it)
                    } else {
                        val source = ImageDecoder.createSource(context.contentResolver, it)
                        ImageDecoder.decodeBitmap(source)
                    }
                }
            }

            Column(
                modifier = Modifier.padding(start = 20.dp, top = 100.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row {
                    if (bitmap != null) {
                        // Jika bitmap sudah ada (gambar dipilih), tampilkan gambar dari bitmap
                        Image(
                            bitmap = bitmap!!.asImageBitmap(),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(75.dp)
                                .aspectRatio(1f)
                                .clip(CircleShape)
                                .border(2.dp, Color.Gray)
                                .clickable {
                                    launcher.launch("image/*")
                                }
                        )
                    } else {
                        // Jika bitmap masih null, tampilkan placeholder dari drawable
                        Image(
                            painter = painterResource(id = R.drawable.img),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(75.dp)
                                .aspectRatio(1f)
                                .clip(CircleShape)
                                .border(2.dp, Color.Gray)
                                .clickable {
                                    launcher.launch("image/*")
                                }
                        )
                    }

                    Column(horizontalAlignment = Alignment.Start) {
                        Text("Tento", modifier = Modifier.padding(start = 20.dp))
                        Row(
                            modifier = Modifier.padding(start = 20.dp),
                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            Text("0 Pengikut")
                            Text("0 Mengikuti")
                        }
                    }
                }
            }
        }
    )
}

