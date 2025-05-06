package com.example.bartenderjetpack

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.provider.ContactsContract
import android.telephony.SmsManager
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.BackNavigationBehavior
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
import kotlinx.parcelize.Parcelize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.material.icons.filled.*
import kotlinx.coroutines.delay
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.ui.Alignment
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.CoroutineScope


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BartenderJetpackTheme {
                val viewModel: MainViewModel = viewModel()
                ScaffoldWithNavigationDrawer(viewModel = viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldWithNavigationDrawer(viewModel: MainViewModel) {
    val selectedCategory by viewModel.selectedCategory

    // Setup for Navigation Drawer
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // ModalDrawer for the navigation
    ModalDrawer(
        drawerState = drawerState,
        drawerContent = {
            // Navigation Drawer content
            Column {
                Text(
                    text = "Categories",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(16.dp)
                )
                LazyColumn {
                    items(drinkCategories) { category ->
                        ListItem(
                            modifier = Modifier.clickable {
                                viewModel.setSelectedCategory(category)
                                scope.launch {
                                    drawerState.close()  // Close drawer when category is selected
                                }
                            },
                            headlineContent = { Text(category.name) },
                            supportingContent = { Text(category.description) },
                            leadingContent = {
                                Image(
                                    painter = painterResource(id = R.drawable.icon),
                                    contentDescription = category.name,
                                    modifier = Modifier.size(40.dp)
                                )
                            }
                        )
                    }
                }
            }
        },
        content = {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = {
                    CenterAlignedTopAppBar(
                        title = {
                            Text(
                                text = selectedCategory?.name ?: "Select a Category",
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(Icons.Filled.Menu, contentDescription = "Open Drawer")
                            }
                        }
                    )
                },
                // Wrapping the main content
                content = { padding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
                    ) {
                        // Main content displaying drinks of the selected category
                        if (selectedCategory != null) {
                            CategoryDetailView(category = selectedCategory!!) { item ->
                                // Handle item click (navigate to drink detail)
                            }
                        } else {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(padding),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("Please select a category")
                            }
                        }
                    }
                }
            )
        }
    )
}

@Composable
fun CategoryDetailView(category: DrinkCategory, onDrinkClick: (MyItem) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(category.drinks) { drink ->
            ListItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onDrinkClick(
                            MyItem(
                                id = drink.name.hashCode(),
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


fun getPhoneNumberFromUri(context: Context, contactUri: Uri): String? {
    val contentResolver = context.contentResolver
    val cursor = contentResolver.query(contactUri, null, null, null, null)
    cursor?.use {
        if (it.moveToFirst()) {
            val id = it.getString(it.getColumnIndexOrThrow(ContactsContract.Contacts._ID))
            val hasPhone = it.getInt(it.getColumnIndexOrThrow(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0
            if (hasPhone) {
                val phones = contentResolver.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = ?",
                    arrayOf(id),
                    null
                )
                phones?.use { phoneCursor ->
                    if (phoneCursor.moveToFirst()) {
                        return phoneCursor.getString(phoneCursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER))
                    }
                }
            }
        }
    }
    return null
}

fun sendSms(context: Context, phoneNumber: String, message: String) {
    try {
        val smsManager = SmsManager.getDefault()
        smsManager.sendTextMessage(phoneNumber, null, message, null, null)
        Toast.makeText(context, "Wysłano SMS!", Toast.LENGTH_SHORT).show()
    } catch (e: Exception) {
        Toast.makeText(context, "Błąd wysyłania SMS: ${e.message}", Toast.LENGTH_LONG).show()
    }
}


class MainViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _backStack = mutableStateListOf<MyItem>()
    val backStack: List<MyItem> get() = _backStack

    private val _categoryBackStack = mutableStateListOf<DrinkCategory>()
    val categoryBackStack: List<DrinkCategory> get() = _categoryBackStack

    private val _selectedCategory = mutableStateOf<DrinkCategory?>(null)
    val selectedCategory: State<DrinkCategory?> get() = _selectedCategory

    fun setSelectedCategory(category: DrinkCategory?) {
        _selectedCategory.value = category
    }

    fun pushBack(item: MyItem) = _backStack.add(0, item)
    fun popBack(): MyItem? = if (_backStack.isNotEmpty()) _backStack.removeAt(0) else null

    fun pushCategoryBack(category: DrinkCategory) = _categoryBackStack.add(0, category)
    fun popCategoryBack(): DrinkCategory? = if (_categoryBackStack.isNotEmpty()) _categoryBackStack.removeAt(0) else null

    fun clearBackStacks() {
        _backStack.clear()
        _categoryBackStack.clear()
    }
}



@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun CenterAlignedTopAppBarExample(viewModel: MainViewModel) {
    val scaffoldNavigator = rememberListDetailPaneScaffoldNavigator<MyItem>()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val scope = rememberCoroutineScope()
    val selectedCategory by viewModel.selectedCategory

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text("Centered Top App Bar", maxLines = 1, overflow = TextOverflow.Ellipsis)
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


@SuppressLint("ContextCastToActivity")
@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun SampleNavigableListDetailPaneScaffoldFull(
    paddingValues: PaddingValues,
    scaffoldNavigator: ThreePaneScaffoldNavigator<MyItem>,
    selectedCategory: DrinkCategory?,
    changeCategory: (DrinkCategory?) -> Unit,
    viewModel: MainViewModel
) {
    val scope = rememberCoroutineScope()

    val activity = (LocalContext.current as? Activity)
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
                        MyDetails(it)
                        if (viewModel.backStack.isEmpty()) viewModel.pushBack(it)
                    }
                }
            }
        )
    }
}


