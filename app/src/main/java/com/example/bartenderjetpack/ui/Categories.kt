package com.example.bartenderjetpack.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.bartenderjetpack.R
import com.example.bartenderjetpack.drinkCategories
import com.example.bartenderjetpack.model.Drink
import com.example.bartenderjetpack.model.DrinkCategory

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
fun CategoryCards(onCategoryClick: (DrinkCategory) -> Unit) {
    Column(
//        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        drinkCategories.forEach { category ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clickable { onCategoryClick(category) },
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Row(modifier = Modifier.padding(16.dp)) {
                    Image(
                        painter = painterResource(id = R.drawable.icon),
                        contentDescription = category.name,
                        modifier = Modifier.fillMaxHeight().width(100.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.Center
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
}
