package com.plcoding.cleanarchitecturenoteapp.feature_note.domain.util


/**
 * Created by Carlos Daniel Agudelo on 22/02/2022.
 */
sealed class OrderType {
    object Ascending: OrderType()
    object Descending: OrderType()
}