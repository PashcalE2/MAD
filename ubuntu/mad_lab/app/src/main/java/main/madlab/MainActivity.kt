package main.madlab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import main.madlab.ui.theme.MADLabTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MADLabTheme {
                Main()
            }
        }
    }
}

@Composable
fun DeviceListItem() {
    Column {
        Row {

        }

        Text(text = "Жаробор")

        Text(text = "Кухня | 27*C")
    }
}

@Composable
fun Main() {
    Scaffold (
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    println(123)
                }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Добавить устройство")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.background(color = Color.Magenta)
            )
            {
                Text(text = "Весь дом")

                Text(text = "Гостинная")

                Text(text = "Коридор")
            }

            LazyColumn(
                modifier = Modifier
                    .background(Color.White)
                    .weight(1f, false)
            ) {

            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun MainPreview() {
    Main()
}
