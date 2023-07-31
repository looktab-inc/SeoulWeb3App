package com.looktab.echonupy.feature.campain.model

data class NftRow(
    val nftAddress: String,
    val name: String,
    val description: String,
    val image: String,
    val status: String,

    val store_address: String,
    val distance: String,

    val discount_type: String,
    val discount_rate: Double,
    val discount_amount: Double,

    val started_at: String,
    val ended_at: String
)