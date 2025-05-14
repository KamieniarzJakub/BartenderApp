package com.example.bartenderjetpack.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.res.Configuration
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.LocalActivity
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.OverscrollConfiguration
import androidx.compose.foundation.OverscrollEffect
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.overscroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.bartenderjetpack.R
import com.example.bartenderjetpack.drinkCategories
import com.example.bartenderjetpack.model.Drink
import com.example.bartenderjetpack.model.DrinkCategory

fun GetIconResourceForCategory(id: Int): Int {
    return when {
        id == 0 -> R.drawable.baseline_local_bar_24
        id == 1 -> R.drawable.baseline_no_drinks_24
        else -> R.drawable.icon
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CategoryDetailView(category: DrinkCategory, onDrinkClick: (Drink) -> Unit) {
    val context = LocalContext.current
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        userScrollEnabled = true,
    ) {
        itemsIndexed(category.drinks) { index, drink ->
            ListItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onDrinkClick(
                            Drink(
                                name = drink.name,
                                ingredients = drink.ingredients,
                                recipe = drink.recipe,
                                imageUrl = drink.imageUrl
                            )
                        )
                    },
                headlineContent = { Text(drink.name) },
                supportingContent = { Text(drink.ingredients) },
                leadingContent = {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(drink.imageUrl)
                            .crossfade(true)
                            .build(),
                        "Drink",
                        placeholder = painterResource(R.drawable.icon),
                        onError = {
                            Toast.makeText(context, "Błąd ładowania zdjęcia", Toast.LENGTH_SHORT)
                                .show();
                            Log.e(
                                "DrinkDetails",
                                "Błąd ładowania zdjęcia ${drink.imageUrl}",
                                it.result.throwable
                            )
                        },
                        modifier = Modifier.size(40.dp),
                        contentScale = ContentScale.Fit
                    )
                }
            )
        }
    }
}


@Composable
fun CategoryItems(modifier: Modifier, onCategoryClick: (DrinkCategory) -> Unit) {
    drinkCategories.forEachIndexed { index, category ->
        Card(
            modifier = modifier.clickable { onCategoryClick(category) },
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = GetIconResourceForCategory(index)),
                    contentDescription = category.name,
                    modifier = Modifier
                        .weight(0.5f)
                        .fillMaxSize()
                )
                Spacer(modifier = Modifier.size(8.dp))
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
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

enum class LayoutArrangement {
    Column, Row
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun CategoryCards(onCategoryClick: (DrinkCategory) -> Unit) {

    val configuration = LocalConfiguration.current
    val windowSizeClass = calculateWindowSizeClass(LocalActivity.current as Activity)
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    val isCompact = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact
    val isMedium = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Medium
    val layoutArrangement = when {
        isCompact -> LayoutArrangement.Column
        isPortrait && isMedium -> LayoutArrangement.Column
        else -> LayoutArrangement.Row
    }

    // Choose the layout based on orientation
    when (layoutArrangement) {
        LayoutArrangement.Column -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CategoryItems(Modifier.weight(1f), onCategoryClick)
            }
        }

        LayoutArrangement.Row -> {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CategoryItems(Modifier.weight(1f), onCategoryClick)
            }
        }
    }

}