@OptIn(ExperimentalMaterial3AdaptiveApi::class)
fun handleBack(
    scaffoldNavigator: ThreePaneScaffoldNavigator<MyItem>,
    selectedCategory: DrinkCategory?,
    onCategoryChange: (DrinkCategory?) -> Unit,
    viewModel: MainViewModel,
    scope: CoroutineScope
): Boolean {
    return when {
        scaffoldNavigator.currentDestination?.pane == ListDetailPaneScaffoldRole.Detail -> {
            val contentKey = scaffoldNavigator.currentDestination?.contentKey
            contentKey?.let { viewModel.pushBack(it) }

            scope.launch {
                scaffoldNavigator.navigateBack(BackNavigationBehavior.PopUntilContentChange)
            }
            true
        }

        selectedCategory != null -> {
            viewModel.pushCategoryBack(selectedCategory)
            onCategoryChange(null)
            true
        }

        else -> {
            false
        }
    }
}



@Composable
fun TimerFragment() {
    var totalTime by rememberSaveable { mutableStateOf(60) }
    var timeLeft by rememberSaveable { mutableStateOf(totalTime) }
    var isRunning by rememberSaveable { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(isRunning) {
        if (isRunning) {
            while (isRunning && timeLeft > 0) {
                delay(1000)
                timeLeft--
            }
            isRunning = false
        }
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Text("Minutnik: ${timeLeft}s", style = MaterialTheme.typography.headlineMedium)
        Slider(
            value = totalTime.toFloat(),
            onValueChange = {
                if (!isRunning) {
                    totalTime = it.toInt()
                    timeLeft = totalTime
                }
            },
            valueRange = 1f..300f,
            steps = 29,
            enabled = !isRunning,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        )
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            IconButton(onClick = { isRunning = true }) { Icon(Icons.Default.PlayArrow, contentDescription = "Start") }
            IconButton(onClick = { isRunning = false }) { Icon(Icons.Default.Clear, contentDescription = "Stop") }
            IconButton(onClick = {
                isRunning = false
                timeLeft = totalTime
            }) { Icon(Icons.Default.Refresh, contentDescription = "Reset") }
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
                    Text(category.name, style = MaterialTheme.typography.headlineSmall)
                    Text(category.description, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}





@Composable
fun MyDetails(item: MyItem) {
    val context = LocalContext.current

    val contactLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickContact()
    ) { uri ->
        uri?.let {
            val phone = getPhoneNumberFromUri(context, it)
            if (phone != null) {
                sendSms(context, phone, "Składniki drinka ${item.name}: ${item.ingredients}")
            } else {
                Toast.makeText(context, "Nie udało się pobrać numeru", Toast.LENGTH_SHORT).show()
            }
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.values.all { it }) {
            contactLauncher.launch(null)
        } else {
            Toast.makeText(context, "Potrzebne uprawnienia", Toast.LENGTH_SHORT).show()
        }
    }

    val hasSmsPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED
    val hasContactPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                if (hasSmsPermission && hasContactPermission) {
                    contactLauncher.launch(null)
                } else {
                    permissionLauncher.launch(arrayOf(
                        Manifest.permission.SEND_SMS,
                        Manifest.permission.READ_CONTACTS
                    ))
                }
            }) {
                Icon(Icons.Default.Send, contentDescription = "Wyślij SMS")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.icon),
                contentDescription = item.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
            Text(item.name, style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(top = 16.dp, bottom = 8.dp))
            Text("Składniki:\n${item.ingredients}", style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(bottom = 8.dp))
            Text("Sposób przygotowania:\n${item.recipe}", style = MaterialTheme.typography.bodyMedium)
            TimerFragment()
        }
    }
}


