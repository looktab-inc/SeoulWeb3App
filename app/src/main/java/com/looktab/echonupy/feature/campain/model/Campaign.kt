package com.looktab.echonupy.feature.campain.model

data class Campaign(
    val model: String,
    val address: String,
    val mintAddress: String,
    val name: String,
    val uri: String,
    val metadataJson: MetadataJson
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
