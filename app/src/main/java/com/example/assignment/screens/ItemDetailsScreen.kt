package com.example.assignment.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.StarHalf
import androidx.compose.material.icons.rounded.StarOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.assignment.data.ListViewModel
import com.example.assignment.navigations.ItemList
import com.example.assignment.navigations.TopBar
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

@ExperimentalGlideComposeApi
@ExperimentalCoilApi
@ExperimentalComposeUiApi
@ExperimentalMaterial3Api
@Composable
fun ItemDetailsScreen(navController: NavHostController,id:Int) {
    Scaffold(
        topBar = { TopBar(name = "Item Details", navController) }
    ) {
        Column(modifier = Modifier
            .padding(it)
            .background(Color(0XFFE6EDf5))) {
            ItemDetailsScreenComponent(navController,id)
        }
    }
}

@ExperimentalGlideComposeApi
@ExperimentalCoilApi
@ExperimentalComposeUiApi
@Composable
fun ItemDetailsScreenComponent(navController: NavHostController,id:Int) {

    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current

    val file = context.createImageFile()
    val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)



    val viewModel: ListViewModel = viewModel()

    val selectedItem by viewModel.getItemById(id).observeAsState()

    var name by remember { mutableStateOf(TextFieldValue("")) }
    var quantity by remember { mutableIntStateOf(0) }
    var rating by remember { mutableDoubleStateOf(0.0) }
    var remarks by remember { mutableStateOf(TextFieldValue("")) }
    var capturedImageUris by remember { mutableStateOf<List<Uri>>(emptyList()) }


    LaunchedEffect(selectedItem){
        name = TextFieldValue(selectedItem?.name?:"")
        quantity = selectedItem?.quantity ?: 0
        rating = selectedItem?.rating ?: 0.0
        remarks = TextFieldValue(selectedItem?.remarks ?: "")
        capturedImageUris = if(selectedItem?.images !=null) {
            selectedItem?.images?.map { Uri.parse(it) }!!
        }else{
            emptyList()
        }
    }

    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
            if (it && !capturedImageUris.contains(uri)) {
                capturedImageUris = capturedImageUris + listOf(uri)

            }
        }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
            cameraLauncher.launch(uri)
        } else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier.padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.Start
        ) {
            Card(
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                modifier = Modifier
                    .padding(10.dp)
                    .border(
                        width = 1.dp,
                        color = Color.Black,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .size(120.dp)
                    .clickable {
                        val permissionCheckResult =
                            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                        if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                            cameraLauncher.launch(uri)
                        } else {
                            // Request a permission
                            permissionLauncher.launch(Manifest.permission.CAMERA)
                        }
                    }
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = "Take Photo",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W500
                    )
                }
            }

            LazyRow(
                modifier = Modifier
                    .padding(5.dp)
                    .width(300.dp)
            ) {
                items(capturedImageUris) { imageUri ->
                    val iconSize = 24.dp
                    val offsetInPx =
                        LocalDensity.current.run { ((iconSize - 5.dp) / 2).roundToPx() }
                    Box(
                        modifier = Modifier
                            .padding(iconSize / 2)
                    ) {
                        Card {
                            GlideImage(
                                model = imageUri,
                                contentDescription = "Image",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(110.dp)
                                    .clip(RoundedCornerShape(5.dp))
                            )
                        }
                        IconButton(
                            onClick = { capturedImageUris = capturedImageUris - listOf(imageUri) },
                            modifier = Modifier
                                .offset {
                                    IntOffset(x = +offsetInPx, y = -offsetInPx)
                                }
                                .clip(CircleShape)
                                .background(Color(0xFFEA4141))
                                .size(iconSize)
                                .align(Alignment.TopEnd)
                        ) {
                            Icon(
                                modifier = Modifier.padding(3.dp),
                                imageVector = Icons.Rounded.Close,
                                contentDescription = "close",
                                tint = Color.White
                            )
                        }
                    }
                }
            }
        }

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Item Name:",
                fontSize = 18.sp,
                fontWeight = FontWeight.W500,
                color = if (name.text.isNotEmpty()) Color.Black else Color.Red
            )
            OutlinedTextField(
                singleLine = true,
                value = name,
                onValueChange = { name = it },
                placeholder = { Text(text = "Name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, bottom = 10.dp, end = 10.dp, top = 10.dp),
                shape = RoundedCornerShape(18),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = { keyboardController?.hide() }
                ),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.LightGray,
                    unfocusedIndicatorColor = Color.LightGray,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    cursorColor = Color.Black
                ),
                textStyle = TextStyle(
                    fontWeight = FontWeight.W500,
                    fontSize = 16.sp
                )
            )
        }
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Quantity:",
                fontSize = 18.sp,
                fontWeight = FontWeight.W500,
                color = if (quantity != 0) Color.Black else Color.Red
            )
            OutlinedTextField(
                singleLine = true,
                value = if (quantity == 0) "" else quantity.toString(),
                onValueChange = { quantity = it.toIntOrNull()?:0},
                placeholder = { Text(text = "Quantity") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 30.dp, bottom = 10.dp, end = 10.dp, top = 10.dp),
                shape = RoundedCornerShape(18),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Number
                ),
                keyboardActions = KeyboardActions(
                    onDone = { keyboardController?.hide() }
                ),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.LightGray,
                    unfocusedIndicatorColor = Color.LightGray,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    cursorColor = Color.Black
                ),
                textStyle = TextStyle(
                    fontWeight = FontWeight.W500,
                    fontSize = 18.sp
                )
            )
        }
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Rating:",
                fontWeight = FontWeight.W500,
                fontSize = 18.sp,
                color = if (rating != 0.0) Color.Black else Color.Red
            )
            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(start = 40.dp, top = 10.dp, bottom = 10.dp)) {
                RatingBar(
                    modifier = Modifier
                        .size(50.dp),
                    rating = rating,
                    onRatingChanged = { rating = it }
                )
            }
        }
        Column(
            modifier = Modifier.padding(top = 10.dp)
        ) {
            Text(
                text = "Remarks:",
                fontSize = 18.sp,
                fontWeight = FontWeight.W500,
            )
            OutlinedTextField(
                value = remarks,
                onValueChange = { newText -> remarks = newText },
                placeholder = { Text(text = "Remarks") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
                    .height(120.dp),
                shape = RoundedCornerShape(12),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = { keyboardController?.hide() }
                ),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.LightGray,
                    unfocusedIndicatorColor = Color.LightGray,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    cursorColor = Color.Black
                ),
                textStyle = TextStyle(
                    fontWeight = FontWeight.W500,
                    fontSize = 18.sp
                )
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Column(
            modifier = Modifier.padding(5.dp)
        ) {
            Button(
                onClick = {
                    if (name.text.isNotEmpty() && quantity != 0 && rating != 0.0) {
                        if (id == 0) {
                            viewModel.saveItem(
                                name.text,
                                quantity,
                                rating,
                                remarks.text,
                                capturedImageUris
                            )
                        } else {
                            viewModel.updateItem(
                                id,
                                name.text,
                                quantity,
                                rating,
                                remarks.text,
                                capturedImageUris
                            )
                        }
                        navController.navigate(ItemList.route){
                            popUpTo(ItemList.route){
                                inclusive = true
                            }
                            launchSingleTop  = true
                        }
                    } else {
                        Toast.makeText(
                            context,
                            "Please fill in all mandatory fields",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 1.dp,
                    pressedElevation = 5.dp,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                shape = RoundedCornerShape(24),
                colors = ButtonDefaults.buttonColors(Color(0XFFC8D1F7))
            ) {
                Text(
                    text = "Save", textAlign = TextAlign.Center, fontSize = 24.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(2.dp)
                )
            }
        }
    }
}

@Composable
fun RatingBar(
    rating: Double = 0.0,
    stars:Int = 5,
    onRatingChanged: (Double) -> Unit,
    modifier: Modifier = Modifier
) {
    var isHalfStar = (rating % 1) !=0.0

    Row{
        for (index in 1..stars){
            Icon(
                modifier = modifier.clickable{onRatingChanged(index.toDouble())},
                contentDescription = null,
                tint = Color(0XFFFFC000),
                imageVector = if(index<=rating){
                    Icons.Rounded.Star
                }else{
                    if(isHalfStar){
                        isHalfStar = false
                        Icons.Rounded.StarHalf
                    }else{
                        Icons.Rounded.StarOutline
                    }
                }
            )
        }
    }
}

@SuppressLint("SimpleDateFormat")
fun Context.createImageFile(): File {
    // Create an image file name
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    val image = File.createTempFile(
        imageFileName, /* prefix */
        ".jpg", /* suffix */
        externalCacheDir      /* directory */
    )
    return image
}