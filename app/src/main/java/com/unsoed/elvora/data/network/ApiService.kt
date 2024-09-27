package com.unsoed.elvora.data.network

import com.unsoed.elvora.data.MapRequest
import com.unsoed.elvora.data.response.BasicResponse
import com.unsoed.elvora.data.response.CommonResponse
import com.unsoed.elvora.data.response.PaidTransactionResponse
import com.unsoed.elvora.data.response.active.ActiveSubsResponse
import com.unsoed.elvora.data.response.auth.LoginResponse
import com.unsoed.elvora.data.response.getSubs.SubsResponse
import com.unsoed.elvora.data.response.home.DashboardResponse
import com.unsoed.elvora.data.response.map.MapResponse
import com.unsoed.elvora.data.response.new.NewTransactionResponse
import com.unsoed.elvora.data.response.transactionId.TransactionResponse
import com.unsoed.elvora.data.response.verify.CardResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {

    @FormUrlEncoded
    @POST("registration")
    suspend fun createAccount(
        @Field("email") email: String,
        @Field("name") name: String,
    ): Response<BasicResponse>

    @FormUrlEncoded
    @POST("verify-otp")
    suspend fun verifyOTP(
        @Field("email") email: String,
        @Field("otp") otp: String,
    ): Response<BasicResponse>

    @FormUrlEncoded
    @POST("set-password")
    suspend fun setPassword(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("confirmPassword") confirmPassword: String,
    ): Response<BasicResponse>

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String,
    ): Response<LoginResponse>

    @FormUrlEncoded
    @POST("shipping/create")
    suspend fun createShipping(
        @Header("Authorization") token: String,
        @Field("user_id") userId: String,
        @Field("name") name: String,
        @Field("phone") phone: String,
        @Field("street_name") streetName: String,
        @Field("village") village: String,
        @Field("full_address") fullAddress: String,
    ): Response<CommonResponse>

    @FormUrlEncoded
    @PATCH("shipping/edit")
    suspend fun editShipping(
        @Field("user_id") userId: String,
        @Field("name") name: String,
        @Field("phone") phone: String,
        @Field("street_name") streetName: String,
        @Field("village") village: String,
        @Field("full_address") fullAddress: String,
    ): Response<LoginResponse>

    @FormUrlEncoded
    @POST("transaction/create")
    suspend fun createNewTransaction(
        @Header("Authorization") token: String,
        @Field("rent_type_id") rentTypeId: String,
    ): Response<NewTransactionResponse>

    @FormUrlEncoded
    @PATCH("update-password")
    suspend fun changePassword(
        @Header("Authorization") token: String,
        @Field("currentPassword") currentPassword: String,
        @Field("newPassword") newPassword: String,
        @Field("confirmNewPassword") confirmNewPassword: String,
    ): Response<CommonResponse>

    @FormUrlEncoded
    @POST("transaction/paid")
    suspend fun transactionPaid(
        @Header("Authorization") token: String,
        @Field("id") id: String,
    ): Response<PaidTransactionResponse>

    @FormUrlEncoded
    @POST("ktp/create")
    suspend fun verificationKtp(
        @Header("Authorization") token: String,
        @Field("nik") nik: String,
        @Field("nama") nama: String,
        @Field("tanggal_lahir") tanggal_lahir: String,
    ): Response<CommonResponse>

    @FormUrlEncoded
    @POST("mqtt/publish-token")
    suspend fun publishToken(
        @Field("token") token: String,
    ): Response<CommonResponse>

    @Multipart
    @POST("verify-ktp")
    suspend fun uploadImageKtp(
        @Part file: MultipartBody.Part,
    ): Response<CardResponse>

    @POST("recommendations")
    suspend fun getStationRecommendation(
        @Body mapRequest: MapRequest
    ): Response<MapResponse>

    @GET("transaction/user")
    suspend fun getAllSubscription(
        @Header("Authorization") token: String,
    ): Response<SubsResponse>
    @GET("transaction/{id}")
    suspend fun getTransactionById(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
    ): Response<TransactionResponse>

    @GET("transaction/active")
    suspend fun getActiveSubs(
        @Header("Authorization") token: String,
    ): Response<ActiveSubsResponse>

    @GET("dashboard")
    suspend fun getDashboard(
        @Header("Authorization") token: String,
    ): Response<DashboardResponse>

    @FormUrlEncoded
    @PATCH("transaction/update/{id}")
    suspend fun changeBatteryName(
        @Path("id") id: Int,
        @Field("battery_name") batteryName: String,
    ): Response<CommonResponse>
}