package io.orkk.vietnam.model.player

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Player(
    var displaySlotNumber: Int? = 0,
    var isDisplayVisible: Boolean? = true,
    var checkInNumber: String? = "",
    var name: String? = "",
    var sex: Int? = 1,
    var member: String? = "N",
    var phone: String? = "",
    var isCheckIn: Boolean? = false,
) : Parcelable