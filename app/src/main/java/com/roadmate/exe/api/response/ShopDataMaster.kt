package com.roadmate.exe.api.response

import com.google.gson.annotations.SerializedName

data class ShopDataMaster(
    @SerializedName("error") val error: String,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: ArrayList<ShopDataTrans>
)