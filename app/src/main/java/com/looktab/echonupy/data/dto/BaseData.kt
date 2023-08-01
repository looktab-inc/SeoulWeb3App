package com.looktab.echonupy.data.dto

data class BaseData<D>(
    val result: Boolean?,
    val data: D?
)