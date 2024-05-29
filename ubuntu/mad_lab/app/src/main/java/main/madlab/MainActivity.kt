package main.madlab

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class MainActivity : AppCompatActivity() {
    private lateinit var addDeviceButton: Button
    private lateinit var devicesLayout: LinearLayout
    private var devicesList = mutableListOf<View>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.main_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        devicesLayout = findViewById(R.id.devices_layout)

        addDeviceButton = findViewById(R.id.add_device_button)
        addDeviceButton.setOnClickListener { _ ->
            val addDeviceActivity = Intent(this, AddDeviceActivity::class.java)
            addDeviceActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(addDeviceActivity)
        }
    }
}


/*

            val newDeviceLayout: ViewGroup = FrameLayout(devicesLayout.context)
            val newDevice: View = LayoutInflater.from(newDeviceLayout.context).inflate(R.layout.device_layout, newDeviceLayout, true)

            devicesList.add(newDeviceLayout)
            devicesLayout.addView(newDeviceLayout)

            println(devicesLayout.childCount)

 */