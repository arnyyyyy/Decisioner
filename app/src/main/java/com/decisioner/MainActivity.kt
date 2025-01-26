package com.decisioner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import com.decisioner.screens.DecisionScreen
import com.decisioner.screens.ResultScreen
import com.decisioner.ui.theme.DecisionerTheme

const val QUESTIONS_SIZE = 4

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DecisionerTheme {
                var showResult by rememberSaveable { mutableStateOf(false) }
                var totalScore by rememberSaveable { mutableFloatStateOf(0.0f) }
                var currentQuestionIndex by rememberSaveable { mutableIntStateOf(0) }

                fun resetTest() {
                    totalScore = 0.0f
                    currentQuestionIndex = 0
                    showResult = false
                }

                if (showResult) {
                    ResultScreen(totalScore = totalScore, onRestart = { resetTest() })
                } else {
                    if (currentQuestionIndex < QUESTIONS_SIZE) {
                        DecisionScreen(
                            currentQuestionIndex = currentQuestionIndex,
                            onAnswer = { score ->
                                totalScore += score
                                currentQuestionIndex++
                                if (currentQuestionIndex >= QUESTIONS_SIZE) {
                                    showResult = true
                                }
                            },
                        )
                    } else {
                        showResult = true
                    }
                }
            }
        }
    }
}