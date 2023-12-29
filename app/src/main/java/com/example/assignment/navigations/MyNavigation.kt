package com.example.assignment.navigations

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.annotation.ExperimentalCoilApi
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.example.assignment.screens.ItemDetailsScreen
import com.example.assignment.screens.ItemsListScreen

@ExperimentalGlideComposeApi
@ExperimentalCoilApi
@ExperimentalComposeUiApi
@ExperimentalMaterial3Api
@Composable
fun MyNavigation(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = ItemList.route)
    {
        composable(ItemList.route){
            ItemsListScreen(navController)
        }
        composable(
            "${ItemDetails.route}/{${ItemDetails.itemID}}",
            arguments = listOf(navArgument(ItemDetails.itemID) { type = NavType.IntType })
        ){
            val id = requireNotNull(it.arguments?.getInt(ItemDetails.itemID))
            ItemDetailsScreen(navController,id)
        }
    }
}