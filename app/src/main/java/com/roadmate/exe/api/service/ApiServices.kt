package com.roadmate.exe.api.service

import com.roadmate.exe.api.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiServices {

    @POST("excutivelogin")
    suspend fun shopLogin(@Body jsonRequest : RequestBody): Response<RoadmateApiResponse>

    @POST("exeotp")
    suspend fun verifyOTP(@Body jsonRequest : RequestBody): Response<OtpMaster>

    @POST("authorised_shop_list")
    suspend fun getAddedShopList(@Body jsonRequest : RequestBody): Response<ShopListMaster>

    @POST("unauthorised_shop_list")
    suspend fun getVisitedShopList(@Body jsonRequest : RequestBody): Response<ShopListMaster>

    @POST("shopslist")
    suspend fun getShopDetails(@Body jsonRequest : RequestBody): Response<ShopListMaster>

    @Multipart
    @POST("shopreg_exe_authorised")
    suspend fun addNewShop(@Part image: MultipartBody.Part,
                           @Part type: MultipartBody.Part,
                           @Part shopname: MultipartBody.Part,
                           @Part phnum: MultipartBody.Part,
                           @Part phnum2: MultipartBody.Part,
                           @Part desc: MultipartBody.Part,
                           @Part opentime: MultipartBody.Part,
                           @Part closetime: MultipartBody.Part,
                           @Part agrimentverification_status: MultipartBody.Part,
                           @Part address: MultipartBody.Part,
                           @Part pincode: MultipartBody.Part,
                           @Part latitude: MultipartBody.Part,
                           @Part logitude: MultipartBody.Part,
                           @Part trans_id: MultipartBody.Part,
                           @Part exeid: MultipartBody.Part,
                           @Part authorised_status: MultipartBody.Part): Response<RoadmateApiResponse>

    @Multipart
    @POST("shopreg_exe_unauthorised")
    suspend fun addNewVisitShop(@Part image: MultipartBody.Part,
                           @Part type: MultipartBody.Part,
                           @Part shopname: MultipartBody.Part,
                           @Part phnum: MultipartBody.Part,
                           @Part phnum2: MultipartBody.Part,
                           @Part desc: MultipartBody.Part,
                           @Part opentime: MultipartBody.Part,
                           @Part closetime: MultipartBody.Part,
                           @Part agrimentverification_status: MultipartBody.Part,
                           @Part address: MultipartBody.Part,
                           @Part pincode: MultipartBody.Part,
                           @Part latitude: MultipartBody.Part,
                           @Part logitude: MultipartBody.Part,
                           @Part trans_id: MultipartBody.Part,
                           @Part exeid: MultipartBody.Part,
                           @Part authorised_status: MultipartBody.Part): Response<RoadmateApiResponse>

    @GET("shop_categories")
    suspend fun getShopTypes(): Response<MoreServicesMaster>

    @POST("storanswer")
    suspend fun registerForFcm(@Body jsonRequest : RequestBody): Response<RoadmateApiResponse>
}