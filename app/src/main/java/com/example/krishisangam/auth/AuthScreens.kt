package com.example.krishisangam.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val PrimaryGreen = Color(0xFF01AC66)
private val BackgroundColor = Color(0xFF003D22)
private val DeepGreen = Color(0xFF002514)
private val AccentYellow = Color(0xFFFFC107)
private val FieldColor = Color(0xFFF4F4F4)
private val TextDark = Color(0xFF111111)
private val TextLight = Color(0xFFF5FFF9)
private val TextMuted = Color(0xFFB9D8C7)

@Composable
fun AuthBackground(
    headerTopPadding: Int = 26,
    spaceAfterHeader: Int = 44,
    content: @Composable ColumnScope.() -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF005C32),
                        BackgroundColor,
                        DeepGreen
                    )
                )
            )
    ) {
        Box(
            modifier = Modifier
                .size(260.dp)
                .align(Alignment.TopEnd)
                .offset(x = 95.dp, y = (-100).dp)
                .background(AccentYellow.copy(alpha = 0.10f), CircleShape)
        )

        Box(
            modifier = Modifier
                .size(290.dp)
                .align(Alignment.BottomStart)
                .offset(x = (-130).dp, y = 120.dp)
                .background(PrimaryGreen.copy(alpha = 0.16f), CircleShape)
        )

        Box(
            modifier = Modifier
                .size(170.dp)
                .align(Alignment.CenterEnd)
                .offset(x = 90.dp, y = 40.dp)
                .background(Color.White.copy(alpha = 0.045f), CircleShape)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 22.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BrandHeader(headerTopPadding = headerTopPadding)

            Spacer(modifier = Modifier.height(spaceAfterHeader.dp))

            content()

            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}

@Composable
private fun BrandHeader(
    headerTopPadding: Int = 26
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = headerTopPadding.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.14f))
                .border(
                    width = 1.dp,
                    color = Color.White.copy(alpha = 0.28f),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "🌾",
                fontSize = 23.sp
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        Text(
            text = "Krishi",
            color = TextLight,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.width(4.dp))

        Text(
            text = "Sangam",
            color = AccentYellow,
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold
        )

        Spacer(modifier = Modifier.weight(1f))

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(50.dp))
                .background(Color.White.copy(alpha = 0.12f))
                .border(
                    width = 1.dp,
                    color = Color.White.copy(alpha = 0.18f),
                    shape = RoundedCornerShape(50.dp)
                )
                .padding(horizontal = 12.dp, vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "🌐 EN",
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun AuthHeroTitle(
    title: String,
    highlightedTitle: String,
    subtitle: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = title,
            color = Color.White,
            fontSize = 30.sp,
            lineHeight = 36.sp,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center
        )

        Text(
            text = highlightedTitle,
            color = AccentYellow,
            fontSize = 30.sp,
            lineHeight = 36.sp,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center
        )

        if (subtitle.isNotBlank()) {
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = subtitle,
                color = Color.White.copy(alpha = 0.68f),
                fontSize = 14.sp,
                lineHeight = 21.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }
}

@Composable
private fun MiniTrustBadge() {
    Text(
        text = "🌾 Verified farmer-to-buyer network",
        color = Color.White.copy(alpha = 0.78f),
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold,
        textAlign = TextAlign.Center
    )
}

