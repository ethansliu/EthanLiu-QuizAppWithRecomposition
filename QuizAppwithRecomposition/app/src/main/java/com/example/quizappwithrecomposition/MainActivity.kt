package com.example.quizappwithrecomposition

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quizappwithrecomposition.ui.theme.QuizAppWithRecompositionTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QuizAppWithRecompositionTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    FlashcardQuizApp()
                }
            }
        }
    }
}

@Composable
fun FlashcardQuizApp() {
    // List of questions and answers
    val questions = listOf(
        "What is the capital of the United States?" to "Washington DC",
        "What is the powerhouse of the cell?" to "Mitochondria",
        "What is 10 + 10?" to "20",
        "What is the most expensive mushroom" to "Truffle",
        "What is the numerical name for the BU Mobile App Development Course" to "CS501",
        "What is the IDE for CS501" to "Android Studio",

    )

    // Mutable variables
    var currentQuestionIndex by remember { mutableStateOf(0) }
    var userInput by remember { mutableStateOf("") }
    var quizComplete by remember { mutableStateOf(false) }

    // Snackbar
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }


    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = 20.dp,
                    start = 40.dp,
                    end = 40.dp
                )
        ) {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
            // Check if the quiz is complete
            if (quizComplete) {
                // Display quiz completion message and restart button
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            top = 20.dp,
                            start = 40.dp,
                            end = 40.dp
                        ),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Quiz Complete!")
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                        // Restart the quiz
                        currentQuestionIndex = 0
                        quizComplete = false
                        userInput = ""
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF3943cb),
                            contentColor = Color.White
                        )
                    ) {
                        Text("Restart Quiz")
                    }
                }
            } else {
                // Get the current question and correct answer
                val (currentQuestion, correctAnswer) = questions[currentQuestionIndex]

                // Main content
                Column (
                    // Aligns the contents
                    modifier = Modifier.padding(
                        top = 80.dp,
                        start = 110.dp,
                        end = 100.dp
                    )
                ) {
                    Text(
                        text = "QuizCard",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            top = 180.dp,
                            start = 40.dp,
                            end = 40.dp
                        )
                ) {
                    // Question Card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF3943cb),
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = currentQuestion,
                            modifier = Modifier.padding(16.dp),
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Answer Input
                    TextField(
                        value = userInput,
                        onValueChange = { userInput = it },
                        label = { Text("Your Answer") },
                        modifier = Modifier.fillMaxWidth(),
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Submit Button
                    Button(
                        onClick = {
                            val isCorrect =
                                userInput.trim().equals(correctAnswer, ignoreCase = true)
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar(
                                    message = if (isCorrect) "Correct!" else "Incorrect, Try Again",
                                    duration = SnackbarDuration.Short
                                )
                            }
                            userInput = ""
                            if (isCorrect) {
                                if (currentQuestionIndex < questions.size - 1) {
                                    currentQuestionIndex += 1
                                } else {
                                    // Set quiz to complete
                                    quizComplete = true
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar(
                                            message = "Quiz Completed!",
                                            duration = SnackbarDuration.Short
                                        )
                                    }
                                }
                            }
                        },
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF3943cb),
                            contentColor = Color.White
                        )
                    ) {
                        Text("Submit Answer")
                    }
                }
            }
        }
    }
}