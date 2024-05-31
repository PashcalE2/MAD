package main.madlab.db.data

data class Room(
    var id: Int,
    var name: String
)

private val definedRooms = listOf(
    Room(1, "Гостинная"),
    Room(2, "Кухня")
)

fun getDefinedRooms(): List<Room> {
    return definedRooms
}
