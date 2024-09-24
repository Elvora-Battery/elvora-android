package com.unsoed.elvora.data.response.verify

import com.google.gson.annotations.SerializedName

data class CardResponse(

	@field:SerializedName("gcs_url")
	val gcsUrl: String? = null,

	@field:SerializedName("full_text")
	val fullText: String? = null,

	@field:SerializedName("ktp_data")
	val ktpData: KtpData? = null
)

data class KtpData(

	@field:SerializedName("NIK")
	val nIK: String? = null,

	@field:SerializedName("Nama")
	val nama: String? = null,

	@field:SerializedName("Tanggal Lahir")
	val tanggalLahir: String? = null
)
