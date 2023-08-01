package com.looktab.echonupy.data.api

import com.looktab.echonupy.data.dto.CampaignResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.*

interface NftService {
    @GET("/api/collection")
    fun getCampaigns(
    ): Single<List<CampaignResponse>>

}