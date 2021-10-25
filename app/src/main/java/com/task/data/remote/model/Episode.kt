package com.task.data.remote.model

import com.google.gson.annotations.SerializedName

data class Episode(

    @SerializedName ("id") val id: Int,
    @SerializedName ("name") val name: String,
    @SerializedName("air_date") val airDate: String,
    @SerializedName("episode") val code: String,
    @SerializedName("characters") val characters: ArrayList<String>,
    @SerializedName("url") val url : String,
    @SerializedName("created") val createdDate: String

    )