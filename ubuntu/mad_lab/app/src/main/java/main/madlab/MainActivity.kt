package main.madlab

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private lateinit var button: Button
    private lateinit var devicesLayout: LinearLayout
    private var devicesList = mutableListOf<View>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        devicesLayout = findViewById(R.id.devices_layout)

        button = findViewById(R.id.add_button)
        button.setOnClickListener { _ ->
            val newDeviceLayout: ViewGroup = FrameLayout(devicesLayout.context)
            val newDevice: View = LayoutInflater.from(newDeviceLayout.context).inflate(R.layout.device, newDeviceLayout, true)

            devicesList.add(newDeviceLayout)
            devicesLayout.addView(newDeviceLayout)

            println(devicesLayout.childCount)
        }
    }
}
