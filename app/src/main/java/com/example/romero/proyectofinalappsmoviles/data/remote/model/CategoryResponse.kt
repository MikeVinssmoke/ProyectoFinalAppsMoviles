package com.example.romero.proyectofinalappsmoviles.data.remote.model

import com.google.gson.annotations.SerializedName

data class CategoryResponse(
    @SerializedName("id")      val id: Long,
    @SerializedName("name")    val name: String,
    @SerializedName("iconKey") val iconKey: String
)