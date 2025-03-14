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
    val firebaseAuth = FirebaseAuth.getInstance()
    val firebaseDatabase = FirebaseDatabase.getInstance().getReference("user")
    val firebaseStorage = FirebaseStorage.getInstance().reference.child("profile_images")
    val uid = firebaseAuth.currentUser?.uid

    var uri by remember { mutableStateOf<Uri?>(null) }
    var imageUrl by remember { mutableStateOf<String?>(null) }
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }

    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { selectedUri ->
        selectedUri?.let {
            uri = it
            if (Build.VERSION.SDK_INT < 28) {
                bitmap = Media.getBitmap(context.contentResolver, it)
            } else {
                val source = ImageDecoder.createSource(context.contentResolver, it)
                bitmap = ImageDecoder.decodeBitmap(source)
            }

            // Simpan gambar ke Firebase Storage
            uploadImageToStorage(bitmap, firebaseStorage, uid, firebaseDatabase)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        TextButton(onClick = { navController.navigate("AddShop") }) {
                            Text("Mulai Jual", color = Color.Black, fontSize = 20.sp)
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Filled.ShoppingCart, null)
                    }
                },
                modifier = Modifier.background(Color.Green),
                actions = {
                    IconButton(onClick = {}) {
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
            Column(
                modifier = Modifier.padding(start = 20.dp, top = 100.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row {
                    if (uid != null) {
                        firebaseDatabase.child(uid).addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                if (snapshot.exists()) {
                                    val user = snapshot.getValue(User::class.java)
                                    imageUrl = user?.image.toString()
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                // Handle error
                            }
                        })
                    }

                    if (imageUrl != null) {
                        AsyncImage(
                            model = imageUrl,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.size(75.dp).aspectRatio(1f).clip(CircleShape).border(2.dp, Color.Gray)
                                .clickable {
                                    launcher.launch("image/*")
                                }
                        )
                    } else {
                        Image(
                            painter = painterResource(R.drawable.img), null,
                            contentScale = ContentScale.FillBounds,
                            modifier = Modifier.size(75.dp).aspectRatio(1f).clip(CircleShape).border(2.dp, Color.Gray)
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

fun uploadImageToStorage(
    bitmap: Bitmap?,
    storageReference: StorageReference,
    uid: String?,
    databaseReference: DatabaseReference
) {
    if (bitmap == null || uid == null) return

    val baos = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
    val data = baos.toByteArray()

    val imageRef = storageReference.child("$uid.jpg")
    val uploadTask = imageRef.putBytes(data)
    uploadTask.addOnSuccessListener {
        imageRef.downloadUrl.addOnSuccessListener { uri ->
            databaseReference.child(uid).child("image").setValue(uri.toString())
        }
    }
}
