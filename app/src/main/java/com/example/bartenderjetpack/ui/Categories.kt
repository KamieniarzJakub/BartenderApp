package com.example.bartenderjetpack.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.res.Configuration
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.bartenderjetpack.R
import com.example.bartenderjetpack.drinkCategories
import com.example.bartenderjetpack.getActivity
import com.example.bartenderjetpack.model.Drink
import com.example.bartenderjetpack.model.DrinkCategory
import org.intellij.lang.annotations.JdkConstants

class Categories {
}@Composable
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
fun CategoryItems(modifier: Modifier, onCategoryClick: (DrinkCategory) -> Unit){
    drinkCategories.forEach{ category ->
        Card(
            modifier = modifier.clickable { onCategoryClick(category) },
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.icon),
                    contentDescription = category.name,
                    modifier = Modifier.weight(0.5f).fillMaxSize()
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

enum class LayoutArrangement{
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
    val layoutArrangement = when{
        isCompact -> LayoutArrangement.Column
        isPortrait && isMedium -> LayoutArrangement.Column
        else -> LayoutArrangement.Row
    }

    // Choose the layout based on orientation
    when (layoutArrangement) {
        LayoutArrangement.Column -> {
            Column(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CategoryItems(Modifier.weight(1f),onCategoryClick)
            }
        }

        LayoutArrangement.Row -> {
            Row(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CategoryItems(Modifier.weight(1f),onCategoryClick)
            }
        }
    }

}
