package com.example.nbathras.foodpantry

import java.util.*
import kotlin.collections.HashMap

data class Request (
    val deliveryDate: String = "",
    val requestId: String = "",
    val finishDate: String = "",
    val itemsList: ArrayList<Pair<String, Pair<Int, Int>>> = ArrayList<Pair<String, Pair<Int, Int>>>()

)