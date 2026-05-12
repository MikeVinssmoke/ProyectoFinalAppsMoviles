package com.example.romero.proyectofinalappsmoviles.data.remote

import com.example.romero.proyectofinalappsmoviles.data.remote.model.CategoryResponse
import com.example.romero.proyectofinalappsmoviles.data.remote.model.ReportRequest
import com.example.romero.proyectofinalappsmoviles.data.remote.model.ReportResponse
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @GET("reports")
    suspend fun getReports(): Response<List<ReportResponse>>

    @GET("reports/{id}")
    suspend fun getReport(@Path("id") id: Long): Response<ReportResponse>

    @POST("reports")
    suspend fun createReport(@Body report: ReportRequest): Response<ReportResponse>

    @PUT("reports/{id}")
    suspend fun updateReport(@Path("id") id: Long, @Body report: ReportRequest): Response<ReportResponse>

    @GET("categories")
    suspend fun getCategories(): Response<List<CategoryResponse>>
}