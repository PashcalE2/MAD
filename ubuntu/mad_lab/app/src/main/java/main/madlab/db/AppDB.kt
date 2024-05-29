package main.madlab.db

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class AppDB(private val context: Context, private val factory: SQLiteDatabase.CursorFactory?) : SQLiteOpenHelper(context, DB_NAME, factory, DB_VERSION) {
    companion object {
        private const val DB_NAME = "MAD db"
        private const val DB_VERSION = 1

        private const val ROOM_TABLE_NAME = "room"
        private const val ROOM_PK = "id"
        private const val ROOM_NAME_ATR = "name"

        private const val DEVICE_TABLE_NAME = "device"
        private const val DEVICE_PK = "id"
        private const val DEVICE_ROOM_FK = "room_id"
        private const val DEVICE_NAME_ATR = "name"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createRoomTableQuery = "create table $ROOM_TABLE_NAME (" +
                "$ROOM_PK int primary key, " +
                "$ROOM_NAME_ATR text not null" +
                ");"

        val createDeviceTableQuery = "create table $DEVICE_TABLE_NAME (" +
                "$DEVICE_PK int primary key, " +
                "$DEVICE_ROOM_FK int not null," +
                "$DEVICE_NAME_ATR text not null," +
                "foreign key ($DEVICE_ROOM_FK) references $ROOM_TABLE_NAME($ROOM_PK)" +
                ");"


        db?.execSQL(createRoomTableQuery)
        db?.execSQL(createDeviceTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("drop table if exists $DEVICE_TABLE_NAME;")
        db?.execSQL("drop table if exists $ROOM_TABLE_NAME;")
        onCreate(db)
    }

    fun addRoom(room: Room) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(ROOM_PK, room.name)
        }
        db.insert(ROOM_TABLE_NAME, null, values)
        db.close()
    }

    @SuppressLint("Range")
    fun getAllRooms(): List<Room> {
        val roomList = mutableListOf<Room>()
        val selectQuery = "select * from $ROOM_TABLE_NAME"
        val db = this.readableDatabase
        val cursor: Cursor?

        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: Exception) {
            db.execSQL(selectQuery)
            return emptyList()
        }

        cursor?.use {
            if (it.moveToFirst()) {
                do {
                    val id = it.getInt(it.getColumnIndex(ROOM_PK))
                    val name = it.getString(it.getColumnIndex(ROOM_NAME_ATR))
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
        val result = db.delete(ROOM_TABLE_NAME, "$ROOM_PK = ?", arrayOf(roomId.toString()))
        db.close()
        return result
    }

}
