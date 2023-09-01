package com.example.sewinglessons.data.api

import com.example.sewinglessons.data.api.model.PatternDetailsItem
import com.example.sewinglessons.data.api.model.PatternItem
import retrofit2.http.GET
import retrofit2.http.Query

interface SewingApi {
    @GET(ApiConstants.END_POINTS)
    suspend fun getPattern():List<PatternItem>

    @GET(ApiConstants.ID_END_POINTS)
    suspend fun getPatternById(@Query(value = "id") id: String):List<PatternDetailsItem>
}