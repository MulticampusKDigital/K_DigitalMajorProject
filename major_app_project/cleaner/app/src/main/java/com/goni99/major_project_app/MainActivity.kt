package com.goni99.major_project_app

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.goni99.designpro.service.GPS
import com.goni99.major_project_app.databinding.ActivityMainBinding
import com.goni99.major_project_app.view.BlindActivity
import com.goni99.major_project_app.view.ClosetActivity
import com.goni99.major_project_app.view.DryerActivity
import com.goni99.major_project_app.view.WashingMachineActivity
import com.goni99.major_project_app.viewmodel.MainViewModel
import com.google.android.gms.location.*

class MainActivity : GPS() {
    val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private var mFusedLocationProviderClient: FusedLocationProviderClient? =
        null // 현재 위치를 가져오기 위한 변수
    lateinit var mLastLocation: Location // 위치 값을 가지고 있는 객체
    internal lateinit var mLocationRequest: LocationRequest // 위치 정보 요청의 매개변수를 저장하는

    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        // mainViewModel 인스턴스 생성
        mainViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(MainViewModel::class.java)


        intentActivity()
    }

    override fun onStart() {
        super.onStart()
        mLocationRequest = LocationRequest.create().apply {
            this.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        if (checkPermissionForLocation(this)) {
            startLocationUpdate()
        }
        observingData()
        mainViewModel.isRetrofit.observe(this, Observer {
            Log.d("isRetrofit" , "$it")
            if (it) binding.loadingWeather.visibility = View.INVISIBLE
            else binding.loadingWeather.visibility = View.VISIBLE
        })
    }

    fun intentActivity() {
        with(binding) {
            washMachineButton.setOnClickListener {
                val intent = Intent(baseContext, WashingMachineActivity::class.java)
                startActivity(intent)
            }
            dryingButton.setOnClickListener {
                val intent = Intent(baseContext, DryerActivity::class.java)
                startActivity(intent)
            }
            closetButton.setOnClickListener {
                val intent = Intent(baseContext, ClosetActivity::class.java)
                startActivity(intent)
            }
            blindButton.setOnClickListener {
                val intent = Intent(baseContext, BlindActivity::class.java)
                startActivity(intent)
            }
        }
    }

    fun observingData() {
        // viewModel 내 데이터를 관찰하여 변경하기(observing)
        mainViewModel.location.observe(this, Observer {
            Log.d("testing", "x:${it.x}, y:${it.y}")
            mainViewModel.loadWeatherData(it.x, it.y)
        })
        mainViewModel.img.observe(this, Observer {
            Log.d("testing", "img url : ${it}")
            Glide.with(this)
                .load(it)
                .error(com.goni99.major_project_app.R.drawable.ic_launcher_background)
                .into(binding.weatherImageView)
        })
        mainViewModel.weather.observe(this, Observer {
            Log.d("isRetrofit", "${it.cityName}")

            with(binding) {
                locationTextView.text = it.cityName + "\n" + it.country
                temperatureTextView.text = it.temperature + "°C"
                weatherStateTextView.text = it.description
            }
        })

    }

    override fun startLocationUpdate() {
        //FusedLocationProviderClient의 인스턴스를 생성.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        // 기기의 위치에 관한 정기 업데이트를 요청하는 메서드 실행
        // 지정한 루퍼 스레드(Looper.myLooper())에서 콜백(mLocationCallback)으로 위치 업데이트를 요청
        mFusedLocationProviderClient!!.requestLocationUpdates(
            mLocationRequest,
            mLocationCallback,
            Looper.myLooper()
        )
    }

    val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult.lastLocation
            onLocationChanged(locationResult.lastLocation)
        }
    }

    fun onLocationChanged(location: Location) {
        mLastLocation = location
        mainViewModel.setLocation(mLastLocation.latitude, mLastLocation.longitude)
    }
}