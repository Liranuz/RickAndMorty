package com.task.data.remote.model

import com.google.gson.annotations.SerializedName

class Cha {
    @SerializedName("id")
    var id = 0

    @SerializedName("name")
    var name: String? = null

    @SerializedName("status")
    var status: String? = null

    @SerializedName("species")
    var species: String? = null

    @SerializedName("type")
    var type: String? = null

    @SerializedName("gender")
    var gender: String? = null

    @SerializedName("image")
    var image: String? = null

    @SerializedName("episode")
    var episode: List<String>? = null

    @SerializedName("url")
    var url: String? = null

    @SerializedName("created")
    var created: String? = null
}