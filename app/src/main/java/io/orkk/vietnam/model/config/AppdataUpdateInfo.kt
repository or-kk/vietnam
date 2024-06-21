package io.orkk.vietnam.model.config

import com.google.gson.annotations.SerializedName

data class AppdataUpdateInfo(
    @SerializedName("club_index")
    val clubIndex: String,
    @SerializedName("download_file_list")
    val downloadFileList: String
)
