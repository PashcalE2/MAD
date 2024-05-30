package main.madlab.db.data

data class Room(
    var id: Int,
    var name: String
)

private val definedRooms = listOf(
    Room(0, "Гостинная"),
    Room(1, "Кухня")
)

fun getDefinedRooms(): List<Room> {
    return definedRooms
}
