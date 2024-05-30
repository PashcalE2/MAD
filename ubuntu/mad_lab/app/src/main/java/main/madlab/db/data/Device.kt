package main.madlab.db.data

data class Device(
    var id: Int,
    var roomId: Int?,
    var typeId: Int,
    var name: String
)

private val definedDevices = listOf(
    Device(0, null, 0, "Жаробор"),
    Device(1, 0, 3, "Лупа"),
    Device(2, 1, 3, "Пупа")
)

fun getDefinedDevices(): List<Device> {
    return definedDevices
}
