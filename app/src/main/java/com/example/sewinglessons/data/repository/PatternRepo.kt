package com.example.sewinglessons.data.repository

import com.example.sewinglessons.data.api.SewingApi
import com.example.sewinglessons.data.api.model.PatternDetailsItem
import com.example.sewinglessons.data.api.model.PatternItem
import javax.inject.Inject

class PatternRepo @Inject constructor(
    private val sewingApi: SewingApi
){
    suspend fun getPatterns(): List<PatternItem> {
        return sewingApi.getPattern()
    }

    suspend fun getPatternsById(id: String): List<PatternDetailsItem> {
        return sewingApi.getPatternById(id)
    }
}