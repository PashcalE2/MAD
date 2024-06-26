package main.madlab.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import main.madlab.AppViewModel
import main.madlab.db.data.Room
import main.madlab.ui.theme.MADLabTheme

@Composable
fun UpdateRoom(room: Room, navController: NavController?, vm: AppViewModel?) {
    var roomName by remember { mutableStateOf(room.name + "") }

    MADLabTheme {
        Scaffold(
            topBar = {
                Row(
                    modifier = Modifier
                        .background(color = MaterialTheme.colorScheme.primaryContainer)
                        .padding(top = Dp(25f))
                        .padding(Dp(5f))
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            navController?.navigate("home")
                        }
                    ) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                    }

                    Text(text = "Настройка комнаты: ${room.id}. ${room.name}", style = MaterialTheme.typography.headlineSmall)
                }
            }
        ) { innerPadding ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(Dp(40f))
            ) {

                // input room name
                TextField(
                    value = roomName,
                    onValueChange = {
                        roomName = it
                    },
                    label = {
                        Text(text = "Название")
                    },
                    modifier = Modifier
                        .padding(bottom = 20.dp)
                )

                Box(
                    contentAlignment = Alignment.BottomCenter,
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    ElevatedButton(
                        onClick = {
                            room.name = roomName

                            vm?.updateRoom(room)
                            navController?.navigate("home")
                        },

                        modifier = Modifier
                            .fillMaxWidth(0.75f)
                    ) {
                        Text(text = "Изменить", style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun UpdateRoomPreview() {
    UpdateRoom(Room(-1, ""), null, null)
}