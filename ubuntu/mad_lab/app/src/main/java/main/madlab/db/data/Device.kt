package main.madlab.db.data

data class Device(
    var id: Int,
    var roomId: Int?,
    var typeId: Int,
    var name: String
)

private val definedDevices = listOf(
    Device(1, null, 1, "Жаробор"),
    Device(2, 1, 4, "Лупа"),
    Device(3, 2, 4, "Пупа")
)

fun getDefinedDevices(): List<Device> {
    return definedDevices
}
