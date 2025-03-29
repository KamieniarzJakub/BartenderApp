package com.example.bartenderjetpack

import android.os.Bundle
import android.os.Parcelable
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bartenderjetpack.ui.theme.BartenderJetpackTheme
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BartenderJetpackTheme {
                SampleNavigableListDetailPaneScaffoldFull()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Preview
@Composable
fun SampleNavigableListDetailPaneScaffoldFull() {
    val scaffoldNavigator = rememberListDetailPaneScaffoldNavigator<MyItem>()
    val scope = rememberCoroutineScope()

    NavigableListDetailPaneScaffold(
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
                            onItemClick(MyItem(drinks.indexOf(drink), drink.name, drink.ingredients, drink.recipe))
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
    Text(
        text = "${item.name}\n\nSkładniki:\n${item.ingredients}\n\nSposób przygotowania:\n${item.recipe}",
        modifier = Modifier.padding(16.dp)
    )
}

@Parcelize
class MyItem(val id: Int, val name: String, val ingredients: String, val recipe: String) : Parcelable

data class Drink(val name: String, val ingredients: String, val recipe: String)

val drinks = listOf(
    Drink("Woda", "Woda", "Wlać wodę do szklanki."),
    Drink("Woda gazowana", "Woda mineralna, gaz", "Dodać do wody gaz, następnie wymieszać wszystko razem do uzyskania płynnej konsystencji."),
    Drink("Sok pomarańczowy", "Pomarańcze, woda mineralna, cukier", "Wycisnąć pomarańcze za pomocą wyciskarki. Następnie przelać zawartość do szklanki i posłodzić. Uzupełnić brakującą przestrzeń w szklance wodą, następnie wymieszać, aby nikt się nie kapnął."),
    Drink("Mleko", "Krowa", "Zmiksuj wszystkie składniki z lodem i podawaj w wysokiej szklance.")
)
