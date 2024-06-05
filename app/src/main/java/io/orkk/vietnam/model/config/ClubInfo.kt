package io.orkk.vietnam.model.config

import com.google.gson.annotations.SerializedName

data class ClubInfo(
    @SerializedName("club_index")
    val clubIndex: String,
    @SerializedName("club_name")
    val clubName: String,
)
