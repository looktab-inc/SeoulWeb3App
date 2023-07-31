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
import com.looktab.echonupy.databinding.ActivityMainBinding
import com.looktab.echonupy.feature.campain.NftDetailFragment
import com.looktab.echonupy.feature.campain.NftFragment
import com.looktab.echonupy.feature.login.PhantomLoginFragment

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private val viewModel by lazy {
        ViewModelProvider(
            viewModelStore, MainViewModelFactory(
            )
        )[MainViewModel::class.java]
    }

    var nft = NftFragment.newInstance()

    private lateinit var locationManager: LocationManager

    override fun initViews() {
        super.initViews()
        binding.viewModel = viewModel
        addOutFragment(PhantomLoginFragment.newInstance())
        binding.btnNft.isActivated = true

        permissionRequest()
        getMylocation()
    }

    override fun initObserves() {
        super.initObserves()
        viewModel.flow.observe(this, Observer {
            Log.e("flow", it.name)
            binding.btnAirdrop.isActivated = false
            binding.btnNft.isActivated = false
            binding.btnWallet.isActivated = false
            binding.btnMypage.isActivated = false
            when (it) {
                MainViewModel.ViewFlow.NFT_Campain -> {
                    binding.btnNft.isActivated = true
                    changePage(nft)
                    viewModel.getNft()
                }

                MainViewModel.ViewFlow.NftDetail -> {
                    addOutFragment(NftDetailFragment.newInstance())
                }

                MainViewModel.ViewFlow.Login -> {

                }

                else -> {}
            }
        })
    }

    private fun changePage(fragment: Fragment) {
        if (!fragment.isAdded) {
            addFragment(fragment)
            changeShowFragment(fragment)
        } else {
            changeHideFragment(nft)
        }
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

    private fun changeHideFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            hide(fragment)
        }.commit()
    }

    private fun changeShowFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            show(fragment)
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