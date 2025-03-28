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
import androidx.compose.material3.Card
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.PaneScaffoldDirective
import androidx.compose.material3.adaptive.navigation.BackNavigationBehavior
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldPredictiveBackHandler
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
@Composable
fun SampleNavigableListDetailPaneScaffoldParts() {
    // [START android_compose_adaptivelayouts_sample_list_detail_pane_scaffold_part02]
    val scaffoldNavigator = rememberListDetailPaneScaffoldNavigator<MyItem>()
    val scope = rememberCoroutineScope()
    // [END android_compose_adaptivelayouts_sample_list_detail_pane_scaffold_part02]

    // [START android_compose_adaptivelayouts_sample_list_detail_pane_scaffold_part03]
    NavigableListDetailPaneScaffold(
        navigator = scaffoldNavigator,
        // [START_EXCLUDE]
        listPane = {},
        detailPane = {},
        // [END_EXCLUDE]
    )
    // [END android_compose_adaptivelayouts_sample_list_detail_pane_scaffold_part03]

    // [START android_compose_adaptivelayouts_sample_list_detail_pane_scaffold_part04]
    NavigableListDetailPaneScaffold(
        navigator = scaffoldNavigator,
        listPane = {
            AnimatedPane {
                MyList(
                    onItemClick = { item ->
                        // Navigate to the detail pane with the passed item
                        scope.launch {
                            scaffoldNavigator
                                .navigateTo(
                                    ListDetailPaneScaffoldRole.Detail,
                                    item
                                )
                        }
                    },
                )
            }
        },
        // [START_EXCLUDE]
        detailPane = {},
        // [END_EXCLUDE]
    )
    // [END android_compose_adaptivelayouts_sample_list_detail_pane_scaffold_part04]

    // [START android_compose_adaptivelayouts_sample_list_detail_pane_scaffold_part05]
    NavigableListDetailPaneScaffold(
        navigator = scaffoldNavigator,
        // [START_EXCLUDE]
        listPane = {},
        // [END_EXCLUDE]
        detailPane = {
            AnimatedPane {
                scaffoldNavigator.currentDestination?.contentKey?.let {
                    MyDetails(it)
                }
            }
        },
    )
    // [END android_compose_adaptivelayouts_sample_list_detail_pane_scaffold_part05]
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Preview
@Composable
fun SampleNavigableListDetailPaneScaffoldFull() {
    // [START android_compose_adaptivelayouts_sample_list_detail_pane_scaffold_full]
    val scaffoldNavigator = rememberListDetailPaneScaffoldNavigator<MyItem>()
    val scope = rememberCoroutineScope()

    NavigableListDetailPaneScaffold(
        navigator = scaffoldNavigator,
        listPane = {
            AnimatedPane {
                MyList(
                    onItemClick = { item ->
                        // Navigate to the detail pane with the passed item
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
                // Show the detail pane content if selected item is available
                scaffoldNavigator.currentDestination?.contentKey?.let {
                    MyDetails(it)
                }
            }
        },
    )
    // [END android_compose_adaptivelayouts_sample_list_detail_pane_scaffold_full]
}

// [END android_compose_adaptivelayouts_sample_list_detail_pane_scaffold_with_pb_full]
@Composable
fun MyList(
    onItemClick: (MyItem) -> Unit,
) {
    Card {
        LazyColumn {
            shortStrings.forEachIndexed { id, string ->
                item {
                    ListItem(
                        modifier = Modifier
                            .background(Color.Magenta)
                            .clickable {
                                onItemClick(MyItem(id))
                            },
                        headlineContent = {
                            Text(
                                text = string,
                            )
                        },
                    )
                }
            }
        }
    }
}

@Composable
fun MyDetails(item: MyItem) {
    val text = shortStrings[item.id]
}

// [START android_compose_adaptivelayouts_sample_list_detail_pane_scaffold_myitem]
@Parcelize
class MyItem(val id: Int) : Parcelable
// [END android_compose_adaptivelayouts_sample_list_detail_pane_scaffold_myitem]

val shortStrings = listOf(
    "Cupcake",
    "Donut",
    "Eclair",
    "Froyo",
    "Gingerbread",
    "Honeycomb",
    "Ice cream sandwich",
    "Jelly bean",
    "Kitkat",
    "Lollipop",
    "Marshmallow",
    "Nougat",
    "Oreo",
    "Pie",
)