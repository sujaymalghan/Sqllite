package com.example.assignment6

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle


import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Person

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface

import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Modifier
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

data class NavigationItem(
    var title: String,
    var selectedIcon: ImageVector,
    var unselectedIcon: ImageVector
)
@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    var showHomeText by mutableStateOf(false)
    private val viewModel by viewModels<AboutMeViewModel>()


    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnrememberedMutableState")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.showHomeText.observe(this) { showHomeText ->
            setContent {
                MaterialTheme {
                    val context = LocalContext.current
                    val items = mutableStateListOf(
                        NavigationItem(
                            title = "About Me",
                            selectedIcon = Icons.Filled.Person,
                            unselectedIcon = Icons.Outlined.Person,
                        ),
                        NavigationItem(
                            title = "Task 2",
                            selectedIcon = Icons.Filled.List,
                            unselectedIcon = Icons.Outlined.Menu
                        ),
                        NavigationItem(
                            title = "Task 3",
                            selectedIcon = Icons.Filled.DateRange,
                            unselectedIcon = Icons.Outlined.DateRange,
                        ),
                    )
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                        val scope = rememberCoroutineScope()
                        var selectedItemIndex by rememberSaveable {
                            mutableStateOf(-1)
                        }

                        ModalNavigationDrawer(
                            drawerContent = {

                                ModalDrawerSheet {
                                    Spacer(modifier = Modifier.height(16.dp))
                                    items.forEachIndexed { index, item ->
                                        val isSelected = index == selectedItemIndex
                                        NavigationDrawerItem(
                                            label = { Text(text = item.title) },
                                            selected = isSelected,
                                            onClick = {
                                                if (item.title == "About Me") {
                                                    viewModel.toggleShowHomeText()
                                                    selectedItemIndex = index
                                                } else if (item.title == "Task 2") {
                                                    context.startActivity(
                                                        Intent(
                                                            context,
                                                            MovieList::class.java
                                                        )
                                                    )
                                                    selectedItemIndex = -1
                                                } else {
                                                    context.startActivity(
                                                        Intent(
                                                            context,
                                                            MovieListtaskthree::class.java
                                                        )
                                                    )
                                                    selectedItemIndex = -1

                                                }

                                                scope.launch {
                                                    drawerState.close()
                                                }
                                            },
                                            icon = {
                                                Icon(
                                                    imageVector = if (index == selectedItemIndex) {
                                                        item.selectedIcon
                                                    } else item.unselectedIcon,
                                                    contentDescription = item.title
                                                )
                                            },
                                            modifier = Modifier
                                                .padding(NavigationDrawerItemDefaults.ItemPadding)

                                        )
                                    }
                                }

                            },
                            drawerState = drawerState
                        ) {

                            Scaffold(
                                topBar = {

                                    TopAppBar(
                                        title = {
                                            Text(
                                                text = "Assignment 6"
                                            )
                                        },
                                        navigationIcon = {

                                            IconButton(onClick = {
                                                scope.launch {
                                                    drawerState.open()
                                                }
                                            }) {
                                                Icon(
                                                    imageVector = Icons.Default.Menu,
                                                    contentDescription = "Menu"
                                                )
                                            }
                                        },
                                        colors = TopAppBarDefaults.smallTopAppBarColors(
                                            titleContentColor = Color.Black,
                                            navigationIconContentColor = Color(0xFF36454F),
                                            containerColor = Color(0xFFF5F5DC)
                                        )
                                    )

                                }


                            ) {

                                MainContent(showHomeText)
                            }
                        }


                    }


                }

            }
        }

        }
    }


@Composable
fun MainContent(showHomeText: Boolean) {

    val context = LocalContext.current
    val maxPaddingLimit = 58.dp
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = maxPaddingLimit)
    ) {
        if (showHomeText) {
            val LightYellow = Color(0xFFF5F5DC)
            val configuration = LocalConfiguration.current
            val screenWidthDp = configuration.screenWidthDp.dp

            val imageSize: Dp
            val fontSize1: TextUnit
            val fontSize2: TextUnit

            if (screenWidthDp > 800.dp) {
                imageSize = 300.dp
                fontSize1 = 36.sp
                fontSize2 = 36.sp
            } else {
                imageSize = 150.dp
                fontSize1 = 20.sp
                fontSize2 = 16.sp
            }
            BoxWithConstraints(modifier = Modifier.fillMaxSize().background(LightYellow)) {
                val imagePainter = painterResource(id = R.drawable.sujay)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .align(Alignment.Center),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = imagePainter,
                        contentDescription = null,
                        modifier = Modifier
                            .size(imageSize)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    val name = context.getString(R.string.sujay)
                    val course = context.getString(R.string.course)
                    Column {
                        Text(
                            text = name,
                            style = TextStyle(
                                fontSize = fontSize1,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Black
                            )
                        )
                        Text(
                            text = course   ,
                            style = TextStyle(
                                fontSize = fontSize2,
                                fontWeight = FontWeight.Normal,
                                color = Color.DarkGray
                            )
                        )
                    }
                }
            }
        } else {
            Box(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = "Use Navigation Drawer to select movie list or about me fragment",
                    modifier = Modifier.align(Alignment.Center),
                    textAlign = TextAlign.Center
                )
            }




        }
    }
}