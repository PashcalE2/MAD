package main.madlab.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import main.madlab.db.data.DeviceInfo
import main.madlab.db.data.Room


class AppDB(private val context: Context, private val factory: SQLiteDatabase.CursorFactory?) : SQLiteOpenHelper(context, DB_NAME, factory, DB_VERSION) {
    companion object {
        private const val DB_NAME = "MAD db"
        private const val DB_VERSION = 1

        private object RoomMeta {
            const val tableName = "room"

            const val id = "id"
            const val idFull = tableName + "." + id

            const val name = "name"
            const val nameFull = tableName + "." + name
        }

        private object DeviceMeta {
            const val tableName = "device"

            const val id = "id"
            const val idFull = tableName + "." + id

            const val roomId = "roomId"
            const val roomIdFull = tableName + "." + roomId

            const val typeId = "typeId"
            const val typeIdFull = tableName + "." + typeId

            const val name = "name"
            const val nameFull = tableName + "." + name
        }

        private object DeviceTypeMeta {
            const val tableName = "devicetype"

            const val id = "id"
            const val idFull = tableName + "." + id

            const val name = "name"
            const val nameFull = tableName + "." + name

            const val imgId = "imgId"
            const val imgIdFull = tableName + "." + imgId
        }

        private object DeviceInfoMeta {
            const val deviceId = "deviceId"
            const val imgId = "imgId"
            const val deviceName = "deviceName"
            const val roomName = "roomName"
        }
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createRoomTableQuery = "create table ${RoomMeta.tableName} (" +
                "${RoomMeta.id} int primary key, " +
                "${RoomMeta.name} text not null" +
                ");"

        val createDeviceTableQuery = "create table ${DeviceMeta.tableName} (" +
                "${DeviceMeta.id} int primary key, " +
                "${DeviceMeta.roomId} int not null," +
                "${DeviceMeta.typeId} int not null," +
                "${DeviceMeta.name} text not null," +
                "foreign key (${DeviceMeta.roomId}) references ${RoomMeta.tableName}(id)," +
                "foreign key (${DeviceMeta.typeId}) references ${DeviceMeta.tableName}(id)" +
                ");"


        db?.execSQL(createRoomTableQuery)
        db?.execSQL(createDeviceTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("drop table if exists ${DeviceMeta.tableName};")
        db?.execSQL("drop table if exists ${DeviceTypeMeta.tableName};")
        db?.execSQL("drop table if exists ${RoomMeta.tableName};")
        onCreate(db)
    }

    fun addRoom(room: Room) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("id", room.name)
        }
        db.insert("room", null, values)
        db.close()
    }

    fun getAllRooms(): List<Room> {
        val roomList = mutableListOf<Room>()
        val db = this.readableDatabase
        val cursor: Cursor?

        try {
            cursor = db.rawQuery("select * from ${RoomMeta.tableName}", null)
        } catch (e: Exception) {
            return emptyList()
        }

        cursor?.use {
            if (it.moveToFirst()) {
                do {
                    val id = it.getInt(it.getColumnIndexOrThrow(RoomMeta.id))
                    val name = it.getString(it.getColumnIndexOrThrow(RoomMeta.name))

                    val room = Room(id, name)
                    roomList.add(room)
                } while (it.moveToNext())
            }
        }

        db.close()
        return roomList
    }

    fun deleteRoom(roomId: Int): Int {
        val db = writableDatabase
        val result = db.delete(RoomMeta.tableName, "${RoomMeta.id} = ?", arrayOf(roomId.toString()))
        db.close()
        return result
    }

    fun getAllDevicesInfo(): List<DeviceInfo> {
        val devicesInfo = mutableListOf<DeviceInfo>()

        val db = this.readableDatabase
        val cursor: Cursor?

        try {
            cursor = db.rawQuery("select " +
                    "${DeviceMeta.idFull} as ${DeviceInfoMeta.deviceId}, " +
                    "${DeviceTypeMeta.imgIdFull} as ${DeviceInfoMeta.imgId}, " +
                    "${DeviceMeta.nameFull} as ${DeviceInfoMeta.deviceName}, " +
                    "${RoomMeta.nameFull} as ${DeviceInfoMeta.roomName} " +
                    "from ${DeviceMeta.tableName}" +
                    "inner join ${DeviceTypeMeta.tableName} on ${DeviceMeta.idFull} = ${DeviceTypeMeta.idFull} " +
                    "inner join room on ${DeviceMeta.roomIdFull} = ${RoomMeta.idFull}", null
            )
        } catch (e: Exception) {
            return emptyList()
        }

        cursor?.use {
            if (it.moveToFirst()) {
                do {
                    val id = it.getInt(it.getColumnIndexOrThrow(DeviceInfoMeta.deviceId))
                    val imgId = it.getInt(it.getColumnIndexOrThrow(DeviceInfoMeta.imgId))
                    val deviceName = it.getString(it.getColumnIndexOrThrow(DeviceInfoMeta.deviceName))
                    val roomName = it.getString(it.getColumnIndexOrThrow(DeviceInfoMeta.roomName))

                    val deviceInfo = DeviceInfo(id, imgId, deviceName, roomName)
                    devicesInfo.add(deviceInfo)
                } while (it.moveToNext())
            }
        }

        db.close()
        return devicesInfo
    }

}
