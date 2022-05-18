package com.goni99.designpro.data.weather

import com.goni99.major_project_app.data.Main
import com.google.gson.annotations.SerializedName

class WeatherResponse() {
    @SerializedName("name") var cityName:String? = null
    @SerializedName("weather") var weather = ArrayList<Weather>()
    @SerializedName("main") var main: Main? = null
    @SerializedName("wind") var wind: Wind? = null
    @SerializedName("sys") var sys: Sys? = null
}