@Parcelize
class MyItem(val id: Int, val name: String, val ingredients: String, val recipe: String) : Parcelable

data class Drink(val name: String, val ingredients: String, val recipe: String)

data class DrinkCategory(
    val name: String,
    val description: String,
    val drinks: List<Drink>
)

val drinkCategories = listOf(
    DrinkCategory(
        name = "Główna",
        description = "Witaj w aplikacji Bartender! Tutaj znajdziesz przepisy na różne napoje.",
        drinks = emptyList()
    ),
    DrinkCategory(
        name = "Bezalkoholowe",
        description = "Koktajle bezalkoholowe dla każdego!",
        drinks = listOf(
            Drink("Woda", "Woda", "Wlać wodę do szklanki."),
            Drink("Woda gazowana", "Woda mineralna, gaz", "Dodać do wody gaz, następnie wymieszać wszystko razem do uzyskania płynnej konsystencji."),
            Drink("Sok pomarańczowy", "Pomarańcze, woda mineralna, cukier", "Wycisnąć pomarańcze za pomocą wyciskarki. Następnie przelać zawartość do szklanki i posłodzić. Uzupełnić brakującą przestrzeń w szklance wodą, następnie wymieszać, aby nikt się nie kapnął."),
            Drink("Mleko", "Krowa", "Zmiksuj wszystkie składniki z lodem i podawaj w wysokiej szklance."),
        )
    ),
    DrinkCategory(
        name = "Template",
        description = "Do uzupełnienia",
        drinks = listOf(
            Drink("Template", "-", "-"),
            Drink("Template", "-", "-"),
            Drink("Template", "-", "-"),
            Drink("Template", "-", "-"),
            Drink("Template", "-", "-"),
            Drink("Template", "-", "-"),
            Drink("Template", "-", "-"),
            Drink("Template", "-", "-"),
            Drink("Template", "-", "-"),
            Drink("Template", "-", "-"),
            Drink("Template", "-", "-"),
            Drink("Template", "-", "-"),
            Drink("Template", "-", "-"),
            Drink("Template", "-", "-"),
            Drink("Template", "-", "-"),
            Drink("Template", "-", "-"),
            Drink("Template", "-", "-"),
            Drink("Template", "-", "-"),
            Drink("Template", "-", "-"),
            Drink("Template", "-", "-"),
            Drink("Template", "-", "-"),
            Drink("Template", "-", "-"))
    )
)