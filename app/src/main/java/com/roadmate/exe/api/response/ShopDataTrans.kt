package com.roadmate.exe.api.response

import com.google.gson.annotations.SerializedName

data class ShopDataTrans(
    @SerializedName("id") val shopId: String,
    @SerializedName("shopname") val shopName: String,
    @SerializedName("image") val shopImage: String,
    @SerializedName("address") val shopAddress: String,
    @SerializedName("timming") val shopTiming: String,
    @SerializedName("phone_number") val shopPhone: String
)