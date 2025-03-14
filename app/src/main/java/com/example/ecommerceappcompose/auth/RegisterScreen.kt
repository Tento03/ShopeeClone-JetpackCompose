@file:Suppress("UNREACHABLE_CODE")
package com.example.ecommerceappcompose.auth

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ecommerceappcompose.R
import com.example.ecommerceappcompose.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.security.MessageDigest

@Suppress("UNREACHABLE_CODE")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val focusRequesterUsername = remember { FocusRequester() }
    val focusRequesterEmail = remember { FocusRequester() }
    val focusRequesterPassword = remember { FocusRequester() }
    val focusRequesterGender = remember { FocusRequester() }
    var hasErrorUsername by remember { mutableStateOf(false) }
    var hasErrorEmail by remember { mutableStateOf(false) }
    var hasErrorPassword by remember { mutableStateOf(false) }
    var hasErrorGender by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    var expanded by remember { mutableStateOf(false) }
    var gender by remember { mutableStateOf("") }
    val listItem = listOf("Male", "Female")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .paint(painter = painterResource(R.drawable.img), contentScale = ContentScale.FillBounds),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .paint(
                    painter = painterResource(R.drawable.img),
                    contentScale = ContentScale.FillBounds
                )
                .padding(start = 20.dp, end = 20.dp)
                .height(200.dp)
                .width(280.dp)
        )
        Card(
            modifier = Modifier
                .padding(start = 20.dp, end = 20.dp)
                .paint(painter = painterResource(R.drawable.img_1), contentScale = ContentScale.FillBounds)
                .height(500.dp)
                .width(280.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Text("Register Page", fontWeight = FontWeight.Bold)
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it; hasErrorUsername = false },
                    isError = hasErrorUsername,
                    modifier = Modifier
                        .focusRequester(focusRequesterUsername)
                        .padding(top = 20.dp, start = 10.dp, end = 10.dp),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    label = { Text("Username") }
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it; hasErrorEmail = false },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    label = { Text("Email") },
                    isError = hasErrorEmail,
                    modifier = Modifier
                        .focusRequester(focusRequesterEmail)
                        .padding(top = 20.dp, start = 10.dp, end = 10.dp)
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it; hasErrorPassword = false },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
                    label = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    isError = hasErrorPassword,
                    modifier = Modifier
                        .focusRequester(focusRequesterPassword)
                        .padding(top = 20.dp, start = 10.dp, end = 10.dp)
                )

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded },
                    modifier = Modifier.focusRequester(focusRequesterGender)
                ) {
                    OutlinedTextField(
                        value = gender,
                        onValueChange = {},
                        readOnly = true,
                        placeholder = { Text("Gender") },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                            .padding(top = 20.dp, start = 10.dp, end = 10.dp),
                        isError = hasErrorGender,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) }
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                    ) {
                        listItem.forEach {
                            DropdownMenuItem(
                                text = { Text(it) },
                                onClick = {
                                    gender = it
                                    expanded = false
                                },
                            )
                        }
                    }
                }

                Button(
                    onClick = {
                        if (username.isEmpty()) {
                            hasErrorUsername = true
                        }
                        else if (email.isEmpty()) {
                            hasErrorEmail = true
                        }
                        else if (password.isEmpty()) {
                            hasErrorPassword = true
                        }
                        else if (gender.isEmpty()) {
                            hasErrorGender = true
                        }
                        else {
                            val passHash = passwordHash(password)
                            val firebaseAuth = FirebaseAuth.getInstance()
                            val firebaseDatabase = FirebaseDatabase.getInstance().getReference("user")
                            firebaseAuth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener{
                                    if (it.isSuccessful){
                                        val id=firebaseAuth.currentUser?.uid
                                        if (id!=null){
                                            val user=User(id,username,email,passHash, gender,null)
                                            firebaseDatabase.child(id).setValue(user)
                                            navController.navigate("Login")
                                            Toast.makeText(context,"Register Success",Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                    else{
                                        Toast.makeText(context,"Register Failed",Toast.LENGTH_SHORT).show()
                                    }
                                }

                        }
                    },
                    modifier = Modifier
                        .padding(top = 20.dp, start = 10.dp, end = 10.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(0.dp)
                ) {
                    Text("Register", textAlign = TextAlign.Center)
                }

                TextButton(onClick = { navController.navigate("Login") }) {
                    Text("Have an account?")
                }
            }
        }
    }

    if (hasErrorUsername) {
        LaunchedEffect(Unit) {
            focusRequesterUsername.requestFocus()
        }
        Toast.makeText(context, "Please fill username field", Toast.LENGTH_SHORT).show()
    }
    if (hasErrorEmail) {
        LaunchedEffect(Unit) {
            focusRequesterEmail.requestFocus()
        }
        Toast.makeText(context, "Please fill email field", Toast.LENGTH_SHORT).show()
    }
    if (hasErrorPassword) {
        LaunchedEffect(Unit) {
            focusRequesterPassword.requestFocus()
        }
        Toast.makeText(context, "Please fill password field", Toast.LENGTH_SHORT).show()
    }
    if (hasErrorGender) {
        LaunchedEffect(Unit) {
            focusRequesterGender.requestFocus()
        }
        Toast.makeText(context, "Please fill gender field", Toast.LENGTH_SHORT).show()
    }
}

fun passwordHash(input: String): String {
    val byte = MessageDigest.getInstance("SHA-256").digest(input.toByteArray())
    return byte.joinToString("") { "%02x".format(it) }
}