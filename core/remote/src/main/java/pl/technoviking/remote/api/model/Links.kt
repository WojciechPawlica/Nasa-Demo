package pl.technoviking.remote.api.model


import com.google.gson.annotations.SerializedName

data class Links(
    @SerializedName("next")
    val next: String?,
    @SerializedName("previous")
    val previous: String?,
    @SerializedName("self")
    val self: String
)