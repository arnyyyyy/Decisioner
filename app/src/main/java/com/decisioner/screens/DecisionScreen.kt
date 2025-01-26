package com.decisioner.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.decisioner.R
import com.decisioner.logics.Coefficients
import com.decisioner.logics.Question


@Composable
fun DecisionScreen(
    currentQuestionIndex: Int,
    onAnswer: (Float) -> Unit,
) {
    val context = LocalContext.current

    val questions = listOf(
        Question(context.getString(R.string.question1), Coefficients(2.0f, 1.5f, -1f, -2f)),
        Question(context.getString(R.string.question2), Coefficients(1.5f, 1.25f, -0.65f, -1.5f)),
        Question(context.getString(R.string.question3), Coefficients(1.0f, 0.75f, -0.75f, -1.0f)),
        Question(context.getString(R.string.question4), Coefficients(0.95f, 0.75f, -0.75f, -0.75f))
    )

    val currentQuestion = questions[currentQuestionIndex]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().padding(10.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = currentQuestion.text, fontSize = 25.sp, textAlign = TextAlign.Center)
        }
        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalArrangement = Arrangement.Center
        ) {
            item {
                AnswerButton(text = context.getString(R.string.yes)) {
                    onAnswer(currentQuestion.coefficients.yes)
                }
            }
            item {
                AnswerButton(text = context.getString(R.string.mostlyYes)) {
                    onAnswer(currentQuestion.coefficients.mostlyYes)
                }
            }
            item {
                AnswerButton(text = context.getString(R.string.no)) {
                    onAnswer(currentQuestion.coefficients.no)
                }
            }
            item {
                AnswerButton(text = context.getString(R.string.mostlyNo)) {
                    onAnswer(currentQuestion.coefficients.mostlyNo)
                }
            }

        }
    }

}