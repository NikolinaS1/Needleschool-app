package com.example.sewinglessons.data

data class ProjectData(
    val title: String? = null,
    val image: String? = null,
    val description: String? = null
) {
    constructor() : this("", null, "")
}

