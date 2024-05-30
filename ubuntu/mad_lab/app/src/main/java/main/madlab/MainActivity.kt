package main.madlab

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import main.madlab.composable.AddDevice
import main.madlab.composable.Main

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NavigationSystem(LocalContext.current)
        }
    }
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
    }
}

