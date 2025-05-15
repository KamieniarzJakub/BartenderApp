package com.example.bartenderjetpack

import android.content.Context
import android.content.ContextWrapper
import android.hardware.Sensor
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
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
import androidx.compose.material3.rememberBottomAppBarState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.window.core.layout.WindowWidthSizeClass
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.bartenderjetpack.model.Drink
import com.example.bartenderjetpack.model.DrinkCategory
import com.example.bartenderjetpack.model.MainViewModel
import com.example.bartenderjetpack.ui.CategoryCards
import com.example.bartenderjetpack.ui.CategoryDetailView
import com.example.bartenderjetpack.ui.DrinkDetails
import com.example.bartenderjetpack.ui.LayoutArrangement
import com.example.bartenderjetpack.ui.LayoutArrangement.Column
import com.example.bartenderjetpack.ui.handleBack
import com.example.bartenderjetpack.ui.rememberSensorRotation
import com.example.bartenderjetpack.ui.theme.BartenderJetpackTheme
import kotlinx.coroutines.launch
import androidx.compose.ui.platform.LocalSoftwareKeyboardController


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
    val horizontalPartitions =
        when (currentWindowAdaptiveInfo.windowSizeClass.windowWidthSizeClass) {
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

@Composable
private fun Scrim(onClose: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier
            // handle pointer input
            .pointerInput(onClose) { detectTapGestures { onClose() } }
            // handle accessibility services
            .semantics(mergeDescendants = true) {
                onClick {
                    onClose()
                    true
                }
            }
            // handle physical keyboard input
            .onKeyEvent {
                if (it.key == Key.Escape) {
                    onClose()
                    true
                } else {
                    false
                }
            }
            // draw scrim
            .background(Color.DarkGray.copy(alpha = 0.75f))
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun BartenderApp(viewModel: MainViewModel) {
    val scaffoldNavigator = rememberListDetailPaneScaffoldNavigator<Drink>()
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState()) // https://developer.android.com/develop/ui/compose/components/app-bars#scroll
    val scrollBehaviorBottom = BottomAppBarDefaults.exitAlwaysScrollBehavior(
        rememberBottomAppBarState()
    )
    val selectedCategory by viewModel.selectedCategory
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val isDetailVisible by rememberUpdatedState(newValue = scaffoldNavigator.currentDestination?.pane == ListDetailPaneScaffoldRole.Detail)
    var imageFullScreen by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val targetRotationDegrees by rememberSensorRotation(
        sensorType = Sensor.TYPE_ROTATION_VECTOR,
        maxTiltDegrees = 70f, // Tilting phone up to 25 degrees
        maxImageRotationDegrees = 70f // results in image rotation up to 6 degrees
    )
    val animatedRotationDegrees by animateFloatAsState(
        targetValue = targetRotationDegrees,
        animationSpec = spring( // Use a spring animation for a natural, physics-based feel
            dampingRatio = 0.6f, // How bouncy the spring is (lower = more bouncy)
            stiffness = 50f // How stiff the spring is (lower = slower animation)
            // Adjust these values to get the desired smoothness and responsiveness
        ),
        // animationSpec = tween(durationMillis = 300), // Alternative: fixed duration animation
        label = "imageRotationAnimation" // Optional label for tooling/debugging
    )


    ModalNavigationDrawer(drawerState = drawerState, drawerContent = {
        ModalDrawerSheet {
            var searchQuery by remember { mutableStateOf("") }
            val scrollState = rememberScrollState()
            val keyboardController = LocalSoftwareKeyboardController.current

            Column(modifier = Modifier.verticalScroll(scrollState)) {
                Text("Kategorie drinków", modifier = Modifier.padding(16.dp))

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Szukaj") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    singleLine = true
                )

                HorizontalDivider()

                val filteredCategories = drinkCategories.filter {
                    it.name.contains(searchQuery, ignoreCase = true) ||
                            it.drinks.any { drink -> drink.name.contains(searchQuery, ignoreCase = true) }
                }

                filteredCategories.forEach { category ->
                    Column {
                        NavigationDrawerItem(
                            label = { Text(text = category.name) },
                            selected = category == selectedCategory,
                            onClick = {
                                scope.launch {
                                    if (selectedCategory != category) {
                                        viewModel.clearBackStacks()
                                        viewModel.pushCategoryBack(category)
                                        scaffoldNavigator.navigateTo(
                                            ListDetailPaneScaffoldRole.List,
                                            null
                                        )
                                        viewModel.setSelectedCategory(category)
                                    }
                                    keyboardController?.hide()
                                    searchQuery = ""
                                    drawerState.close()
                                }
                            },
                            icon = {
                                Icon(
                                    Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                    contentDescription = "Kategoria: ${category.name}"
                                )
                            },
                        )

                        category.drinks
                            .filter { it.name.contains(searchQuery, ignoreCase = true) }
                            .forEach { drink ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            scope.launch {
                                                if (selectedCategory != category) {
                                                    viewModel.clearBackStacks()
                                                    viewModel.pushCategoryBack(category)
                                                    viewModel.setSelectedCategory(category)
                                                }
                                                viewModel.clearBackStacks()
                                                scaffoldNavigator.navigateTo(
                                                    ListDetailPaneScaffoldRole.Detail,
                                                    drink
                                                )
                                                keyboardController?.hide()
                                                searchQuery = ""
                                                drawerState.close()
                                            }
                                        }
                                        .padding(start = 32.dp, top = 4.dp, bottom = 4.dp)
                                ) {
                                    Text(drink.name)
                                }
                            }
                    }
                }
            }
        }
    }) {
        Scaffold(
            contentWindowInsets = WindowInsets.safeContent,
            modifier = when {isDetailVisible -> Modifier.nestedScroll(scrollBehavior.nestedScrollConnection) else -> Modifier},
            topBar = {
                when {
                    isDetailVisible -> (
                            Box {
                                val imageUrl = scaffoldNavigator.currentDestination?.contentKey?.imageUrl
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(imageUrl)
                                        .crossfade(true)
                                        .build(),
                                    "Drink",
                                    placeholder = painterResource(R.drawable.icon),
                                    onError = {
                                        Toast.makeText(context, "Błąd ładowania zdjęcia", Toast.LENGTH_SHORT).show();
                                        Log.e("DrinkDetails", "Błąd ładowania zdjęcia ${imageUrl}", it.result.throwable)
                                    },
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.matchParentSize()
                                )
                                LargeTopAppBar(
                                    expandedHeight = 300.dp,
                                    colors = TopAppBarDefaults.topAppBarColors(
                                        containerColor = Color.Transparent,
                                        titleContentColor = MaterialTheme.colorScheme.primary,
                                        actionIconContentColor = MaterialTheme.colorScheme.primary,
                                        navigationIconContentColor = MaterialTheme.colorScheme.primary,
                                        scrolledContainerColor = MaterialTheme.colorScheme.primaryContainer
                                    ),
                                    title = {
                                        Text(
                                            viewModel.peekBack()?.name ?: "???",
                                            maxLines = 3,
                                            overflow = TextOverflow.Ellipsis,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier
                                                .background(
                                                    MaterialTheme.colorScheme.primaryContainer,
                                                    RoundedCornerShape(8.dp)
                                                )
                                                .padding(4.dp)
                                        )
                                    },
                                    navigationIcon = {
                                        if (selectedCategory != null) {
                                            IconButton(
                                                modifier = Modifier
                                                    .background(
                                                        MaterialTheme.colorScheme.primaryContainer,
                                                        RoundedCornerShape(8.dp)
                                                    )
                                                    .padding(4.dp),
                                                onClick = {
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
                                                    modifier = Modifier
                                                        .background(
                                                            MaterialTheme.colorScheme.primaryContainer,
                                                            RoundedCornerShape(8.dp)
                                                        )
                                                        .padding(4.dp),
                                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                                    contentDescription = "Wróć"
                                                )
                                            }
                                        }
                                    },
                                    actions = {
                                        if (selectedCategory != null) {
                                            IconButton(
                                                modifier = Modifier
                                                    .background(
                                                        MaterialTheme.colorScheme.primaryContainer,
                                                        RoundedCornerShape(8.dp)
                                                    )
                                                    .padding(4.dp),
                                                onClick = { scope.launch { drawerState.apply { if (isClosed) open() else close() } } }) {
                                                Icon(Icons.Filled.Menu, contentDescription = "Menu")
                                            }
                                        }
                                    },
                                    scrollBehavior = scrollBehavior,
                                    modifier = Modifier
                                        .clickable(onClick = {
                                            imageFullScreen = true
                                        })
                                )
                            }
                            )

                    else -> (
                            CenterAlignedTopAppBar(
                                colors = TopAppBarDefaults.topAppBarColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                                    titleContentColor = MaterialTheme.colorScheme.primary,
                                ),
                                title = {
                                    Text(
                                        when {
                                            selectedCategory != null -> selectedCategory!!.name
                                            else -> "Drinki"
                                        },
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
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
                            )
                            )
                }

            },
            bottomBar = {
                BottomAppBar(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentPadding = PaddingValues(16.dp, 0.dp),
                    scrollBehavior = scrollBehaviorBottom
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly.also { Arrangement.Center },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val dark = isSystemInDarkTheme()
                        val color = if (dark) Color.White else Color.Black
                        Icon(
                            painterResource(R.drawable.rounded_category_24),
                            modifier = Modifier.size(36.dp),
                            contentDescription = "Kategorie drinków",
                            tint = color
                        )
                        if (selectedCategory != null) {
                            Icon(
                                Icons.AutoMirrored.Default.KeyboardArrowRight,
                                contentDescription = ">",
                                tint = color
                            )
                            Icon(
                                painterResource(R.drawable.baseline_format_list_bulleted_24),
                                modifier = Modifier.size(36.dp),
                                contentDescription = "Lista drinków",
                                tint = color
                            )
                            if (isDetailVisible) {
                                Icon(
                                    Icons.AutoMirrored.Default.KeyboardArrowRight,
                                    contentDescription = ">",
                                    tint = color
                                )
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
            Spacer(modifier = Modifier.padding(bottom=BottomAppBarDefaults.ContentPadding.calculateBottomPadding()))
        }
    }

    if (imageFullScreen) {
        var zoomed by remember { mutableStateOf(false) }
        var zoomOffset by remember { mutableStateOf(Offset.Zero) }
        val imageUrl = scaffoldNavigator.currentDestination?.contentKey?.imageUrl
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Scrim({ imageFullScreen = false }, Modifier.fillMaxSize())
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .crossfade(true)
                    .build(),
                "Drink",
                placeholder = painterResource(R.drawable.icon),
                onError = {
                    Toast.makeText(context, "Błąd ładowania zdjęcia", Toast.LENGTH_SHORT).show();
                    Log.e("DrinkDetails", "Błąd ładowania zdjęcia ${imageUrl}", it.result.throwable)
                },
                modifier = Modifier
                    .padding(16.dp)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onDoubleTap = { tapOffset ->
                                zoomOffset = if (zoomed) Offset.Zero else calculateOffset(tapOffset, size)
                                zoomed = !zoomed
                            }
                        )
                    }
                    .graphicsLayer {
                        scaleX = if (zoomed) 2f else 1f
                        scaleY = if (zoomed) 2f else 1f
                        translationX = zoomOffset.x
                        translationY = zoomOffset.y
                        rotationZ = -animatedRotationDegrees
                    },
                contentScale = ContentScale.Fit
            )
        }
    }
}

private fun calculateOffset(tapOffset: Offset, size: IntSize): Offset {
    val offsetX = (-(tapOffset.x - (size.width / 2f)) * 2f)
        .coerceIn(-size.width / 2f, size.width / 2f)
    return Offset(offsetX, 0f)
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .pointerInput(scaffoldNavigator.currentDestination, selectedCategory) {
                detectHorizontalDragGestures(
                    onHorizontalDrag = { _, dragAmount ->

                        if (dragAmount > 50) {
                            Log.d("CustomNavigation","Drag > 50")
                            handleBack(
                                scaffoldNavigator,
                                selectedCategory,
                                changeCategory,
                                viewModel,
                                scope
                            )
                        } else if (dragAmount < -50) {
                            Log.d("CustomNavigation","Drag < -50")
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








