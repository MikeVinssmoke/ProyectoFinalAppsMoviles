package com.example.romero.proyectofinalappsmoviles.data.remote.model

import com.google.gson.annotations.SerializedName

data class ReportResponse(
    @SerializedName("id")          val id: Long,
    @SerializedName("title")       val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("category")    val category: String,
    @SerializedName("priority")    val priority: String,
    @SerializedName("status")      val status: String,
    @SerializedName("location")    val location: String,
    @SerializedName("date")        val date: Long
)

data class ReportRequest(
    @SerializedName("title")       val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("category")    val category: String,
    @SerializedName("priority")    val priority: String,
    @SerializedName("status")      val status: String,
    @SerializedName("location")    val location: String
)