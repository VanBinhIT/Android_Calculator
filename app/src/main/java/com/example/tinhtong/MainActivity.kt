package com.example.tinhtong

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tinhtong.ui.theme.TinhTongTheme
import kotlin.math.abs

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TinhTongTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CalculatorScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .padding(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun CalculatorScreen(modifier: Modifier = Modifier) {
    var aText by rememberSaveable { mutableStateOf("") }
    var bText by rememberSaveable { mutableStateOf("") }
    var resultText by rememberSaveable { mutableStateOf("Kết quả sẽ hiển thị ở đây") }
    var errorText by remember { mutableStateOf<String?>(null) }

    fun parseNumber(s: String): Double? = s.trim().replace(',', '.').toDoubleOrNull()

    fun displayResult(value: Double) {
        // Nếu gần số nguyên, hiển thị không có .0
        val pretty = if (abs(value - value.toLong()) < 1e-9) {
            value.toLong().toString()
        } else value.toString()
        resultText = "Kết quả: $pretty"
        errorText = null
    }

    fun calculate(op: Char) {
        val a = parseNumber(aText)
        val b = parseNumber(bText)
        if (a == null || b == null) {
            errorText = "Vui lòng nhập đúng 2 số (có thể dùng dấu chấm hoặc phẩy cho thập phân)."
            return
        }
        when (op) {
            '+' -> displayResult(a + b)
            '-' -> displayResult(a - b)
            '×' -> displayResult(a * b)
            '÷' -> {
                if (abs(b) < 1e-12) {
                    errorText = "Không thể chia cho 0."
                } else displayResult(a / b)
            }
        }
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Android Calculator (Compose)",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = aText,
            onValueChange = { aText = it },
            label = { Text("Số thứ nhất") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = bText,
            onValueChange = { bText = it },
            label = { Text("Số thứ hai") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CalcButton("＋") { calculate('+') }
            CalcButton("－") { calculate('-') }
            CalcButton("×") { calculate('×') }
            CalcButton("÷") { calculate('÷') }
        }

        Spacer(Modifier.height(16.dp))

        if (errorText != null) {
            Text(text = errorText!!, color = MaterialTheme.colorScheme.error)
        }

        Card(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = resultText,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(16.dp)
            )
        }

        Spacer(Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { aText = ""; bText = ""; resultText = "Kết quả sẽ hiển thị ở đây"; errorText = null },
                modifier = Modifier.weight(1f)
            ) { Text("Xoá") }

            Button(
                onClick = { // ví dụ nhỏ: đổi chỗ nhanh
                    val tmp = aText; aText = bText; bText = tmp
                },
                modifier = Modifier.weight(1f)
            ) { Text("Đổi chỗ a↔b") }
        }
    }
}

@Composable
private fun CalcButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
    ) { Text(text) }
}

@Preview(showBackground = true)
@Composable
fun CalculatorPreview() {
    TinhTongTheme {
        CalculatorScreen(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        )
    }
}
