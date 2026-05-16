package com.example.krishisangam.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val PrimaryGreen = Color(0xFF01AC66)
private val BackgroundColor = Color(0xFFE8FAF6)
private val FieldColor = Color(0xFFF4F4F4)
private val TextDark = Color(0xFF111111)

@Composable
fun AuthBackground(
    content: @Composable ColumnScope.() -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
    ) {
        Box(
            modifier = Modifier
                .size(180.dp)
                .align(Alignment.TopEnd)
                .offset(x = 50.dp, y = (-60).dp)
                .background(Color(0xFFDFF8EF), CircleShape)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Center,
            content = content
        )
    }
}

@Composable
fun AuthInputField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingEmoji: String,
    isPassword: Boolean = false
) {
    var passwordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(placeholder, color = Color.Gray)
        },
        leadingIcon = {
            Text(leadingEmoji, fontSize = 18.sp)
        },
        trailingIcon = {
            if (isPassword) {
                Text(
                    text = if (passwordVisible) "🙈" else "👁️",
                    fontSize = 18.sp,
                    modifier = Modifier.clickable {
                        passwordVisible = !passwordVisible
                    }
                )
            }
        },
        visualTransformation = if (isPassword && !passwordVisible) {
            PasswordVisualTransformation()
        } else {
            VisualTransformation.None
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = if (isPassword) KeyboardType.Password else KeyboardType.Email
        ),
        singleLine = true,
        shape = RoundedCornerShape(14.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = TextDark,
            unfocusedTextColor = TextDark,
            disabledTextColor = TextDark,
            focusedContainerColor = FieldColor,
            unfocusedContainerColor = FieldColor,
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            cursorColor = PrimaryGreen
        ),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun GreenButton(
    text: String,
    isLoading: Boolean = false,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = !isLoading,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .shadow(8.dp, RoundedCornerShape(12.dp))
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color = Color.White,
                strokeWidth = 2.dp,
                modifier = Modifier.size(22.dp)
            )
        } else {
            Text(
                text = text,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun AuthMessage(viewModel: AuthViewModel) {
    val message = viewModel.uiState.message

    if (message != null) {
        Spacer(modifier = Modifier.height(14.dp))
        Text(
            text = message,
            color = if (viewModel.uiState.isError) Color.Red else PrimaryGreen,
            fontSize = 13.sp
        )
    }
}

@Composable
fun RoleSelectionScreen(
    onBuyerClick: () -> Unit,
    onFarmerClick: () -> Unit,
    onManagerClick: () -> Unit
) {
    var selectedRole by remember { mutableStateOf<String?>(null) }

    AuthBackground {
        Text(
            text = "Select Your\nAccount Type",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = TextDark
        )

        Spacer(modifier = Modifier.height(34.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            RoleCard(
                title = "Buyer",
                emoji = "🛒",
                selected = selectedRole == "buyer",
                modifier = Modifier.weight(1f),
                onClick = {
                    selectedRole = "buyer"
                    onBuyerClick()
                }
            )

            RoleCard(
                title = "Farmer",
                emoji = "🌾",
                selected = selectedRole == "farmer",
                modifier = Modifier.weight(1f),
                onClick = {
                    selectedRole = "farmer"
                    onFarmerClick()
                }
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        RoleCardWide(
            title = "Agro Node Manager",
            subtitle = "Verify produce and manage dispatch",
            emoji = "🏢",
            selected = selectedRole == "manager",
            onClick = {
                selectedRole = "manager"
                onManagerClick()
            }
        )
    }
}
@Composable
fun RoleCard(
    title: String,
    emoji: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(130.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (selected) PrimaryGreen else FieldColor
        ),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(emoji, fontSize = 38.sp)

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = title,
                color = if (selected) Color.White else Color.Gray,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp
            )
        }
    }
}

@Composable
fun RoleCardWide(
    title: String,
    subtitle: String,
    emoji: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (selected) PrimaryGreen else Color.White
        ),
        elevation = CardDefaults.cardElevation(5.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(emoji, fontSize = 34.sp)

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = if (selected) Color.White else TextDark
                )

                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = if (selected) Color.White else Color.Gray
                )
            }
        }
    }
}

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onLoginSuccess: () -> Unit,
    onGoToSignup: () -> Unit,
    onForgotPassword: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    AuthBackground {
        Text(
            text = "Let’s Get\nYou Sign In !",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = TextDark
        )

        Spacer(modifier = Modifier.height(32.dp))

        AuthInputField(
            value = email,
            onValueChange = { email = it },
            placeholder = "Email",
            leadingEmoji = "✉️"
        )

        Spacer(modifier = Modifier.height(14.dp))

        AuthInputField(
            value = password,
            onValueChange = { password = it },
            placeholder = "Password",
            leadingEmoji = "🔒",
            isPassword = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Forgot Password",
            modifier = Modifier
                .align(Alignment.End)
                .clickable { onForgotPassword() },
            fontSize = 13.sp,
            textDecoration = TextDecoration.Underline,
            fontWeight = FontWeight.SemiBold,
            color = TextDark
        )

        Spacer(modifier = Modifier.height(30.dp))

        GreenButton(
            text = "Sign In",
            isLoading = viewModel.uiState.isLoading
        ) {
            viewModel.signIn(email, password) {
                onLoginSuccess()
            }
        }

        Spacer(modifier = Modifier.height(22.dp))

        Divider(color = Color.LightGray)

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Don’t Have An Account ? Sign Up",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clickable { onGoToSignup() },
            fontSize = 14.sp,
            color = TextDark
        )

        AuthMessage(viewModel)
    }
}

