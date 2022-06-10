package dev.alejo.colombiaholidays.data.model

import com.google.gson.annotations.SerializedName

data class HolidayModel(
    @SerializedName("date") val date: String,
    @SerializedName("localName") val localName: String,
    @SerializedName("name") val name: String,
    @SerializedName("fixed") val fixed: Boolean,
    @SerializedName("global") val global: Boolean,
    @SerializedName("launchYear") val launchYear: Int,
    @SerializedName("types") val types: List<String>
)