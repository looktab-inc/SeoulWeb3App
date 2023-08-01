package com.looktab.echonupy.data.source.nft

import com.looktab.echonupy.data.api.NftService
import com.looktab.echonupy.feature.campain.model.Attribute
import com.looktab.echonupy.feature.campain.model.Campaign
import com.looktab.echonupy.feature.campain.model.MetadataJson
import io.reactivex.rxjava3.core.Single

class RemoteNftDataSource(
    private val service: NftService
) : NftDataSource {

    override fun getCampaigns():
        Single<List<Campaign>> {
        return service.getCampaigns().map { res ->
            res.map {
                Campaign(
                    model = it.model,
                    address = it.address,
                    mintAddress = it.mintAddress,
                    name = it.name,
                    uri = it.uri,
                    metadataJson = MetadataJson(
                        name = it.name,
                        description = it.metadataJson.description,
                        image = it.metadataJson.image,
                        symbol = it.metadataJson.symbol,
                        attributes = it.metadataJson.attributes.map { attributes ->
                            Attribute(
                                trait_type = attributes.trait_type,
                                value = attributes.value
                            )
                        }
                    )
                )
            }
        }
    }

}