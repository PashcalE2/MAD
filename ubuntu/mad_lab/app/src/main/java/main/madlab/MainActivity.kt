package main.madlab

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import main.madlab.composable.AddDevice
import main.madlab.composable.AddRoom
import main.madlab.composable.Main
import main.madlab.composable.UpdateDevice
import main.madlab.composable.UpdateRoom
import main.madlab.db.data.Device
import main.madlab.db.data.Room

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NavigationSystem(LocalContext.current)
        }
    }
}

fun updateDeviceRoute(device: Device): String {
    return "updateDevice/{${device.id}}/{${device.roomId}}/{${device.typeId}}/{${device.name}}"
}

fun updateRoomRoute(room: Room): String {
    return "updateRoom/{${room.id}}/{${room.name}}"
}

@Composable
fun NavigationSystem(context: Context) {
    val navController = rememberNavController()
    val vm = AppViewModel(context)

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") { Main(navController = navController, vm = vm) }

        composable("addDevice") { AddDevice(navController = navController, vm = vm) }

        composable(
            "updateDevice/{deviceId}/{roomId}/{typeId}/{name}",
            arguments = listOf(
                navArgument("deviceId") {
                    type = NavType.IntType
                },
                navArgument("roomId") {
                    type = NavType.IntType
                },
                navArgument("typeId") {
                    type = NavType.IntType
                },
                navArgument("name") {
                    type = NavType.StringType
                }
            )
        ) {
            UpdateDevice(
                device = Device(
                    it.arguments?.getInt("deviceId")!!,
                    it.arguments?.getInt("roomId")!!,
                    it.arguments?.getInt("typeId")!!,
                    it.arguments?.getString("name")!!
                ),
                navController = navController,
                vm = vm
            )
        }

        composable("addRoom") { AddRoom(navController = navController, vm = vm) }

        composable(
            "updateRoom/{roomId}/{name}",
            arguments = listOf(
                navArgument("roomId") {
                    type = NavType.IntType
                },
                navArgument("name") {
                    type = NavType.StringType
                }
            )
        ) {
            UpdateRoom(
                room = Room(
                    it.arguments?.getInt("roomId")!!,
                    it.arguments?.getString("name")!!
                ),
                navController = navController,
                vm = vm
            )
        }
    }
}

