package com.example.zenithra.UI

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.zenithra.App
import com.example.zenithra.model.SignInViewModel
import com.example.zenithra.model.UserRepository
import com.example.zenithra.model.UserSessionManager
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Sign_In_Screen(
    onGoogleSignInClick: () -> Unit = {},
    onAppleSignInClick: () -> Unit = {},
    onSignInSuccess: () -> Unit = {},
    onForgotPasswordClick: () -> Unit = {},
    onSignUpClick: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val isSignInEnabled = email.isNotBlank() && password.isNotBlank()
    val signInViewModel: SignInViewModel = viewModel()
    val context = LocalContext.current
    val userRepository = UserRepository((context.applicationContext as App).appDatabase)

    val coroutineScope = rememberCoroutineScope()
    var errorMessage by remember { mutableStateOf("") }

    val handleError = { message: String ->
        errorMessage = message
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

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
                .height(600.dp), // Set height constraint for the Card
                shape = RoundedCornerShape(8.dp), // Optional: for rounded corners
                elevation = CardDefaults.cardElevation(8.dp), // Corrected elevation usage
                colors = CardDefaults.cardColors(containerColor = Color.DarkGray) // Corrected background color
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Zenithra",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Welcome back",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text("Please enter your details to sign in", fontSize = 14.sp, color = Color.White)

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        IconButton(onClick = onGoogleSignInClick) {
                            Icon(
                                Icons.Default.AccountCircle,
                                contentDescription = "Google",
                                tint = Color.White
                            )
                        }
                        IconButton(onClick = onAppleSignInClick) {
                            Icon(Icons.Default.Menu, contentDescription = "Apple", tint = Color.White)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Text("OR", color = Color.White, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Your Email Address", color = Color.White) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.White,
                            cursorColor = Color.White,
                            focusedLabelColor = Color.White,
                            unfocusedLabelColor = Color.White,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password", color = Color.White) },
                        singleLine = true,
                        trailingIcon = {
                            Icon(Icons.Default.Visibility, contentDescription = "Toggle", tint = Color.White)
                        },
                        visualTransformation = PasswordVisualTransformation(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.White,
                            cursorColor = Color.White,
                            focusedLabelColor = Color.White,
                            unfocusedLabelColor = Color.White,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = onForgotPasswordClick) {
                            Text("Forgot password?", color = Color.White)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            coroutineScope.launch {
                                try {
                                    val userExists = userRepository.isUserExist(email)
                                    val isPasswordCorrect =
                                        userRepository.checkUserCredentials(email, password)
                                    if (userExists && isPasswordCorrect) {
                                        UserSessionManager.setLoggedIn(context, true)


                                        signInViewModel.signInOrCreateUser(
                                            email = email,
                                            password = password,
                                            onSuccess = onSignInSuccess,
                                            onError = handleError,
                                        )
                                    } else {
                                        handleError("Invalid credentials")
                                    }
                                } catch (e: Exception) {
                                    handleError("Error: ${e.localizedMessage}")
                                    Log.i("SignInError", "Error: ${e.localizedMessage}")
                                }
                            }
                        },
                        enabled = isSignInEnabled,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                    ) {
                        Text("Sign In", color = Color.Black)
                    }

                    if (errorMessage.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = errorMessage,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Don't have an account?", color = Color.White)
                        TextButton(onClick = onSignUpClick) {
                            Text("Sign Up", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}
