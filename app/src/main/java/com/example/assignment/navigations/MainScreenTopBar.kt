package com.example.assignment.navigations

import androidx.compose.foundation.background
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@ExperimentalMaterial3Api
@Composable
fun MainScreenTopBar(){
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "Items List",
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = Color.DarkGray
            )
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color(0xFFC8D1F7))
    )
}