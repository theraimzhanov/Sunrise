package com.example.sunrise.widgets

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CheckboxDefaults.colors
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.sunrise.model.Favorite
import com.example.sunrise.navigation.WeatherScreens
import com.example.sunrise.screens.favorites.FavoriteScreen
import com.example.sunrise.screens.favorites.FavoriteViewModel
import dagger.hilt.android.lifecycle.HiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherAppBar(
    title: String = "Title",
    icon: ImageVector? = null,
    isMainScreen: Boolean = true,
    elevation: Dp = 0.dp,
    navController: NavController,
    favoriteViewModel: FavoriteViewModel = hiltViewModel(),
    onAddActionClicked: () -> Unit = {},
    onButtonClicked: () -> Unit = {}
) {

    val showDialog = remember {
        mutableStateOf(false)
    }

    val showIt= remember { mutableStateOf(false) }
    val context = LocalContext.current

    if (showDialog.value) {
        ShowSettingDropDownMenu(showDialog = showDialog, navController = navController)
    }
    Surface(shadowElevation = elevation) {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = title,
                    color = MaterialTheme.colorScheme.onSecondary,
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                )
            }, actions = {
                if (isMainScreen) {
                    IconButton(onClick = { onAddActionClicked.invoke() }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "search icon"
                        )
                    }
                    IconButton(onClick = {
                        showDialog.value = true
                    }) {
                        Icon(
                            imageVector = Icons.Rounded.MoreVert,
                            contentDescription = "search rounded"
                        )
                    }
                } else Box {}
            },
            navigationIcon = {
                if (icon != null) {
                    Icon(
                        imageVector = icon, contentDescription = "icon",
                        tint = MaterialTheme.colorScheme.onSecondary,
                        modifier = Modifier.clickable {
                            onButtonClicked.invoke()
                        })
                }
                if (isMainScreen) {
                    val isAlreadyInList = favoriteViewModel.favList.collectAsState().value
                        .filter { item->
                            (item.city==title.split(",")[0])
                        }
                    if (isAlreadyInList.isNullOrEmpty()){
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Favorite icon",
                            modifier = Modifier.padding(start = 12.dp)
                                .scale(0.9f)
                                .clickable {
                                    val dataList = title.split(",")
                                    favoriteViewModel.insertFavorite(
                                        Favorite(
                                            city = dataList[0],
                                            country = dataList[1]
                                        )).run {
                                            showIt.value = true
                                        }
                                },
                            tint = Color.Red.copy(alpha = 0.6f))
                    } else{
                        showIt.value = false
                        Box{}
                    }
                    ShowToast(context = context,showIt)
                }

            }, colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
            )
        )
    }

}

@Composable
fun ShowToast(context: Context, showIt: MutableState<Boolean>) {
   if (showIt.value){
       Toast.makeText(context,"City Added to Favorites", Toast.LENGTH_SHORT).show()
   } else {

   }
}

@Composable
fun ShowSettingDropDownMenu(showDialog: MutableState<Boolean>, navController: NavController) {
    var expanded by remember { mutableStateOf(true) }
    val items = listOf("About", "Favorites", "Settings")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.TopEnd)
            .absolutePadding(top = 45.dp, right = 25.dp)
    ) {
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(160.dp)
                .background(Color.White)
        ) {
            items.forEach { text ->

                DropdownMenuItem(
                    text = {
                        Text(
                            text = text,
                            fontWeight = FontWeight.W300
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = when (text) {
                                "About" -> Icons.Default.Info
                                "Favorites" -> Icons.Default.FavoriteBorder
                                else -> Icons.Default.Settings
                            },
                            contentDescription = null,
                            tint = Color.LightGray
                        )
                    },
                    onClick = {
                        expanded = false
                        showDialog.value = false

                        val route = when (text) {
                            "About" -> WeatherScreens.AboutScreen.name
                            "Favorites" -> WeatherScreens.FavoriteScreen.name
                            else -> WeatherScreens.SettingScreen.name
                        }

                        navController.navigate(route)
                    }
                )
            }
        }
    }
}


