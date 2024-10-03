import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import com.example.calculator.R
import java.math.BigDecimal
import java.math.RoundingMode

// Define global mutable state for calculator text
var calculatorText by mutableStateOf("0")
var lastNumber by mutableStateOf(BigDecimal.ZERO)
var currNumber by mutableStateOf(BigDecimal.ZERO)
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
        calculatorText = if (calculatorText == "0") input else calculatorText + input // Append if not zero
        isNewCalculation = false // Reset the new calculation flag
    } else {
        // Append numbers to calculator text
        if (calculatorText == "0") {
            calculatorText = input
        } else {
            // If the calculator text has a comma and we're inputting more numbers
            if (calculatorText.contains(",")) {
                calculatorText += input
            } else {
                // Normal append behavior
                calculatorText += input
            }
        }
    }
}


fun HandleSymbols(input: String) {
    if (calculatorText == "0" && (input != "C" && input != ",")) return // Do nothing if current text is just "0" and not clear

    when (input) {
        "C" -> {
            calculatorText = "0"
            lastNumber = BigDecimal.ZERO
            currNumber = BigDecimal.ZERO
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
            lastNumber = calculatorText.replace(",", ".").toBigDecimal() // Store the last number
            calculatorText = "0" // Reset for next input
            isNewCalculation = false // Don't reset when inputting a symbol after "="
        }
    }
}

fun FinishCurrent() {
    currNumber = calculatorText.replace(",", ".").toBigDecimal() // Convert current text to number

    // Perform the calculation based on the current symbol
    when (currSymbol) {
        "÷" -> lastNumber = lastNumber.divide(currNumber, 10, RoundingMode.HALF_UP) // Use divide with scale and rounding
        "×" -> lastNumber = lastNumber.multiply(currNumber)
        "-" -> lastNumber = lastNumber.subtract(currNumber)
        "+" -> lastNumber = lastNumber.add(currNumber)
        "%" -> lastNumber = lastNumber.remainder(currNumber)
    }

    // Format the result
    val formattedResult = formatBigDecimal(lastNumber)

    // Update calculator text with the formatted result
    calculatorText = formattedResult

    // Reset current symbol after calculation
   currSymbol = ""
}

// Function to format BigDecimal to a cleaner string representation
fun formatBigDecimal(value: BigDecimal): String {
    val strippedValue = value.stripTrailingZeros() // Remove trailing zeros
    return when {
        strippedValue.scale() <= 0 -> strippedValue.toBigInteger().toString() // If it's an integer
        else -> strippedValue.toPlainString().replace(".", ",") // Replace "." with "," for display
    }
}



@Composable
fun CalculatorScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .background(Color.Black)
    ) {
        // Create a horizontal scrolling row for the calculator text
        Box(modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(end = 16.dp), // Add padding to the right
            contentAlignment = Alignment.CenterEnd // Align text to the right
        ) {
            Text(
                text = calculatorText,
                fontSize = 80.sp, // Keep font size constant
                color = Color.White,
                modifier = Modifier.align(Alignment.CenterEnd)
            )
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
                        onClick = { HandleInput(text) },
                        shape = CircleShape,
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .padding(4.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = when (text) {
                                "⌫", "±", "%" -> Color(0xFF5C5C5F)
                                "1", "2", "3", "4", "5", "6", "7", "8", "9", "C", "0", "," -> Color(0xFF2A2A2C)
                                "÷", "×", "-", "+", "=" -> Color(0xFFFF9F0A)
                                else -> Color.DarkGray
                            }
                        )
                    ) {
                        when (text) {
                            "⌫" -> Icon(
                                painter = painterResource(id = R.drawable.backspace), // Check drawable name
                                contentDescription = "Backspace",
                                tint = Color.White
                            )
                            "±" -> Icon(
                                painter = painterResource(id = R.drawable.plus_minus_sign), // Check drawable name
                                contentDescription = "Plus/Minus",
                                tint = Color.White
                            )
                            "%" -> Icon(
                                painter = painterResource(id = R.drawable.percentage), // Check drawable name
                                contentDescription = "Percentage",
                                tint = Color.White,
                                modifier = Modifier.scale(1.75f)
                            )
                            "÷" -> Icon(
                                painter = painterResource(id = R.drawable.divide), // Check drawable name
                                contentDescription = "Division",
                                tint = Color.White,
                                modifier = Modifier.scale(2f)
                            )
                            "×" -> Icon(
                                painter = painterResource(id = R.drawable.multiply), // Check drawable name
                                contentDescription = "Multiplication",
                                tint = Color.White,
                                modifier = Modifier.scale(2f)
                            )
                            "-" -> Icon(
                                painter = painterResource(id = R.drawable.minus), // Check drawable name
                                contentDescription = "Subtraction",
                                tint = Color.White,
                                modifier = Modifier.scale(1.75f)
                            )
                            "+" -> Icon(
                                painter = painterResource(id = R.drawable.plus), // Check drawable name
                                contentDescription = "Addition",
                                tint = Color.White,
                                modifier = Modifier.scale(1.5f)
                            )
                            "=" -> Icon(
                                painter = painterResource(id = R.drawable.equals), // Check drawable name
                                contentDescription = "Equals",
                                tint = Color.White,
                                modifier = Modifier.scale(1.25f)
                            )
                            "," -> Icon(
                                painter = painterResource(id = R.drawable.comma), // Check drawable name
                                contentDescription = "Comma",
                                tint = Color.White,
                                modifier = Modifier.scale(2f).padding(bottom = 10.dp)
                            )
                            else -> Text(
                                text = text,
                                fontSize = 36.sp,
                                fontWeight = FontWeight.W300,
                                color = Color.White
                            )
                        }
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
