package com.example.krishisangam

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.krishisangam.auth.AuthNavGraph
import com.example.krishisangam.buyer.BuyerPaymentStore
import com.example.krishisangam.ui.theme.KrishiSangamTheme
import com.razorpay.PaymentResultListener

class MainActivity : ComponentActivity(), PaymentResultListener {

    override fun onCreate(savedInstanceState: Bundle?) {

        LanguageManager.applyLanguage(
            LanguageManager.getSavedLanguage(this)
        )

        installSplashScreen()

        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            KrishiSangamTheme {
                AuthNavGraph()
            }
        }
    }

    override fun onPaymentSuccess(razorpayPaymentId: String?) {
        BuyerPaymentStore.markSuccess(
            paymentId = razorpayPaymentId
        )
    }

    override fun onPaymentError(
        code: Int,
        response: String?
    ) {
        BuyerPaymentStore.markFailure(
            message = response ?: "Payment failed. Please try again."
        )
    }
}