package com.looktab.echonupy.feature

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.looktab.echonupy.data.source.nft.NftDataSource

class MainViewModelFactory(val nftService: NftDataSource) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return MainViewModel(nftService) as T
    }
}