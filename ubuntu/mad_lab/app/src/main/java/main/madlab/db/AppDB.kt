package main.madlab.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import main.madlab.db.data.Device
import main.madlab.db.data.DeviceInfo
import main.madlab.db.data.DeviceType
import main.madlab.db.data.Room
import main.madlab.db.data.getDefinedDeviceTypes
import main.madlab.db.data.getDefinedDevices
import main.madlab.db.data.getDefinedRooms


class AppDB(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
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
            const val roomId = "roomId"
            const val roomName = "roomName"
            const val deviceTypeId = "deviceTypeId"
        }
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createRoomTableQuery = "create table ${RoomMeta.tableName} (" +
                "${RoomMeta.id} integer primary key autoincrement, " +
                "${RoomMeta.name} text not null " +
                ");"

        val createDeviceTypeTableQuery = "create table ${DeviceTypeMeta.tableName} (" +
                "${DeviceTypeMeta.id} integer primary key autoincrement, " +
                "${DeviceTypeMeta.name} text not null, " +
                "${DeviceTypeMeta.imgId} integer not null " +
                ");"

        val createDeviceTableQuery = "create table ${DeviceMeta.tableName} (" +
                "${DeviceMeta.id} integer primary key autoincrement, " +
                "${DeviceMeta.roomId} integer, " +
                "${DeviceMeta.typeId} integer not null, " +
                "'${DeviceMeta.name}' text not null, " +
                "foreign key (${DeviceMeta.roomId}) references ${RoomMeta.tableName}(id), " +
                "foreign key (${DeviceMeta.typeId}) references ${DeviceTypeMeta.tableName}(id) " +
                ");"

        db?.execSQL(createRoomTableQuery)
        db?.execSQL(createDeviceTypeTableQuery)
        db?.execSQL(createDeviceTableQuery)

        getDefinedRooms().forEach {room ->
            db?.execSQL("insert into ${RoomMeta.tableName} values (${room.id}, '${room.name}')")
        }

        getDefinedDeviceTypes().forEach { deviceType ->
            db?.execSQL("insert into ${DeviceTypeMeta.tableName} values (${deviceType.id}, '${deviceType.name}', ${deviceType.imgId})")
        }

        getDefinedDevices().forEach { device ->
            db?.execSQL("insert into ${DeviceMeta.tableName} values (${device.id}, ${device.roomId}, ${device.typeId}, '${device.name}')")
        }

        println("DB: CREATED")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("drop table if exists ${DeviceMeta.tableName};")
        db?.execSQL("drop table if exists ${DeviceTypeMeta.tableName};")
        db?.execSQL("drop table if exists ${RoomMeta.tableName};")

        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("drop table if exists ${DeviceMeta.tableName};")
        db?.execSQL("drop table if exists ${DeviceTypeMeta.tableName};")
        db?.execSQL("drop table if exists ${RoomMeta.tableName};")

        onCreate(db)
    }

    override fun onOpen(db: SQLiteDatabase?) {
        super.onOpen(db)
    }

    fun getAllRooms(): List<Room> {
        val roomList = mutableListOf<Room>()
        val db = readableDatabase
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
                    println("SELECT * FROM ROOM: found: $id, $name")

                    roomList.add(room)
                } while (it.moveToNext())
            }
        }

        db.close()
        return roomList
    }

    fun addRoom(room: Room) {
        val db = writableDatabase
        val values = ContentValues()

        values.put(RoomMeta.name, room.name)

        db.insertOrThrow(RoomMeta.tableName, null, values)
        db.close()
    }

    fun updateRoom(room: Room) {
        val db = writableDatabase
        val values = ContentValues()

        values.put(RoomMeta.name, room.name)

        db.update(RoomMeta.tableName, values, "${RoomMeta.id} = ?", arrayOf(room.id.toString()))
        db.close()
    }

    fun deleteRoom(roomId: Int): Int {
        val db = writableDatabase

        db.execSQL("update ${DeviceMeta.tableName} set ${DeviceMeta.roomId} = null where ${DeviceMeta.roomId} = $roomId")
        val result = db.delete(RoomMeta.tableName, "${RoomMeta.id} = ?", arrayOf(roomId.toString()))

        db.close()
        return result
    }

    fun getAllDeviceTypes(): List<DeviceType> {
        val typeList = mutableListOf<DeviceType>()
        val db = readableDatabase
        val cursor: Cursor?

        try {
            cursor = db.rawQuery("select * from ${DeviceTypeMeta.tableName}", null)
        } catch (e: Exception) {
            return emptyList()
        }

        cursor?.use {
            if (it.moveToFirst()) {
                do {
                    val id = it.getInt(it.getColumnIndexOrThrow(DeviceTypeMeta.id))
                    val name = it.getString(it.getColumnIndexOrThrow(DeviceTypeMeta.name))
                    val imgId = it.getInt(it.getColumnIndexOrThrow(DeviceTypeMeta.imgId))

                    val type = DeviceType(id, name, imgId)
                    typeList.add(type)
                } while (it.moveToNext())
            }
        }

        db.close()
        return typeList
    }

    fun getAllDevicesInfo(roomId: Int?): List<DeviceInfo> {
        val devicesInfo = mutableListOf<DeviceInfo>()

        val db = readableDatabase
        val cursor: Cursor?

        val whereClause = when (roomId) {
            null -> ""
            else -> "where ${RoomMeta.idFull} = $roomId"
        }

        try {
            cursor = db.rawQuery(
                "select " +
                        "${DeviceMeta.idFull} as ${DeviceInfoMeta.deviceId}, " +
                        "${DeviceTypeMeta.imgIdFull} as ${DeviceInfoMeta.imgId}, " +
                        "${DeviceMeta.nameFull} as ${DeviceInfoMeta.deviceName}, " +
                        "${RoomMeta.idFull} as ${DeviceInfoMeta.roomId}, " +
                        "${RoomMeta.nameFull} as ${DeviceInfoMeta.roomName}, " +
                        "${DeviceMeta.typeIdFull} as ${DeviceInfoMeta.deviceTypeId} " +
                        "from ${DeviceMeta.tableName} " +
                        "inner join ${DeviceTypeMeta.tableName} on ${DeviceMeta.typeIdFull} = ${DeviceTypeMeta.idFull} " +
                        "left join room on ${DeviceMeta.roomIdFull} = ${RoomMeta.idFull} " +
                        whereClause,
                null
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

                    val deviceRoomId: Int? = try {
                        it.getInt(it.getColumnIndexOrThrow(DeviceInfoMeta.roomId))
                    } catch (e: Exception) {
                        null
                    }

                    val roomName = it.getString(it.getColumnIndexOrThrow(DeviceInfoMeta.roomName))
                    val deviceTypeId = it.getInt(it.getColumnIndexOrThrow(DeviceInfoMeta.deviceTypeId))

                    val deviceInfo = DeviceInfo(id, imgId, deviceName, deviceRoomId, roomName, deviceTypeId)
                    devicesInfo.add(deviceInfo)
                } while (it.moveToNext())
            }
        }

        db.close()
        return devicesInfo
    }

    fun addDevice(device: Device) {
        val db = writableDatabase
        val values = ContentValues()

        values.put(DeviceMeta.roomId, device.roomId)
        values.put(DeviceMeta.typeId, device.typeId)
        values.put(DeviceMeta.name, device.name)

        db.insertOrThrow(DeviceMeta.tableName, null, values)

        println("DB: INSERT: ${device.roomId}, ${device.name}, ${device.typeId}")

        db.close()
    }

    fun updateDevice(device: Device) {
        val db = writableDatabase
        val values = ContentValues()

        values.put(DeviceMeta.roomId, device.roomId)
        values.put(DeviceMeta.typeId, device.typeId)
        values.put(DeviceMeta.name, device.name)

        db.update(DeviceMeta.tableName, values, "${DeviceMeta.id} = ?", arrayOf(device.id.toString()))
        db.close()
    }

    fun deleteDevice(deviceId: Int): Int {
        val db = writableDatabase
        val result = db.delete(DeviceMeta.tableName, "${DeviceMeta.id} = ?", arrayOf(deviceId.toString()))
        db.close()
        return result
    }

}
