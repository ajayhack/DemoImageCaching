package com.example.demoimagecaching.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.demoimagecaching.model.cache.ImageCaching
import com.example.demoimagecaching.utils.InternetUtils
import com.example.demoimagecaching.viewmodel.AppViewModal

@Composable
fun ScrollablePhotoGrid() {
    val viewModel: AppViewModal = viewModel()
    val rememberedPhotosList = viewModel.photosListData.value.toMutableList()
    Scaffold(topBar = { AppBar(appBarTitle = "Photos App" ,
        appBarIcon = Icons.Default.Home ,
        contentDescription = "Back Arrow Icon")}) { paddingValues ->
        Surface(modifier = Modifier.fillMaxSize() , color = Color.LightGray) {
            if(InternetUtils.noDataFoundCase()) {
                Column(modifier = Modifier
                    .size(200.dp)
                    .padding(paddingValues) ,
                    horizontalAlignment = Alignment.CenterHorizontally ,
                    verticalArrangement = Arrangement.Center){
                    Text(text = "[No Internet] Please check your connection\nNo Offline Data Found!!!")
                }
                return@Surface
            }
            AnimatedVisibility(visible = rememberedPhotosList.isEmpty()) {
                Column(modifier = Modifier
                    .size(200.dp)
                    .padding(paddingValues) ,
                    horizontalAlignment = Alignment.CenterHorizontally ,
                    verticalArrangement = Arrangement.Center){
                    CircularProgressIndicator(color = colors.primary, modifier = Modifier.size(28.dp))
                }
            }
            if(rememberedPhotosList.isNotEmpty()){
                LazyVerticalGrid(columns = GridCells.Fixed(3)) {
                    itemsIndexed(rememberedPhotosList) { index , photo ->
                        PhotoItem(photo)
                    }
                }
            }
        }
    }
}

@Composable
fun AppBar(appBarTitle : String = "Photos App",
           appBarIcon : ImageVector = Icons.Default.Home,
           contentDescription : String = "Home Icon"){
    TopAppBar(navigationIcon = {
        Icon(appBarIcon,
            contentDescription = contentDescription ,
            modifier = Modifier
                .padding(12.dp))},
        title = { Text(text = appBarTitle)})
}

@Composable
fun PhotoItem(photo: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ImageCaching.readImageFromDiskCache(photo)?.asImageBitmap()?.let {
            Image(bitmap = it,
                contentDescription = null,
                modifier = Modifier
                    .wrapContentSize()
                    .aspectRatio(1f, matchHeightConstraintsFirst = false),
                contentScale = ContentScale.Fit
            )
        }
    }
}