@Composable
fun SignupScreen(
    viewModel: AuthViewModel,
    onSignupSuccess: () -> Unit,
    onGoToLogin: () -> Unit
) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var agreeTerms by remember { mutableStateOf(false) }

    AuthBackground {
        Text(
            text = "Create\nAn Account",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = TextDark
        )

        Spacer(modifier = Modifier.height(24.dp))

        AuthInputField(firstName, { firstName = it }, "First Name", "👤")

        Spacer(modifier = Modifier.height(12.dp))

        AuthInputField(lastName, { lastName = it }, "Last Name", "👤")

        Spacer(modifier = Modifier.height(12.dp))

        AuthInputField(email, { email = it }, "Email", "✉️")

        Spacer(modifier = Modifier.height(12.dp))

        AuthInputField(password, { password = it }, "Password", "🔒", true)

        Spacer(modifier = Modifier.height(12.dp))

        AuthInputField(confirmPassword, { confirmPassword = it }, "Confirm Password", "🔒", true)

        Spacer(modifier = Modifier.height(12.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = agreeTerms,
                onCheckedChange = { agreeTerms = it },
                colors = CheckboxDefaults.colors(checkedColor = PrimaryGreen)
            )

            Text(
                text = "I agree to the Terms & Conditions",
                fontSize = 13.sp
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        GreenButton(
            text = "Sign Up",
            isLoading = viewModel.uiState.isLoading
        ) {
            if (!agreeTerms) {
                viewModel.signUp("", "", "", "", "") {}
            } else {
                viewModel.signUp(
                    firstName = firstName,
                    lastName = lastName,
                    email = email,
                    password = password,
                    confirmPassword = confirmPassword,
                    onSuccess = onSignupSuccess
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = "Already Have An Account ? Sign In",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clickable { onGoToLogin() },
            fontSize = 14.sp,
            color = TextDark
        )

        AuthMessage(viewModel)
    }
}

@Composable
fun ForgotPasswordScreen(
    viewModel: AuthViewModel,
    onBackToLogin: () -> Unit
) {
    var email by remember { mutableStateOf("") }

    AuthBackground {
        Text(
            text = "Forgot Your\nPassword !",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = TextDark
        )

        Spacer(modifier = Modifier.height(32.dp))

        AuthInputField(
            value = email,
            onValueChange = { email = it },
            placeholder = "Email",
            leadingEmoji = "✉️"
        )

        Spacer(modifier = Modifier.height(30.dp))

        GreenButton(
            text = "Send",
            isLoading = viewModel.uiState.isLoading
        ) {
            viewModel.resetPassword(email)
        }

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = "Back to Sign In",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clickable { onBackToLogin() },
            textDecoration = TextDecoration.Underline
        )

        AuthMessage(viewModel)
    }
}

@Composable
fun EmailVerificationScreen(
    viewModel: AuthViewModel,
    onContinueToLogin: () -> Unit
) {
    AuthBackground {
        Text(
            text = "Verify Your\nEmail !",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = TextDark
        )

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = "We have sent a verification link to your email. Please verify your email before signing in.",
            fontSize = 15.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(30.dp))

        GreenButton(
            text = "Resend Verification Email",
            isLoading = viewModel.uiState.isLoading
        ) {
            viewModel.resendVerificationEmail()
        }

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = "Go to Sign In",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clickable { onContinueToLogin() },
            textDecoration = TextDecoration.Underline
        )

        AuthMessage(viewModel)
    }
}