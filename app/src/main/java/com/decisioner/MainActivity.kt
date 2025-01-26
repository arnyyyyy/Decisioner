package com.decisioner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.decisioner.logics.Event
import com.decisioner.logics.MainViewModel
import com.decisioner.screens.DecisionScreen
import com.decisioner.screens.ResultScreen
import com.decisioner.ui.theme.DecisionerTheme
import kotlinx.coroutines.launch


const val QUESTIONS_SIZE = 4

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DecisionerTheme {
                val snackbarHostState = remember { SnackbarHostState() }
                val scope = rememberCoroutineScope()
                var showResult by rememberSaveable { mutableStateOf(false) }
                var totalScore by rememberSaveable { mutableFloatStateOf(0.0f) }
                var currentQuestionIndex by rememberSaveable { mutableIntStateOf(0) }

                fun resetTest() {
                    totalScore = 0.0f
                    currentQuestionIndex = 0
                    showResult = false
                }


                Scaffold(
                    snackbarHost = { SnackbarHost(snackbarHostState) }
                ) { padding ->
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

                viewModel.init(this)


                lifecycleScope.launch {
                    viewModel.events
                        .flowWithLifecycle(lifecycle)
                        .collect { event ->
                            when (event) {
                                Event.UpdateCompleted -> {
                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            message = getString(R.string.downloading_completed),
                                            actionLabel = getString(R.string.button_install),
                                            duration = SnackbarDuration.Indefinite
                                        )
                                    }
                                }
                            }
                        }
                }
            }
        }
    }
}