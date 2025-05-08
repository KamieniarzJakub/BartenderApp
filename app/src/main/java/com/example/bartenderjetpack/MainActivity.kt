package com.example.bartenderjetpack

import android.content.Context
import android.content.ContextWrapper
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.window.core.layout.WindowWidthSizeClass
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bartenderjetpack.model.Drink
import com.example.bartenderjetpack.model.DrinkCategory
import com.example.bartenderjetpack.model.MainViewModel
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

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),

        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(
                        "Centered Top App Bar",
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
                    IconButton(onClick = { /* Placeholder */ }) {
                        Icon(Icons.Filled.Menu, contentDescription = "Menu")
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
    ) { innerPadding ->
        SampleNavigableListDetailPaneScaffoldFull(
            innerPadding,
            scaffoldNavigator,
            selectedCategory,
            { viewModel.setSelectedCategory(it) },
            viewModel
        )
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun SampleNavigableListDetailPaneScaffoldFull(
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

    Box(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
            .pointerInput(scaffoldNavigator.currentDestination, selectedCategory) {
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
            }
    ) {
        NavigableListDetailPaneScaffold(
            modifier = Modifier.fillMaxSize(),
            navigator = scaffoldNavigator,
            listPane = {
                AnimatedPane {
                    if (selectedCategory == null) {
                        CategoryCards {
                            viewModel.clearBackStacks()
                            changeCategory(it)
                        }
                    } else {
                        CategoryDetailView(selectedCategory) { item ->
                            viewModel.clearBackStacks()
                            scope.launch {
                                scaffoldNavigator.navigateTo(ListDetailPaneScaffoldRole.Detail, item)
                            }
                        }
                    }
                }
            },
            detailPane = {
                AnimatedPane {
                    scaffoldNavigator.currentDestination?.contentKey?.let {
                        DrinkDetails(it)
                        if (viewModel.backStack.isEmpty()) viewModel.pushBack(it)
                    }
                }
            }
        )
    }
}



@Composable
fun CategoryDetailView(category: DrinkCategory, onDrinkClick: (Drink) -> Unit) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(category.drinks) { drink ->
            ListItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onDrinkClick(
                            Drink(
                                name = drink.name,
                                ingredients = drink.ingredients,
                                recipe = drink.recipe
                            )
                        )
                    },
                headlineContent = { Text(drink.name) },
                supportingContent = { Text(drink.ingredients) },
                leadingContent = {
                    Image(
                        painter = painterResource(id = R.drawable.icon),
                        contentDescription = "Obrazek koktajlu",
                        modifier = Modifier.size(40.dp)
                    )
                }
            )
        }
    }
}



@Composable
fun CategoryCards(onCategoryClick: (DrinkCategory) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(drinkCategories.size) { index ->
            val category = drinkCategories[index]
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onCategoryClick(category) },
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Image(
                        painter = painterResource(id = R.drawable.icon),
                        contentDescription = category.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = category.name,
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = category.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}





