package main.madlab.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import main.madlab.db.data.Device
import main.madlab.db.data.DeviceType
import main.madlab.db.data.Room
import main.madlab.ui.theme.MADLabTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateDevice(device: Device, navController: NavController?, vm: AppViewModel?) {
    var deviceName by remember { mutableStateOf(device.name + "") }

    var deviceRoomExpanded by remember { mutableStateOf(false) }
    val deviceRoom by remember { mutableStateOf(Room(
        if (device.roomId == null) vm!!.ALL_ROOMS_ID else device.roomId!!,
        if (device.roomId == null) vm!!.ALL_ROOMS_NAME else vm!!.getAllRooms().stream().filter { it.id == device.roomId }.findFirst().get().name
        )
    ) }

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

                    Text(text = "Настройка устройства: ${device.id}. ${device.name}", style = MaterialTheme.typography.headlineSmall)
                }
            }
        ) { innerPadding ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(Dp(40f))
            ) {

                // input device name
                TextField(
                    value = deviceName,
                    onValueChange = {
                        deviceName = it
                    },
                    label = {
                        Text(text = "Имя устройства")
                    },
                    modifier = Modifier
                        .padding(bottom = 20.dp)
                )

                // select device room
                ExposedDropdownMenuBox(
                    expanded = deviceRoomExpanded,
                    onExpandedChange = {
                        deviceRoomExpanded = !deviceRoomExpanded
                    },
                    modifier = Modifier
                        .padding(bottom = 20.dp)
                ) {
                    TextField(
                        value = deviceRoom.name,
                        label = { Text(text = "Комната") },
                        onValueChange = {
                            deviceRoom.name = it
                        },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = deviceRoomExpanded) },
                        modifier = Modifier
                            .menuAnchor()
                    )

                    ExposedDropdownMenu(
                        expanded = deviceRoomExpanded,
                        onDismissRequest = {
                            deviceRoomExpanded = false
                        }
                    ) {
                        LazyColumn (
                            modifier = Modifier
                                .width(200.dp)
                                .height(200.dp)
                        ) {
                            items(1) {
                                DropdownMenuItem(
                                    text = { Text(text = vm!!.ALL_ROOMS_NAME) },
                                    onClick = {
                                        deviceRoom.id = vm!!.ALL_ROOMS_ID
                                        deviceRoom.name = vm.ALL_ROOMS_NAME

                                        deviceRoomExpanded = false
                                    },
                                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                                )
                            }

                            items(vm!!.getAllRooms()) {
                                DropdownMenuItem(
                                    text = { Text(text = it.name) },
                                    onClick = {
                                        deviceRoom.id = it.id
                                        deviceRoom.name = it.name

                                        deviceRoomExpanded = false
                                    },
                                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                                )
                            }
                        }
                    }
                }


                Box(
                    contentAlignment = Alignment.BottomCenter,
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    ElevatedButton(
                        onClick = {
                            device.roomId = deviceRoom.id
                            device.name = deviceName

                            vm?.updateDevice(device)
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
fun UpdateDevicePreview() {
    UpdateDevice(Device(-1, 0, 0, ""), null, null)
}