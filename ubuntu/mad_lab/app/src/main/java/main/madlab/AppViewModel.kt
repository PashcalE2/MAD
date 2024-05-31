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

    fun addRoom(roomName: String) {
        db.addRoom(Room(-1, roomName))
    }

    fun updateRoom(room: Room) {
        db.updateRoom(room)
    }

    fun deleteRoom(roomId: Int): Int {
        val result = db.deleteRoom(roomId)
        println("Deleted room $result")

        return result
    }

    fun getAllDevicesInfo(roomId: Int): List<DeviceInfo> {
        if (roomId == ALL_ROOMS_ID) {
            return db.getAllDevicesInfo(null)
        }

        return db.getAllDevicesInfo(roomId)
    }

    fun getAllDeviceTypes(): List<DeviceType> {
        return db.getAllDeviceTypes()
    }

    fun addDevice(deviceName: String, roomId: Int, typeId: Int) {
        if (roomId == ALL_ROOMS_ID) {
            db.addDevice(Device(-1, null, typeId, deviceName))
        }
        else {
            db.addDevice(Device(-1, roomId, typeId, deviceName))
        }
    }

    fun updateDevice(device: Device) {
        db.updateDevice(device)
    }

    fun deleteDevice(deviceId: Int): Int {
        val result = db.deleteDevice(deviceId)
        println("Deleted device $result")

        return result
    }


}