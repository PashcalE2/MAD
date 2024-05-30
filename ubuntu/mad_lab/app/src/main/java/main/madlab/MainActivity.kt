package main.madlab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import main.madlab.db.data.DeviceInfo
import main.madlab.db.data.generateRandom
import main.madlab.ui.theme.MADLabTheme
import main.madlab.ui.theme.MainPageRoomStyle

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Main()
        }
    }
}

@Composable
fun DeviceListItem(deviceInfo: DeviceInfo) {
    var showMenu by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(top = Dp(5f))
            .background(Color.White)
            .border(Dp(2f), Color.LightGray)
            .padding(Dp(5f))

    ) {
        Row {
            Image(
                painter = painterResource(id = deviceInfo.imgId),
                contentDescription = null,
                modifier = Modifier
                    .width(Dp(300f))
                    .height(Dp(200f))
            )

            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.TopEnd
            ) {
                IconButton(
                    onClick = {
                        showMenu = !showMenu
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = null
                    )
                }

                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text(text = "Настроить") },
                        onClick = {
                            println("Настроить " + deviceInfo.deviceId)
                            showMenu = false
                        }
                    )

                    DropdownMenuItem(
                        text = { Text(text = "Удалить") },
                        onClick = {
                            println("Удалить " + deviceInfo.deviceId)
                            showMenu = false
                        }
                    )
                }
            }
        }

        Row {
            Column {
                Text(
                    text = String.format("%s: %s", deviceInfo.deviceId.toString(), deviceInfo.deviceName)
                )

                Text(
                    text = String.format("%s", deviceInfo.roomName)
                )
            }

            Box (
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.BottomEnd
            ) {
                Button(
                    onClick = {
                        println(deviceInfo.deviceId)
                    },
                    colors = ButtonDefaults.buttonColors(
                        MaterialTheme.colorScheme.primaryContainer,
                        Color.Black,
                        MaterialTheme.colorScheme.primaryContainer,
                        Color.Gray
                    )
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_power_settings_new_24),
                        contentDescription = null
                    )
                }
            }
        }

    }
}

@Composable
fun Main() {
    var showRoomMenu by remember { mutableStateOf(false) }

    MADLabTheme {
        Scaffold (
            topBar = {
                Row(
                    modifier = Modifier
                        .background(color = MaterialTheme.colorScheme.primaryContainer)
                        .padding(Dp(5f))
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    verticalAlignment = Alignment.CenterVertically
                )
                {
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .weight(weight = 1f, fill = false),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        items(1) { _ ->
                            Text(
                                text = "Весь дом",
                                style = MainPageRoomStyle,
                                modifier = Modifier
                                    .padding(horizontal = Dp(5f))
                            )
                        }
                    }

                    Row(
                        modifier = Modifier
                            .wrapContentWidth()
                            .wrapContentHeight(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box {
                            IconButton(
                                onClick = {
                                    showRoomMenu = !showRoomMenu
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Menu,
                                    contentDescription = null
                                )
                            }

                            DropdownMenu(
                                expanded = showRoomMenu,
                                onDismissRequest = { showRoomMenu = false }
                            ) {

                                DropdownMenuItem(
                                    text = { Text(text = "Добавить комнату") },
                                    enabled = true,
                                    onClick = { showRoomMenu = false }
                                )

                                DropdownMenuItem(
                                    text = { Text(text = "Удалить комнату") },
                                    enabled = false,
                                    onClick = { showRoomMenu = false }
                                )
                            }
                        }
                    }
                }
            },

            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        println(123)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null
                    )
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LazyColumn(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                        .fillMaxWidth()
                        .weight(1f, false),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(generateRandom()) { info ->
                        DeviceListItem(deviceInfo = info)
                    }

                    items(1) { _ ->
                        Box(
                            modifier = Modifier
                                .height(Dp(200f))
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainPreview() {
    Main()
}
