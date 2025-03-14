package com.example.ecommerceappcompose.user

import android.graphics.Bitmap
import android.graphics.ImageDecoder
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import android.net.Uri
import android.os.Build
import android.provider.MediaStore.Images.Media
import com.example.ecommerceappcompose.R
import com.example.ecommerceappcompose.model.Store
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

@Composable
fun AddShopScreen(navController: NavController,modifier: Modifier = Modifier) {
    var storeName by remember { mutableStateOf("") }
    var storeImage by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var hasErrorStorename by remember { mutableStateOf(false) }
    var hasErrorDescription by remember { mutableStateOf(false) }
    var focusRequesterStoreName by remember { mutableStateOf(FocusRequester()) }
    var focusRequesterDescrption by remember { mutableStateOf(FocusRequester()) }
    val context= LocalContext.current
    val keyboardController= LocalSoftwareKeyboardController.current
    var uri by remember { mutableStateOf<Uri?>(null) }
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    val launcher= rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ){selectedUri->
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

    Column(modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        if (bitmap!=null){
            Image(
                bitmap!!.asImageBitmap(),
                null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(75.dp).aspectRatio(1f).clip(CircleShape).border(2.dp,Color.Gray)
                    .clickable {
                        launcher.launch("image/*")
                    }
            )
        }
        else{
            Image(
                painter = painterResource(R.drawable.img),
                null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(75.dp).aspectRatio(1f).clip(CircleShape).border(2.dp,Color.Gray)
                    .clickable {
                        launcher.launch("image/*")
                    }
            )
        }

        OutlinedTextField(
            value = storeName,
            onValueChange = {
                storeName=it
            },
            modifier
                .focusRequester(focusRequesterStoreName)
                .padding(start = 20.dp, end = 20.dp),
            isError = hasErrorStorename,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
            label = { Text("Store Name") },
            leadingIcon = { Icon(Icons.Filled.AccountBox,null) }
        )
        OutlinedTextField(
            value = description,
            onValueChange = {
                description=it
            },
            modifier
                .focusRequester(focusRequesterDescrption)
                .padding(start = 20.dp, end = 20.dp),
            isError = hasErrorDescription,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                }
            ),
            label = { Text("Description") },
            leadingIcon = { Icon(Icons.Filled.AccountBox,null) }
        )
        Button(onClick = {
            if (storeName.isEmpty()){
                hasErrorStorename=true
            }
            if (description.isEmpty()){
                hasErrorDescription=true
            }
            else if (storeName.isNotEmpty() && description.isNotEmpty()){
                val firebaseDatabase=FirebaseDatabase.getInstance().getReference("shop")
                val firebaseAuth=FirebaseAuth.getInstance()
                val uid=firebaseAuth.currentUser?.uid
                val store= uid?.let { Store(it,storeName,description,image = null) }
                if (uid != null) {
                    firebaseDatabase.child(uid).setValue(store).addOnCompleteListener{
                        if (it.isSuccessful) {
                            Toast.makeText(context,"Sucess",Toast.LENGTH_SHORT).show()
                            navController.navigate("Home")
                        }
                        else{
                            Toast.makeText(context,"Failed",Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        },modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(Color.Green)) {
            Text("Submit", color = Color.Black, textAlign = TextAlign.Center)
        }
        if (hasErrorStorename){
            LaunchedEffect(Unit) {
                focusRequesterStoreName.requestFocus()
                Toast.makeText(context,"Please fill this field",Toast.LENGTH_SHORT).show()
            }
        }
        else if (hasErrorDescription){
            LaunchedEffect(Unit) {
                focusRequesterDescrption.requestFocus()
                Toast.makeText(context,"Please fill this field",Toast.LENGTH_SHORT).show()
            }
        }
    }
}