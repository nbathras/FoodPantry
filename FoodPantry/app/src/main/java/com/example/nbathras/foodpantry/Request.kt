package com.example.nbathras.foodpantry

import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

data class Request (
    val deliveryDate: String = "",
    val requestId: String = "",
    val finishDate: String = "",
    val itemsList: ArrayList<HashMap<String,Any>> = ArrayList<HashMap<String,Any>>(),
    val userId: String = "")

{
    companion object {
         const val ITEM_NAME  = "itemName"
         const val ITEM_CURRENT_QUANTITY   = "currentItemQuantity"
         const val ITEM_MAX_QUANTITY  = "maxItemQuantity"
    }
}



