package main.madlab.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType

private val LightColorScheme = lightColorScheme(
    primaryContainer = Color(0xfff0f7ff),
    primary = Color.Black,

    secondaryContainer = Color(0xfff0f0f0),
    secondary = Color.Black
)

val MainPageRoomStyle = TextStyle(
    color = Color.Black,
    fontSize = TextUnit(20f, TextUnitType.Sp),
    fontFamily = FontFamily.SansSerif,
    fontStyle = FontStyle.Normal
)

@Composable
fun MADLabTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}