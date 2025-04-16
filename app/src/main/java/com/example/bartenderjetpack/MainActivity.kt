package com.example.bartenderjetpack

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BartenderJetpackTheme {
                CenterAlignedTopAppBarExample()
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun CenterAlignedTopAppBarExample() {
    val scaffoldNavigator = rememberListDetailPaneScaffoldNavigator<MyItem>()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val scope = rememberCoroutineScope()
    val context = LocalContext.current;
    var selectedCategory by remember { mutableStateOf<DrinkCategory?>(null) }

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
                    IconButton(onClick = {
                        scope.launch {
                            if (scaffoldNavigator.currentDestination?.pane == ListDetailPaneScaffoldRole.List){
                                if (selectedCategory == null){
                                    scaffoldNavigator.navigateBack();
                                } else {
                                    selectedCategory = null;
                                }
                            } else if (scaffoldNavigator.currentDestination?.pane == ListDetailPaneScaffoldRole.Detail) {
                                scaffoldNavigator.navigateBack(BackNavigationBehavior.PopUntilContentChange)
                            } else {
                                Toast.makeText(context, "Unknown", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Localized description"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* do something */ }) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = "Localized description"
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
    ) {
            innerPadding ->
        SampleNavigableListDetailPaneScaffoldFull(innerPadding, scaffoldNavigator,selectedCategory,{ category -> selectedCategory = category})
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun SampleNavigableListDetailPaneScaffoldFull(
    paddingValues: PaddingValues,
    scaffoldNavigator: ThreePaneScaffoldNavigator<MyItem>,
    selectedCategory: DrinkCategory?,
    changeCategory: (DrinkCategory) -> Unit
) {
    val scope = rememberCoroutineScope()

    NavigableListDetailPaneScaffold(
        modifier = Modifier.padding(paddingValues),
        navigator = scaffoldNavigator,
        listPane = {
            AnimatedPane {
                if (selectedCategory == null) {
                    CategoryCards { category ->
                        changeCategory(category)
                    }
                } else {
                    CategoryDetailView(category = selectedCategory) { item ->
                        scope.launch {
                            scaffoldNavigator.navigateTo(
                                ListDetailPaneScaffoldRole.Detail,
                                item
                            )
                        }
                    }
                }
            }
        },
        detailPane = {
            AnimatedPane {
                scaffoldNavigator.currentDestination?.contentKey?.let {
                    MyDetails(it)
                }
            }
        }
    )
}


@Composable
fun TimerFragment() {
    var totalTime by rememberSaveable { mutableStateOf(60) } // czas w sekundach
    var timeLeft by rememberSaveable { mutableStateOf(totalTime) }
    var isRunning by rememberSaveable { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    // uruchamianie minutnika
    LaunchedEffect(isRunning) {
        if (isRunning) {
            while (isRunning && timeLeft > 0) {
                delay(1000)
                timeLeft--
            }
            isRunning = false
        }
    }

    // główny widok z przewijaniem w pionie
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Minutnik: ${timeLeft}s",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Suwak ustawiania czasu (blokowany jeśli minutnik działa)
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

        Text("Ustaw czas: ${totalTime}s", modifier = Modifier.padding(8.dp))

        // Przycisk Start / Stop / Reset
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            IconButton(onClick = { isRunning = true }) {
                Icon(Icons.Default.PlayArrow, contentDescription = "Start")
            }
            IconButton(onClick = { isRunning = false }) {
                Icon(Icons.Default.Clear, contentDescription = "Stop")
            }
            IconButton(onClick = {
                isRunning = false
                timeLeft = totalTime
            }) {
                Icon(Icons.Default.Refresh, contentDescription = "Przerwij")
            }
        }
    }
}

@Composable
fun CategoryDetailView(category: DrinkCategory, onDrinkClick: (MyItem) -> Unit) {
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




@Composable
fun MyDetails(item: MyItem) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.icon),
            contentDescription = item.name,
            modifier = Modifier.fillMaxWidth().height(200.dp)
        )
        Text(
            text = item.name,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
        )

        Text(
            text = "Składniki:\n${item.ingredients}",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = "Sposób przygotowania:\n${item.recipe}",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        TimerFragment()
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