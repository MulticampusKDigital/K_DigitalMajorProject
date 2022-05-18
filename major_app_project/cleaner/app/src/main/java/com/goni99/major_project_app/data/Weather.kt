package com.goni99.designpro.data.weather

import com.google.gson.annotations.SerializedName

class Weather {
    @SerializedName("id") var id: Int = 0
    @SerializedName("main") var main :String? = null
    @SerializedName("description") var description: String? = null
    @SerializedName("icon") var icon: String? = null
}