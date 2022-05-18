package com.goni99.major_project_app.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.goni99.designpro.model.Location
import com.goni99.designpro.data.weather.WeatherResponse
import com.goni99.designpro.model.WeatherData
import com.goni99.designpro.service.WeatherService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.DecimalFormat


// UI 관련 데이터를 저장하고 관리해주는 역할
class MainViewModel : ViewModel() {
    val apiKey = "openweatherAPI 사이트 API키"
    val baseUrl = "https://api.openweathermap.org"
    val K:Float = 273.15F
    val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private var _isRetrofit = MutableLiveData<Boolean>()
    val isRetrofit: LiveData<Boolean>
        get() = _isRetrofit

    private var _location = MutableLiveData<Location>()
    val location: LiveData<Location>
        get() = _location

    private var _weather = MutableLiveData<WeatherData>()
    val weather: LiveData<WeatherData>
        get() = _weather

    private var _img = MutableLiveData<String>()
    val img: LiveData<String>
        get() = _img


    fun setLocation(x: Double, y: Double) {
        _location.value = Location(x, y)
    }

    fun loadWeatherData(x: Double, y: Double) {
        val service = retrofit.create(WeatherService::class.java)
        val call = service.getCurrentWeatherData(x.toString(), y.toString(), apiKey)
        _isRetrofit.value = false
        call.enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(
                call: Call<WeatherResponse>,
                response: Response<WeatherResponse>
            ) {
                _isRetrofit.value = true
                if (response.code() == 200) {
                    val weatherResponse = response.body()

                    var imgURL = "http://openweathermap.org/img/wn/" + weatherResponse!!.weather!!.get(0).icon + "@2x.png";
                    _img.value = imgURL

                    _weather.value = WeatherData(
                        weatherResponse!!.cityName!!,
                        weatherResponse!!.sys!!.country!!,
                        DecimalFormat("#0.00").format(weatherResponse!!.main!!.temp - K),
                        weatherResponse!!.weather!!.get(0).description.toString()
                    )

                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                Log.d("WeatherMain", "result : "+ t.message)
            }
        })
    }

}