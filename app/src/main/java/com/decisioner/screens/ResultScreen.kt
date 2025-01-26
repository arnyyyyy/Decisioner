package com.decisioner.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.decisioner.R
import ru.rustore.sdk.review.RuStoreReviewManager
import ru.rustore.sdk.review.RuStoreReviewManagerFactory
import ru.rustore.sdk.review.model.ReviewInfo

@Composable
fun ResultScreen(totalScore: Float, onRestart: () -> Unit) {
    val context = LocalContext.current
    val manager = RuStoreReviewManagerFactory.create(context)

    val reviewInfoState = remember { mutableStateOf<ReviewInfo?>(null) }
    val isReviewRequested = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when {
            totalScore >= 3.5f -> {
                Image(
                    painter = painterResource(id = R.drawable.green_checkmark_icon),
                    contentDescription = "Yes",
                    modifier = Modifier.size(200.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = context.getString(R.string.answer1),
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                )
            }

            totalScore > 0.0f && totalScore < 3.5f -> {
                Image(
                    painter = painterResource(id = R.drawable.light_bulb_color_icon),
                    contentDescription = "Pause",
                    modifier = Modifier.size(200.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = context.getString(R.string.answer2),
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                )
            }

            totalScore <= 0.0f -> {
                Image(
                    painter = painterResource(id = R.drawable.red_x_icon),
                    contentDescription = "No",
                    modifier = Modifier.size(200.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = context.getString(R.string.answer3),
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRestart) {
            Text(text = context.getString(R.string.restart), fontSize = 16.sp)
        }

        if (!isReviewRequested.value) {
            Log.d("rustore", "Try to get feedback")
            manager.requestReviewFlow()
                .addOnSuccessListener { reviewInfo ->
                    reviewInfoState.value = reviewInfo
                    isReviewRequested.value = true
                    launchReviewFlow(manager, reviewInfo)
                }
                .addOnFailureListener { throwable ->
                  Log.e("feedback error:", throwable.message.toString())
                }
        }
    }
}

private fun launchReviewFlow(manager: RuStoreReviewManager, reviewInfo: ReviewInfo) {
    manager.launchReviewFlow(reviewInfo)
        .addOnSuccessListener {

        }
        .addOnFailureListener { throwable ->
            Log.e("feedback error:", throwable.message.toString())
        }
}