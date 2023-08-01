package com.looktab.echonupy.feature

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.looktab.echonupy.R
import com.looktab.echonupy.Util
import com.looktab.echonupy.base.BaseActivity
import com.looktab.echonupy.data.injection.Injection
import com.looktab.echonupy.databinding.ActivityMainBinding
import com.looktab.echonupy.feature.campain.NftDetailFragment
import com.looktab.echonupy.feature.campain.NftFragment
import com.looktab.echonupy.feature.login.PhantomLoginFragment
import com.looktab.echonupy.feature.plantnft.PlantNftFragment
import com.looktab.echonupy.feature.progressroom.ProgressFragment
import com.looktab.echonupy.feature.web.WebFragment

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private val viewModel by lazy {
        ViewModelProvider(
            viewModelStore, MainViewModelFactory(
                Injection.provideRemoteNftSource()
            )
        )[MainViewModel::class.java]
    }

    var progressFragment = ProgressFragment.newInstance()
    var plantNftFragment = PlantNftFragment.newInstance()
    var loginFragment = PhantomLoginFragment.newInstance()
    var nftList = NftFragment.newInstance()

    private lateinit var locationManager: LocationManager

    override fun initViews() {
        super.initViews()
        binding.viewModel = viewModel
        addFragment(progressFragment)
        viewModel.getNft()

//        addOutFragment(PhantomLoginFragment.newInstance())

        permissionRequest()
        getMylocation()

        binding.btnJoinCampaign.setOnClickListener {
            viewModel.setViewFlow(MainViewModel.ViewFlow.NftCampaign)
        }
        binding.btnNftList.setOnClickListener {
            viewModel.setViewFlow(MainViewModel.ViewFlow.PlantLft)
        }
    }

    override fun initObserves() {
        super.initObserves()
        viewModel.flow.observe(this, Observer {
            Log.e("flow", it.name)

            when (it) {
                MainViewModel.ViewFlow.NftDetail -> {
                    addOutFragment(NftDetailFragment.newInstance())
                }

                MainViewModel.ViewFlow.NftCampaign -> {
                    addOutFragment(nftList)
                }

                MainViewModel.ViewFlow.Main -> {
                    addFragment(progressFragment)
                }

                MainViewModel.ViewFlow.PlantLft -> {
                    addOutFragment(plantNftFragment)
                }

                MainViewModel.ViewFlow.Chat -> {
                    addOutFragment(WebFragment.newInstance())
                }

                MainViewModel.ViewFlow.UpPoint -> {
                    binding.tvToday.text = "오늘 모은 탄소배출량 .. 100 CO2"
                    binding.tvTotal.text = "Total CO2 1300"
                }

                else -> {}
            }
        })
    }

    private fun addOutFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            add(R.id.out_fragment_container, fragment)
        }.commit()
    }

    private fun addFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            add(R.id.fragment_container, fragment)
        }.commit()
    }

    private fun permissionRequest() {
        val permissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                    Log.e("permission", "location permission granted")
                }

                permissions.getOrDefault(Manifest.permission.CAMERA, false) -> {
                    Log.e("permission", "camera permission granted")
                }

                else -> {
                    Util.createToast(this, "permission denied")
                }
            }
        }

        permissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.CAMERA
            )
        )
    }


    private fun getMylocation() {
        if (::locationManager.isInitialized.not()) {
            locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        }

        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        val loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        viewModel.setLoc(loc?.latitude, loc?.longitude)
    }
}