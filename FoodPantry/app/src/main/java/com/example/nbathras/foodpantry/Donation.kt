package com.example.nbathras.foodpantry

import java.util.*

data class Donation (
    val randomId: String = "",
    val userId: String = "",
    val deliveryDate: String    = "",
    val isDelivered: Boolean   = false,
    val itemsList: ArrayList<Pair<String, Int>> = ArrayList<Pair<String, Int>>(),
    val userName: String = ""
)
