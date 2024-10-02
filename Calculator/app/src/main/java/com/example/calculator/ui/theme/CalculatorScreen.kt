import androidx.compose.foundation.background
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
import android.util.Log

fun HandleInput(input: String) {
    // Aqui você pode implementar a lógica para lidar com a entrada do botão
    println("Botão pressionado: $input") // Exibe o input no console
}

@Composable
fun CalculatorScreen(modifier: Modifier = Modifier) {
    var calculatorText by remember { mutableStateOf("0") }

    Column(
        modifier = modifier
            .padding(16.dp)
    ) {
        // Exibir o texto da calculadora, alinhado à direita
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopEnd) {
            Text(text = calculatorText, fontSize = 56.sp)
        }

        // Criar as linhas com os botões
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
                    Log.d("Calculator", "Botão pressionado")
                    Button(
                        onClick = { HandleInput(text) }, // Passa o texto do botão para HandleInput
                        shape = CircleShape, // Define os botões como redondos
                        modifier = Modifier
                            .weight(1f) // Força cada botão a ocupar o mesmo espaço
                            .aspectRatio(1f) // Garante que os botões sejam quadrados (e depois redondos)
                            .padding(2.dp) // Reduz o espaçamento entre os botões
                    ) {
                        Text(
                            text = text,
                            fontSize = 24.sp, // Aumenta o tamanho da fonte
                            fontWeight = FontWeight.Bold, // Torna o texto mais espesso, se desejado
                            color = Color.White // Texto dos botões branco
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

