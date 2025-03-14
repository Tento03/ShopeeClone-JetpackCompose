package com.example.ecommerceappcompose.user

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore.Images.Media
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.ecommerceappcompose.R
import com.example.ecommerceappcompose.model.Product
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductScreen(navController: NavHostController) {
    var productName by remember { mutableStateOf("") }
    var productDescription by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var category by remember { mutableStateOf("") }
    var categoryList = listOf("Education","Religion","Beauty","Sports")
    var hasErrorProductName by remember { mutableStateOf(false) }
    var hasErrorProductDescription by remember { mutableStateOf(false) }
    var hasErrorCategory by remember { mutableStateOf(false) }
    var focusRequesterProductName by remember { mutableStateOf(FocusRequester()) }
    var focusRequesterProductDescription by remember { mutableStateOf(FocusRequester()) }
    var focusRequesterCategory by remember { mutableStateOf(FocusRequester()) }
    val context= LocalContext.current
    val keyboardController= LocalSoftwareKeyboardController.current
    var uri by remember { mutableStateOf<Uri?>(null) }
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) {selectedUri->
        selectedUri?.let {
            uri=it

            if (Build.VERSION.SDK_INT < 28){
                bitmap=Media.getBitmap(context.contentResolver,it)
            }
            else{
                val source=ImageDecoder.createSource(context.contentResolver,it)
                bitmap=ImageDecoder.decodeBitmap(source)
            }
        }
    }

    Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(
            value = productName,
            onValueChange = {
                productName=it
                hasErrorProductName=false
            },
            modifier = Modifier
                .focusRequester(focusRequesterProductName)
                .fillMaxWidth(),
            isError = hasErrorProductName,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
            label = { Text("Product Name") }
        )
        OutlinedTextField(
            value = productDescription,
            onValueChange = {
                productDescription=it
                hasErrorProductDescription=false
            },
            modifier = Modifier
                .focusRequester(focusRequesterProductDescription)
                .fillMaxWidth(),
            isError = hasErrorProductDescription,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                }
            ),
            placeholder = { Text("Description Product") }
        )
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded=!expanded
            },
            modifier = Modifier.focusRequester(focusRequesterCategory)
        ) {
            OutlinedTextField(
                value = category,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
                    .focusRequester(focusRequesterCategory),
                trailingIcon = {ExposedDropdownMenuDefaults.TrailingIcon(expanded)},
                placeholder = { Text("Category") }
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    expanded=false
                },
            ) {
                categoryList.forEach {
                    DropdownMenuItem(
                        text = { Text(it) },
                        onClick = {
                            category=it
                            expanded=false
                        },
                    )
                }
            }
        }
        if (bitmap!=null){
            Image(
                bitmap!!.asImageBitmap(),null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(75.dp).aspectRatio(1f).clip(shape = RectangleShape).border(2.dp,Color.Gray)
                    .clickable { launcher.launch("image/*")  }
            )
        }
        else{
            Image(
                painter = painterResource(R.drawable.img),null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(75.dp).aspectRatio(1f).clip(shape = RectangleShape).border(2.dp,Color.Gray)
                    .clickable { launcher.launch("image/*") }
            )
        }
        Button(onClick = {
            val firebaseAuth=FirebaseAuth.getInstance()
            val uid=firebaseAuth.currentUser?.uid
            val firebaseDatabase=FirebaseDatabase.getInstance().getReference("product")
            if (uid != null) {
                val product=Product(uid,productName,productDescription,bitmap.toString())
                firebaseDatabase.child(uid).setValue(product).addOnCompleteListener{
                    if (it.isSuccessful){
                        Toast.makeText(context,"Success",Toast.LENGTH_SHORT).show()
                        navController.navigate("Home")
                    }
                    else{
                        Toast.makeText(context,"Failed",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(Color.Green)) {
            Text("Submit", color = Color.Black, textAlign = TextAlign.Center)
        }
    }
}