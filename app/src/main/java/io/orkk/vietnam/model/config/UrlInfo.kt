package io.orkk.vietnam.model.config

import com.google.gson.annotations.SerializedName

data class UrlInfo(
    @SerializedName("club_index")
    val clubIndex: String,
    @SerializedName("download_url")
    val downloadUrl: String,
)