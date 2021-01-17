package com.roadmate.exe.api.response

import com.google.gson.annotations.SerializedName

data class ShopListMaster(
    @SerializedName("error") val error: String,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: ArrayList<ShopListTrans>
)