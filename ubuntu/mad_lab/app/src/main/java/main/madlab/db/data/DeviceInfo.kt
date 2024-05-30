package main.madlab.db.data

import androidx.annotation.DrawableRes

data class DeviceInfo(
    var deviceId: Int,
    @DrawableRes var imgId: Int,
    var deviceName: String,
    var roomId: Int?,
    var roomName: String?
)