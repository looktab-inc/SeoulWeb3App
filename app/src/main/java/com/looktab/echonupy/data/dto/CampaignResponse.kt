package com.looktab.echonupy.data.dto

import com.google.gson.annotations.SerializedName

data class CampaignResponse(
    @SerializedName("model")
    val model: String,
    val address: String,
    val mintAddress: String,
    val updateAuthorityAddress: String,
    val json: String?, // Set the appropriate type for json field, e.g., JsonObject, if known
    val jsonLoaded: Boolean,
    val name: String,
    val symbol: String,
    val uri: String,
    val isMutable: Boolean,
    val primarySaleHappened: Boolean,
    val sellerFeeBasisPoints: Int,
    val editionNonce: Int,
    val creators: List<Creator>,
    val tokenStandard: Int,
    val collection: String?, // Set the appropriate type for collection field if known
    val collectionDetails: String?, // Set the appropriate type for collectionDetails field if known
    val uses: String?, // Set the appropriate type for uses field if known
    val programmableConfig: String?, // Set the appropriate type for programmableConfig field if known
    val metadataJson: MetadataJson
)

data class Creator(
    val address: String,
    val verified: Boolean,
    val share: Int
)

data class MetadataJson(
    val name: String,
    val description: String,
    val image: String,
    val symbol: String,
    val attributes: List<Attribute>
)

data class Attribute(
    val trait_type: String,
    val value: String
)