@Composable
private fun GlassPanel(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 14.dp,
                shape = RoundedCornerShape(28.dp)
            )
            .clip(RoundedCornerShape(28.dp))
            .background(Color.White.copy(alpha = 0.095f))
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.16f),
                shape = RoundedCornerShape(28.dp)
            )
            .padding(14.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        content = content
    )
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
            Text(
                text = placeholder,
                color = Color.White.copy(alpha = 0.55f)
            )
        },
        leadingIcon = {
            Text(
                text = leadingEmoji,
                fontSize = 18.sp
            )
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
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            disabledTextColor = Color.White,
            focusedContainerColor = Color.White.copy(alpha = 0.13f),
            unfocusedContainerColor = Color.White.copy(alpha = 0.09f),
            focusedBorderColor = PrimaryGreen,
            unfocusedBorderColor = Color.White.copy(alpha = 0.18f),
            cursorColor = AccentYellow
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp)
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
        shape = RoundedCornerShape(17.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = PrimaryGreen,
            disabledContainerColor = PrimaryGreen.copy(alpha = 0.65f)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(17.dp)
            )
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
                fontWeight = FontWeight.ExtraBold,
                fontSize = 16.sp
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
            color = if (viewModel.uiState.isError) Color(0xFFFF6B6B) else PrimaryGreen,
            fontSize = 13.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun RoleSelectionScreen(
    onBuyerClick: () -> Unit,
    onFarmerClick: () -> Unit,
    onManagerClick: () -> Unit,
    onTransportClick: () -> Unit
) {
    var selectedRole by remember { mutableStateOf<String?>(null) }

    AuthBackground(
        headerTopPadding = 50,
        spaceAfterHeader = 70
    ) {
        AuthHeroTitle(
            title = "Empowering Farmers.",
            highlightedTitle = "Simplifying Trade.",
            subtitle = ""
        )

        Spacer(modifier = Modifier.height(20.dp))

        MiniTrustBadge()

        Spacer(modifier = Modifier.height(34.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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

            Spacer(modifier = Modifier.height(14.dp))

            RoleCardWide(
                title = "Agro Node Manager",
                emoji = "🏢",
                selected = selectedRole == "manager",
                onClick = {
                    selectedRole = "manager"
                    onManagerClick()
                }
            )

            Spacer(modifier = Modifier.height(14.dp))

            RoleCardWide(
                title = "Transport Partner",
                emoji = "🚚",
                selected = selectedRole == "transport",
                onClick = {
                    selectedRole = "transport"
                    onTransportClick()
                }
            )
        }
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
            .height(106.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (selected) PrimaryGreen else Color.White.copy(alpha = 0.09f)
        ),
        border = BorderStroke(
            width = 1.dp,
            color = if (selected) PrimaryGreen else Color.White.copy(alpha = 0.18f)
        ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = emoji,
                fontSize = 31.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = title,
                color = Color.White,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun RoleCardWide(
    title: String,
    emoji: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (selected) PrimaryGreen else Color.White.copy(alpha = 0.09f)
        ),
        border = BorderStroke(
            width = 1.dp,
            color = if (selected) PrimaryGreen else Color.White.copy(alpha = 0.18f)
        ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = emoji,
                fontSize = 30.sp
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = title,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 17.sp,
                color = Color.White
            )
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
        AuthHeroTitle(
            title = "Welcome Back",
            highlightedTitle = "to Krishi Sangam",
            subtitle = "Access verified buyers, farmer listings, Agro Nodes and transparent payments."
        )

        Spacer(modifier = Modifier.height(18.dp))

        MiniTrustBadge()

        Spacer(modifier = Modifier.height(26.dp))

        GlassPanel {
            Text(
                text = "Login / Register",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(22.dp))

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

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Forgot Password",
                modifier = Modifier
                    .align(Alignment.End)
                    .clickable { onForgotPassword() },
                fontSize = 13.sp,
                textDecoration = TextDecoration.Underline,
                fontWeight = FontWeight.SemiBold,
                color = Color.White.copy(alpha = 0.86f)
            )

            Spacer(modifier = Modifier.height(24.dp))

            GreenButton(
                text = "Sign In",
                isLoading = viewModel.uiState.isLoading
            ) {
                viewModel.signIn(email, password) {
                    onLoginSuccess()
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Divider(color = Color.White.copy(alpha = 0.14f))

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Don’t Have An Account ? Sign Up",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .clickable { onGoToSignup() },
                fontSize = 14.sp,
                color = AccentYellow,
                fontWeight = FontWeight.Bold
            )

            AuthMessage(viewModel)
        }
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
        AuthHeroTitle(
            title = "Create Account",
            highlightedTitle = "Join Krishi Sangam",
            subtitle = "Connect directly with farmers, buyers and verified Agro Node managers."
        )

        Spacer(modifier = Modifier.height(22.dp))

        GlassPanel {
            Text(
                text = "Create An Account",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(18.dp))

            AuthInputField(
                value = firstName,
                onValueChange = { firstName = it },
                placeholder = "First Name",
                leadingEmoji = "👤"
            )

            Spacer(modifier = Modifier.height(11.dp))

            AuthInputField(
                value = lastName,
                onValueChange = { lastName = it },
                placeholder = "Last Name",
                leadingEmoji = "👤"
            )

            Spacer(modifier = Modifier.height(11.dp))

            AuthInputField(
                value = email,
                onValueChange = { email = it },
                placeholder = "Email",
                leadingEmoji = "✉️"
            )

            Spacer(modifier = Modifier.height(11.dp))

            AuthInputField(
                value = password,
                onValueChange = { password = it },
                placeholder = "Password",
                leadingEmoji = "🔒",
                isPassword = true
            )

            Spacer(modifier = Modifier.height(11.dp))

            AuthInputField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                placeholder = "Confirm Password",
                leadingEmoji = "🔒",
                isPassword = true
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Checkbox(
                    checked = agreeTerms,
                    onCheckedChange = { agreeTerms = it },
                    colors = CheckboxDefaults.colors(
                        checkedColor = PrimaryGreen,
                        uncheckedColor = Color.White.copy(alpha = 0.55f),
                        checkmarkColor = Color.White
                    )
                )

                Text(
                    text = "I agree to the Terms & Conditions",
                    fontSize = 13.sp,
                    color = Color.White.copy(alpha = 0.82f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

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

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Already Have An Account ? Sign In",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .clickable { onGoToLogin() },
                fontSize = 14.sp,
                color = AccentYellow,
                fontWeight = FontWeight.Bold
            )

            AuthMessage(viewModel)
        }
    }
}

@Composable
fun ForgotPasswordScreen(
    viewModel: AuthViewModel,
    onBackToLogin: () -> Unit
) {
    var email by remember { mutableStateOf("") }

    AuthBackground {
        AuthHeroTitle(
            title = "Forgot Password",
            highlightedTitle = "Recover Access",
            subtitle = "Enter your registered email and we will send you a reset link."
        )

        Spacer(modifier = Modifier.height(26.dp))

        GlassPanel {
            Text(
                text = "Reset Password",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(22.dp))

            AuthInputField(
                value = email,
                onValueChange = { email = it },
                placeholder = "Email",
                leadingEmoji = "✉️"
            )

            Spacer(modifier = Modifier.height(26.dp))

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
                textDecoration = TextDecoration.Underline,
                color = AccentYellow,
                fontWeight = FontWeight.Bold
            )

            AuthMessage(viewModel)
        }
    }
}

@Composable
fun EmailVerificationScreen(
    viewModel: AuthViewModel,
    onContinueToLogin: () -> Unit
) {
    AuthBackground {
        AuthHeroTitle(
            title = "Verify Email",
            highlightedTitle = "Secure Access",
            subtitle = "Please verify your email before signing in to Krishi Sangam."
        )

        Spacer(modifier = Modifier.height(26.dp))

        GlassPanel {
            Text(
                text = "Verify Your Email !",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "We have sent a verification link to your email. Please verify your email before signing in.",
                fontSize = 14.sp,
                color = TextMuted,
                textAlign = TextAlign.Center,
                lineHeight = 21.sp
            )

            Spacer(modifier = Modifier.height(26.dp))

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
                textDecoration = TextDecoration.Underline,
                color = AccentYellow,
                fontWeight = FontWeight.Bold
            )

            AuthMessage(viewModel)
        }
    }
}