import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Define global mutable state for calculator text
var calculatorText by mutableStateOf("0")
var lastNumber by mutableStateOf(0.0)
var currNumber by mutableStateOf(0.0)
var currSymbol by mutableStateOf("")
var isNewCalculation by mutableStateOf(false) // Track if a new calculation is starting

fun HandleInput(input: String) {
    when (input) {
        "⌫", "±", "%", "÷", "×", "-", "+", "=", ",", "C" -> {
            HandleSymbols(input)
        }
        "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" -> {
            HandleNumbers(input)
        }
    }
}

fun HandleNumbers(input: String) {
    if (isNewCalculation) {
        // If starting a new calculation, reset only the symbol
        currSymbol = ""
        calculatorText = input
        isNewCalculation = false // Reset the new calculation flag
    } else {
        // Append numbers to calculator text
        if (calculatorText == "0") calculatorText = input
        else calculatorText += input
    }
}

fun HandleSymbols(input: String) {
    if (calculatorText == "0" && input != "C") return // Do nothing if current text is just "0" and not clear

    when (input) {
        "C" -> {
            calculatorText = "0"
            lastNumber = 0.0
            currNumber = 0.0
            currSymbol = ""
            isNewCalculation = false // Reset flag on clear
        } // Clear text example

        "⌫" -> {
            // Remove the last character or reset to "0" if it's the last character
            calculatorText = if (calculatorText.length > 1) calculatorText.dropLast(1) else "0"
        }

        "," -> if (!calculatorText.contains(",")) calculatorText += ","

        "±" -> {
            // Toggle the sign of the number
            if (calculatorText.startsWith("-")) {
                calculatorText = calculatorText.removePrefix("-") // Remove the negative sign
            } else {
                calculatorText = "-$calculatorText" // Add the negative sign
            }
        }

        "=" -> {
            // Perform calculation when "=" is pressed
            if (currSymbol.isNotEmpty()) {
                FinishCurrent()
            }
            isNewCalculation = true // Set flag to true after performing calculation
        }

        else -> {
            // If a symbol is pressed and we already have one, we finish the current operation
            if (currSymbol.isNotEmpty()) {
                FinishCurrent() // Perform the existing operation before setting a new symbol
            }
            currSymbol = input // Set the current symbol
            lastNumber = calculatorText.replace(",", ".").toDouble() // Store the last number
            calculatorText = "0" // Reset for next input
            isNewCalculation = false // Don't reset when inputting a symbol after "="
        }
    }
}

fun FinishCurrent() {
    currNumber = calculatorText.replace(",", ".").toDouble() // Convert current text to number

    // Perform the calculation based on the current symbol
    when (currSymbol) {
        "÷" -> lastNumber /= currNumber
        "×" -> lastNumber *= currNumber
        "-" -> lastNumber -= currNumber
        "+" -> lastNumber += currNumber
        "%" -> lastNumber %= currNumber
    }

    // Update calculator text with the result
    calculatorText = if (lastNumber % 1.0 == 0.0) {
        lastNumber.toInt().toString() // Convert to Int if it's a whole number
    } else {
        lastNumber.toString().replace(".", ",") // Replace "." with "," for display
    }

    // Reset current symbol after calculation
    currSymbol = ""
}

@Composable
fun CalculatorScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .padding(16.dp)
    ) {
        // Display calculator text, aligned to the right
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopEnd) {
            Text(text = calculatorText, fontSize = 56.sp)
        }

        // Create button rows
        val buttons = listOf(
            listOf("⌫", "±", "%", "÷"),
            listOf("1", "2", "3", "×"),
            listOf("4", "5", "6", "-"),
            listOf("7", "8", "9", "+"),
            listOf("C", "0", ",", "=")
        )

        buttons.forEach { row ->
            Row(modifier = Modifier.fillMaxWidth()) {
                row.forEach { text ->
                    Button(
                        onClick = { HandleInput(text) }, // HandleInput modifies the global calculatorText
                        shape = CircleShape, // Define buttons as round
                        modifier = Modifier
                            .weight(1f) // Force each button to occupy the same space
                            .aspectRatio(1f) // Ensure buttons are square (and then round)
                            .padding(2.dp) // Reduce spacing between buttons
                    ) {
                        Text(
                            text = text,
                            fontSize = 24.sp, // Increase font size
                            fontWeight = FontWeight.Bold, // Make text bold
                            color = Color.White // Button text color
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CalculatorScreenPreview() {
    CalculatorScreen()
}
