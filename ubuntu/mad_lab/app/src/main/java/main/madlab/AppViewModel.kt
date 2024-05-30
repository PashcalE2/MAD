package main.madlab

import android.content.Context
import androidx.lifecycle.ViewModel
import main.madlab.db.AppDB
import main.madlab.db.data.Device
import main.madlab.db.data.DeviceInfo
import main.madlab.db.data.DeviceType
import main.madlab.db.data.Room

class AppViewModel(context: Context): ViewModel() {
    private val db: AppDB = AppDB(context = context)

    val ALL_ROOMS_ID = -1
    val ALL_ROOMS_NAME = "Весь дом"

    fun getAllRooms(): List<Room> {
        return db.getAllRooms()
    }

    fun getAllDevicesInfo(roomId: Int): List<DeviceInfo> {
        if (roomId < 0) {
            return db.getAllDevicesInfo(null)
        }

        return db.getAllDevicesInfo(roomId)
    }

    fun getAllDeviceTypes(): List<DeviceType> {
        return db.getAllDeviceTypes()
    }

    fun addDevice(deviceName: String, roomId: Int, typeId: Int) {
        if (roomId < 0) {
            db.addDevice(Device(-1, null, typeId, deviceName))
        }
        else {
            db.addDevice(Device(-1, roomId, typeId, deviceName))
        }
    }

    fun removeDevice(deviceId: Int): Int {
        val result = db.removeDevice(deviceId)
        println("Removed $result")

        return result
    }


}