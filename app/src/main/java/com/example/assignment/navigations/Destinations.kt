package com.example.assignment.navigations

interface Destinations
{
    val route:String
}
object ItemList: Destinations {
    override val route="ItemList"
}
object ItemDetails: Destinations {
    override val route="ItemDetails"
    const val itemID = "itemId"
}