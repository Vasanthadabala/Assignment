package com.example.assignment.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.assignment.R
import com.example.assignment.data.ListViewModel
import com.example.assignment.data.roomdb.ListEntity
import com.example.assignment.navigations.ItemDetails
import com.example.assignment.navigations.MainScreenTopBar

@ExperimentalMaterial3Api
@Composable
fun ItemsListScreen(navController: NavHostController){
    Scaffold(
        topBar = { MainScreenTopBar()}
    ) {
        Column(modifier = Modifier.padding(it)) {
            ItemListScreenComponent(navController)
        }
    }
}
@Composable
fun ItemListScreenComponent(navController: NavHostController){
    val viewModel: ListViewModel = viewModel()
    val listItems = viewModel.getListOrderdByTitle().observeAsState(emptyList()).value

    Column(
        modifier = Modifier
            .background(Color(0XFFE6EDf5)),
    ) {
        if(listItems.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                items(listItems) { item ->
                    MenuDish(item = item, navController = navController, viewModel = viewModel)
                }
            }
        }else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                if (listItems.isEmpty()) {
                    Image(
                        painter = painterResource(id = R.drawable.corrupted),
                        contentDescription = null,
                        modifier = Modifier.size(120.dp)
                    )
                }
            }
        }
        FloatingActionButton(
            onClick = { navController.navigate(ItemDetails.route + "/0") },
            modifier = Modifier
                .padding(top = 0.dp, bottom = 20.dp, start = 0.dp, end = 10.dp)
                .align(Alignment.End)
                .size(60.dp),
            containerColor = Color(0XFFC8D1F7)
        ) {
            Text(
                text = "+",
                fontSize = 28.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun MenuDish(item: ListEntity, navController: NavHostController, viewModel: ListViewModel) {
    Card(
        elevation = CardDefaults.cardElevation(1.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(Color.White),
        modifier = Modifier
            .padding(10.dp)
            .clickable {
                navController.navigate("${ItemDetails.route}/${item.id}")
            }
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Item: ${item.name}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.W800,
                    modifier = Modifier
                        .padding(5.dp)
                        .weight(1f)
                )
                Text(
                    text = "Qty: ${item.quantity}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.W800,
                    modifier = Modifier.padding(5.dp)
                )

            }
            Text(
                text = "Ratings: ${item.rating}",
                fontSize = 18.sp,
                fontWeight = FontWeight.W800,
                modifier = Modifier.padding(5.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Remarks: " +
                            "${item.remarks.take(20)}${if (item.remarks.length>20) "..." else ""}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.W800,
                    modifier = Modifier
                        .padding(5.dp, end = 10.dp)
                        .weight(1f)
                )
                Icon(
                    imageVector = Icons.Rounded.Delete,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(5.dp)
                        .align(Alignment.Bottom)
                        .clickable {
                            viewModel.deleteItem(item.id)
                        }
                )
            }
        }
    }
}