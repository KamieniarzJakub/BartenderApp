package com.example.bartenderjetpack

import android.os.Bundle
import android.os.Parcelable
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
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
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.BackNavigationBehavior
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.bartenderjetpack.ui.theme.BartenderJetpackTheme
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.filled.*
import kotlinx.coroutines.delay
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.material.icons.filled.PlayArrow


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
                            scaffoldNavigator.navigateBack(BackNavigationBehavior.PopUntilContentChange)
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
        SampleNavigableListDetailPaneScaffoldFull(innerPadding, scaffoldNavigator)
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun SampleNavigableListDetailPaneScaffoldFull(paddingValues: PaddingValues, scaffoldNavigator: ThreePaneScaffoldNavigator<MyItem>) {
    val scope = rememberCoroutineScope()

    NavigableListDetailPaneScaffold(
        modifier = Modifier.padding(paddingValues),
        navigator = scaffoldNavigator,
        listPane = {
            AnimatedPane {
                MyList(
                    onItemClick = { item ->
                        scope.launch {
                            scaffoldNavigator.navigateTo(
                                ListDetailPaneScaffoldRole.Detail,
                                item
                            )
                        }
                    },
                )
            }
        },
        detailPane = {
            AnimatedPane {
                scaffoldNavigator.currentDestination?.contentKey?.let {
                    MyDetails(it)
                }
            }
        },
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
fun MyList(
    onItemClick: (MyItem) -> Unit,
) {
    Card {
        LazyColumn {
            items(drinks) { drink ->
                ListItem(
                    modifier = Modifier
                        .background(Color.Magenta)
                        .clickable {
                            onItemClick(
                                MyItem(
                                    drinks.indexOf(drink),
                                    drink.name,
                                    drink.ingredients,
                                    drink.recipe
                                )
                            )
                        },
                    headlineContent = {
                        Text(text = drink.name)
                    },
                )
            }
        }
    }
}

@Composable
fun MyDetails(item: MyItem) {
    Column( modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .padding(16.dp)) {
        Text(
            text = "${item.name}\n\nSkładniki:\n${item.ingredients}\n\nSposób przygotowania:\n${item.recipe}",
            modifier = Modifier.padding(bottom = 16.dp)
        )

        TimerFragment()
    }
}


@Parcelize
class MyItem(val id: Int, val name: String, val ingredients: String, val recipe: String) : Parcelable

data class Drink(val name: String, val ingredients: String, val recipe: String)

val drinks = listOf(
    Drink("Woda", "Woda", "Wlać wodę do szklanki."),
    Drink("Woda gazowana", "Woda mineralna, gaz", "Dodać do wody gaz, następnie wymieszać wszystko razem do uzyskania płynnej konsystencji."),
    Drink("Sok pomarańczowy", "Pomarańcze, woda mineralna, cukier", "Wycisnąć pomarańcze za pomocą wyciskarki. Następnie przelać zawartość do szklanki i posłodzić. Uzupełnić brakującą przestrzeń w szklance wodą, następnie wymieszać, aby nikt się nie kapnął."),
    Drink("Mleko", "Krowa", "Zmiksuj wszystkie składniki z lodem i podawaj w wysokiej szklance."),
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
    Drink("Template", "-", "-")
)
