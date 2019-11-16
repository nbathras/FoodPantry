package com.example.nbathras.foodpantry

import java.util.*
import kotlin.collections.ArrayList

data class Distributor (
    val userId: String             = "",
    val distributorId: String      = "",
    val distributorName: String    = "",
    val distributorAbout: String   = "",
    val distributorLocation: ArrayList<String> = ArrayList<String>()
)