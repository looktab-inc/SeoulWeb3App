package com.looktab.echonupy.data.injection

import com.looktab.echonupy.data.apiprovider.ApiProvider
import com.looktab.echonupy.data.source.nft.RemoteNftDataSource


object Injection {
    fun provideRemoteNftSource(): RemoteNftDataSource {
        return RemoteNftDataSource(ApiProvider.provideNft())
    }
}