package com.example.zenithra.UI

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.example.zenithra.App
import com.example.zenithra.model.UserRepository
import kotlinx.coroutines.launch


@Composable
fun SignUpScreen(
    onSignUpSuccess: () -> Unit = {},
    onSignInClick: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val context = LocalContext.current
    val userRepository = UserRepository((context.applicationContext as App).appDatabase)
    val coroutineScope = rememberCoroutineScope()

    val isSignUpEnabled = email.isNotBlank() && password.isNotBlank()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Black // entire background is black
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(0.dp), // Padding around the center box
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(0.85f) // Adjust width of the box as needed
                    .height(300.dp), // Set height constraint for the Card
                shape = RoundedCornerShape(8.dp), // Optional: for rounded corners
                elevation = CardDefaults.cardElevation(8.dp), // Corrected elevation usage
                colors = CardDefaults.cardColors(containerColor = Color.DarkGray) // Corrected background color
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email",color = Color.White) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password",color = Color.White) },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                    )

                    Button(
                        onClick = {
                            coroutineScope.launch {
                                val userExists = userRepository.isUserExist(email)
                                if (userExists) {
                                    Toast.makeText(
                                        context,
                                        "Account already exists.",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                } else {
                                    userRepository.createUser(email, password)
                                    Toast.makeText(
                                        context,
                                        "Account created successfully.",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                    onSignUpSuccess()
                                }
                            }
                        },
                        enabled = isSignUpEnabled,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                    ) {
                        Text("Sign Up",color = Color.White)
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically // This fixes the alignment
                    ) {
                        Text("Already have an account?",color = Color.White)
                        TextButton(onClick = onSignInClick) {
                            Text("Sign In",color = Color.White)
                        }
                    }
                }
            }
        }
    }
}
