package com.example.calculator.ui.theme

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@Composable
fun CalculatorScreen(modifier: Modifier = Modifier) {
    var calculatorText by remember { mutableStateOf("0") }
    var operators = arrayOf("÷", "x", "-", "+")
    var tf = true;

    Column(modifier = modifier) {
        Text(text = calculatorText)
        for (i in 0..2) {
            Row {
                for (j in 1..3) {
                    val buttonText = (j + (3 * i)).toString()

                    Button(onClick = { calculatorText = Calculate(buttonText, calculatorText) }) {
                        Text(text = buttonText)
                    }
                }
                Button(onClick = { }) {
                    Text(text = (operators[i]))
                }
            }
        }

        Row{
            Button(onClick = {}) {
                Text(text = "C")
            }
            Button(onClick = {}) {
                Text(text = "0")
            }
            Button(onClick = {}) {
                Text(text = ",")
            }
            Button(onClick = { }) {
                Text(text = "+")
            }
        }

    }
}

// Função que calcula ou concatena o valor inserido
fun Calculate(input: String, currentText: String): String {
    return if (currentText == "0") {
        input // Substituir "0" pelo número clicado
    } else {
        currentText + input // Concatenar o número clicado ao valor atual
    }
}

@Preview(showBackground = true)
@Composable
fun CalculatorScreenPreview() {
    CalculatorTheme {
        CalculatorScreen()
    }
}
