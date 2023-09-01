package com.example.sewinglessons.data.api.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PatternDetailsItem(
    @Json(name = "description")
    val description: String,
    @Json(name = "id")
    val id: String,
    @Json(name = "image")
    val image: String,
    @Json(name = "materials_used")
    val materialsUsed: String,
    @Json(name = "name")
    val name: String,
    @Json(name = "recommended_fabrics")
    val recommendedFabrics: String,
    @Json(name = "short_description")
    val shortDescription: String
)