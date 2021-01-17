package com.roadmate.exe.api.response

import com.google.gson.annotations.SerializedName

data class OtpTrans(
    @SerializedName("id") val user_id: String,
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("addrress") val addrress: String,
    @SerializedName("district") val district: String,
    @SerializedName("location") val location: String,
    @SerializedName("work_location") val work_location: String,
    @SerializedName("other_details") val other_details: String,
    @SerializedName("image") val image: String,
    @SerializedName("phonenum") val phonenum: String
)