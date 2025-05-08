package com.example.bartenderjetpack

import android.content.Context
import android.content.ContextWrapper
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.PaneScaffoldDirective
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.bartenderjetpack.ui.theme.BartenderJetpackTheme
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.window.core.layout.WindowWidthSizeClass
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bartenderjetpack.model.Drink
import com.example.bartenderjetpack.model.DrinkCategory
import com.example.bartenderjetpack.model.MainViewModel
import com.example.bartenderjetpack.ui.CategoryCards
import com.example.bartenderjetpack.ui.CategoryDetailView
import com.example.bartenderjetpack.ui.DrinkDetails
import com.example.bartenderjetpack.ui.handleBack

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BartenderJetpackTheme {
                val viewModel: MainViewModel = viewModel()
                BartenderApp(viewModel = viewModel)
            }
        }
    }
}


fun customPaneScaffoldDirective(currentWindowAdaptiveInfo: WindowAdaptiveInfo): PaneScaffoldDirective {
    val horizontalPartitions = when(currentWindowAdaptiveInfo.windowSizeClass.windowWidthSizeClass) {
        WindowWidthSizeClass.EXPANDED -> 2
        else -> 1
    }

    return PaneScaffoldDirective(
        maxHorizontalPartitions = horizontalPartitions,
        horizontalPartitionSpacerSize = 16.dp,
        maxVerticalPartitions = 1,
        verticalPartitionSpacerSize = 8.dp,
        defaultPanePreferredWidth = 320.dp,
        excludedBounds = emptyList()
    )
}

