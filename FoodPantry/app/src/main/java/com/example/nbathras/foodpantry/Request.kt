package com.example.nbathras.foodpantry

import java.util.*

data class Request (
    val finishDate: String = "",
    val requestName: String = "",
    val currRequestNum: Int = 0,
    val requestNumNeeded:Int = 0
)