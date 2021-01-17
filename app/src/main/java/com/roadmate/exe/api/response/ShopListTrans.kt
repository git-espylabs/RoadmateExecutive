package com.roadmate.exe.api.response

import com.google.gson.annotations.SerializedName

data class ShopListTrans(
    @SerializedName("id") val shopId: String,
    @SerializedName("type") val type: String,
    @SerializedName("image") val shopImage: String,
    @SerializedName("open_time") val open_time: String,
    @SerializedName("close_time") val close_time: String,
    @SerializedName("shopname") val shopName: String,
    @SerializedName("address") val address: String,
    @SerializedName("phone_number") val phone_number: String = "0",
    @SerializedName("agrimentverification_status") val agrimentverification_status: String,
    @SerializedName("pincode") val pincode: String,
    @SerializedName("description") val description: String,
    @SerializedName("lattitude") val lattitude: String,
    @SerializedName("logitude") val logitude: String,
    @SerializedName("trans_id") val trans_id: String,
    @SerializedName("timming") val shopTiming: String,
    @SerializedName("authorised_status") val authorised_status: String
)