fun Context.getActivity(): ComponentActivity? = when (this) {
    is ComponentActivity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> null
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun BartenderApp(viewModel: MainViewModel) {
    val scaffoldNavigator = rememberListDetailPaneScaffoldNavigator<Drink>()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val selectedCategory by viewModel.selectedCategory
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    ModalNavigationDrawer(drawerState = drawerState, drawerContent = {
        ModalDrawerSheet() {
            Text("Kategorie drinków", modifier = Modifier.padding(16.dp))
            HorizontalDivider()
            drinkCategories.forEach { category ->
                NavigationDrawerItem(
                    label = { Text(text = category.name) },
                    selected = category == selectedCategory,
                    onClick = {
                        scope.launch {
                        if (selectedCategory != category) {
                            viewModel.clearBackStacks()
                            viewModel.pushCategoryBack(category)
                            scaffoldNavigator.navigateTo(ListDetailPaneScaffoldRole.List,null)
                        }
                        viewModel.setSelectedCategory(category)
                        }
                    },
                    icon = {Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Kategoria: ${category.name}")},
                )
            }
        }
    }) {
        Scaffold(
            contentWindowInsets = WindowInsets.safeContent,
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),

            topBar = {
                CenterAlignedTopAppBar(
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.primary,
                    ),
                    title = {
                        Text(
                            "Drinki",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    navigationIcon = {
                        if (selectedCategory != null) {
                            IconButton(onClick = {
                                scope.launch {
                                    handleBack(
                                        scaffoldNavigator,
                                        selectedCategory,
                                        { viewModel.setSelectedCategory(it) },
                                        viewModel,
                                        scope
                                    )
                                }
                            }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Wróć"
                                )
                            }
                        }
                    },
                    actions = {
                        if (selectedCategory != null) {
                            IconButton(onClick = { scope.launch { drawerState.apply { if (isClosed) open() else close() } } }) {
                                Icon(Icons.Filled.Menu, contentDescription = "Menu")
                            }
                        }
                    },
                    scrollBehavior = scrollBehavior,
                )
            },
            bottomBar = {
                BottomAppBar(containerColor = BottomAppBarDefaults.containerColor, contentPadding = PaddingValues(16.dp,0.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly.also { Arrangement.Center }, verticalAlignment = Alignment.CenterVertically){
                        val dark = isSystemInDarkTheme()
                        val color = if (dark) Color.White else Color.Black
                        Icon(
                            painterResource(R.drawable.rounded_category_24),
                            modifier = Modifier.size(36.dp),
                            contentDescription = "Kategorie drinków",
                            tint=color)
                        if (selectedCategory != null){
                            Icon(Icons.AutoMirrored.Default.KeyboardArrowRight, contentDescription = ">",tint=color)
                            Icon(
                                painterResource(R.drawable.baseline_format_list_bulleted_24),
                                modifier = Modifier.size(36.dp),
                                contentDescription = "Lista drinków",
                                tint = color
                            )
                            val isDetailVisible by rememberUpdatedState(newValue = scaffoldNavigator.currentDestination?.pane == ListDetailPaneScaffoldRole.Detail)
                            if (isDetailVisible){
                                Icon(Icons.AutoMirrored.Default.KeyboardArrowRight, contentDescription = ">",tint=color)
                                Icon(
                                    painterResource(R.drawable.baseline_local_bar_24),
                                    modifier = Modifier.size(36.dp),
                                    contentDescription = "Szczególy drinka",
                                    tint = color
                                )
                            }
                        }
                    }
                }
            }
        ) { innerPadding ->
            BartenderAppBody(
                innerPadding,
                scaffoldNavigator,
                selectedCategory,
                { viewModel.setSelectedCategory(it) },
                viewModel
            )
        }
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun BartenderAppBody(
    paddingValues: PaddingValues,
    scaffoldNavigator: ThreePaneScaffoldNavigator<Drink>,
    selectedCategory: DrinkCategory?,
    changeCategory: (DrinkCategory?) -> Unit,
    viewModel: MainViewModel
) {
    val scope = rememberCoroutineScope()
    val customScaffoldDirective = customPaneScaffoldDirective(currentWindowAdaptiveInfo())
    val activity = LocalContext.current.getActivity()
    BackHandler {
        val handled = handleBack(
            scaffoldNavigator,
            selectedCategory,
            changeCategory,
            viewModel,
            scope
        )
        if (!handled) {
            activity?.finish()
        }
    }

    Box(modifier = Modifier.fillMaxSize().padding(paddingValues).pointerInput(scaffoldNavigator.currentDestination, selectedCategory) {
        detectHorizontalDragGestures(
            onHorizontalDrag = { _, dragAmount ->

                if (dragAmount > 50) {
                    handleBack(
                        scaffoldNavigator,
                        selectedCategory,
                        changeCategory,
                        viewModel,
                        scope
                    )
                } else if (dragAmount < -50) {
                    viewModel.popCategoryBack()?.let {
                        changeCategory(it)
                    } ?: viewModel.popBack()?.let {
                        scope.launch {
                            scaffoldNavigator.navigateTo(
                                ListDetailPaneScaffoldRole.Detail,
                                it
                            )
                        }
                    }
                }
            }
        )
    }) {

        if (selectedCategory == null) {

            CategoryCards {
                viewModel.clearBackStacks()
                changeCategory(it)
            }

        } else {
            NavigableListDetailPaneScaffold(
                modifier = Modifier
                    .fillMaxSize(),
                navigator = scaffoldNavigator,
                listPane = {
                    AnimatedPane(modifier = Modifier.fillMaxSize()) {
                        CategoryDetailView(selectedCategory) { item ->
                            viewModel.clearBackStacks()
                            scope.launch {
                                scaffoldNavigator.navigateTo(
                                    ListDetailPaneScaffoldRole.Detail,
                                    item
                                )
                            }
                        }
                    }
                },
                detailPane = {
                    AnimatedPane(modifier = Modifier.fillMaxSize()) {
                        scaffoldNavigator.currentDestination?.contentKey?.let {
                            DrinkDetails(it)
                            if (viewModel.backStack.isEmpty()) viewModel.pushBack(it)
                        }
                    }
                }
            )
        }
    }
}








