package com.roadmate.exe.api.response

import com.google.gson.annotations.SerializedName
import com.roadmate.exe.api.response.MoreServicesTrans

data class MoreServicesMaster(
    @SerializedName("error") val error: String,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: ArrayList<MoreServicesTrans>
)