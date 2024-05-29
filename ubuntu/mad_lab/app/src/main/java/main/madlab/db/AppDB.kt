package main.madlab.db

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import main.madlab.db.data.Room


class AppDB(private val context: Context, private val factory: SQLiteDatabase.CursorFactory?) : SQLiteOpenHelper(context, DB_NAME, factory, DB_VERSION) {
    companion object {
        private const val DB_NAME = "MAD db"
        private const val DB_VERSION = 1

        private object RoomMeta {
            const val tableName = "room"

            const val id = "id"
            const val name = "name"
        }

        private object DeviceMeta {
            const val tableName = "device"

            const val id = "id"
            const val roomId = "roomId"
            const val typeId = "typeId"
            const val name = "name"
        }

        private object DeviceTypeMeta {
            const val tableName = "devicetype"

            const val id = "id"
            const val name = "name"
            const val imgId = "imgId"
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

    @SuppressLint("Range")
    fun getAllRooms(): List<Room> {
        val roomList = mutableListOf<Room>()
        val selectQuery = "select * from ${RoomMeta.tableName}"
        val db = this.readableDatabase
        val cursor: Cursor?

        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: Exception) {
            return emptyList()
        }

        cursor?.use {
            if (it.moveToFirst()) {
                do {
                    val id = it.getInt(it.getColumnIndex(RoomMeta.id))
                    val name = it.getString(it.getColumnIndex(RoomMeta.name))
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

}
