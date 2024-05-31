package main.madlab.composable

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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import main.madlab.AppViewModel
import main.madlab.db.data.DeviceInfo
import main.madlab.db.data.Room
import main.madlab.ui.theme.MADLabTheme

@Composable
fun DeviceListItem(navController: NavController?, vm: AppViewModel?, deviceInfo: DeviceInfo, onDelete: () -> Unit) {
    var showMenu by remember { mutableStateOf(false) }

    println("MAIN: PLACED ${deviceInfo.deviceName}, ${deviceInfo.imgId}")

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
                    .width(300.dp)
                    .height(200.dp)
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

                            navController!!.currentBackStackEntry!!.arguments!!.putInt("deviceId", deviceInfo.deviceId)
                            navController.currentBackStackEntry!!.arguments!!.putInt("roomId", if (deviceInfo.roomId == null) vm!!.ALL_ROOMS_ID else deviceInfo.roomId!!)
                            navController.currentBackStackEntry!!.arguments!!.putInt("typeId", deviceInfo.deviceTypeId)
                            navController.currentBackStackEntry!!.arguments!!.putString("name", deviceInfo.deviceName)
                            navController.navigate("updateDevice")

                            showMenu = false
                        }
                    )


                    DropdownMenuItem(
                        text = { Text(text = "Удалить") },
                        onClick = {
                            println("Удалить " + deviceInfo.deviceId)
                            vm!!.deleteDevice(deviceInfo.deviceId)
                            onDelete()

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
                    text = String.format("%s", if (deviceInfo.roomName == null) vm!!.ALL_ROOMS_NAME else deviceInfo.roomName)
                )
            }

            /*
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
            */
        }
    }
}

@Composable
fun RoomSelectionButton(room: Room, onClickWhenNotSelected: (Room) -> Unit, onClickWhenSelected: (Room) -> Unit, selectedRoomId: Int) {
    var selected by remember { mutableStateOf(false) }

    selected = selectedRoomId == room.id

    ElevatedButton(
        onClick = {
            if (selected) {
                onClickWhenSelected(room)
            }
            else {
                onClickWhenNotSelected(room)
            }
        },

        colors = ButtonDefaults.buttonColors(
            containerColor = when (selected) {
                true -> Color.Blue
                else -> Color.White
            }
        ),
        modifier = Modifier
            .padding(horizontal = Dp(5f))
    ) {
        Text(
            text = room.name,
            style = MaterialTheme.typography.bodyLarge,
            color = when (selected) {
                true -> Color.White
                else -> Color.Black
            }
        )
    }
}

@Composable
fun Main(navController: NavController?, vm: AppViewModel?) {
    var showRoomMenu by remember { mutableStateOf(false) }
    var updateTrigger by remember { mutableStateOf(false) }
    var selectedRoom by remember { mutableStateOf(Room(vm!!.ALL_ROOMS_ID, vm.ALL_ROOMS_NAME)) }
    var deleteRoomEnabled by remember { mutableStateOf(false) }

    val devicesInfo: List<DeviceInfo> = vm!!.getAllDevicesInfo(selectedRoom.id)
    println("MAIN: DEVICES COUNT NOW: ${devicesInfo.count()}, ${vm.getAllDevicesInfo(selectedRoom.id).count()}")

    key (updateTrigger) {
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
                                RoomSelectionButton(
                                    room = Room(vm.ALL_ROOMS_ID, vm.ALL_ROOMS_NAME),
                                    onClickWhenNotSelected = {
                                        selectedRoom.id = it.id
                                        deleteRoomEnabled = false

                                        updateTrigger = !updateTrigger
                                    },
                                    onClickWhenSelected = {
                                    },
                                    selectedRoomId = selectedRoom.id
                                )
                            }

                            items(vm.getAllRooms()) { room ->
                                RoomSelectionButton(
                                    room = room,
                                    onClickWhenNotSelected = {
                                        selectedRoom.id = it.id
                                        selectedRoom.name = it.name
                                        deleteRoomEnabled = true

                                        updateTrigger = !updateTrigger
                                    },
                                    onClickWhenSelected = {
                                        navController!!.currentBackStackEntry!!.arguments!!.putInt("roomId", selectedRoom.id)
                                        navController.currentBackStackEntry!!.arguments!!.putString("name", selectedRoom.name)
                                        navController.navigate("updateRoom")
                                    },
                                    selectedRoomId = selectedRoom.id
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
                                        onClick = {
                                            navController?.navigate("addRoom")
                                            showRoomMenu = false
                                        }
                                    )

                                    DropdownMenuItem(
                                        text = { Text(text = "Удалить комнату") },
                                        enabled = deleteRoomEnabled,
                                        onClick = {
                                            vm.deleteRoom(selectedRoom.id)
                                            showRoomMenu = false

                                            selectedRoom.id = vm.ALL_ROOMS_ID
                                            selectedRoom.name = vm.ALL_ROOMS_NAME

                                            updateTrigger = !updateTrigger
                                        }
                                    )
                                }
                            }
                        }
                    }
                },

                floatingActionButton = {
                    FloatingActionButton(
                        onClick = {
                            navController?.navigate("addDevice")
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

                        println("MAIN: UPDATE DEVICES INFO")

                        itemsIndexed(devicesInfo) { _, info ->
                            DeviceListItem(
                                navController = navController,
                                vm = vm,
                                deviceInfo = info,
                                onDelete = {
                                    updateTrigger = !updateTrigger
                                }
                            )
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
}

@Preview(showBackground = true)
@Composable
fun MainPreview() {
    Main(null, null)
}


