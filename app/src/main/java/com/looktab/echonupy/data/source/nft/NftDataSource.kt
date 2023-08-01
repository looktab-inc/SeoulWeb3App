package com.looktab.echonupy.data.source.nft

import com.looktab.echonupy.feature.campain.model.Campaign
import io.reactivex.rxjava3.core.Single

interface NftDataSource {
    fun getCampaigns(): Single<List<Campaign>>
}