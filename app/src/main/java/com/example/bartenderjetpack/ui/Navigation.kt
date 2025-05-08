package com.example.bartenderjetpack.ui

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.BackNavigationBehavior
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import com.example.bartenderjetpack.model.MainViewModel
import com.example.bartenderjetpack.model.Drink
import com.example.bartenderjetpack.model.DrinkCategory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
fun handleBack(
    scaffoldNavigator: ThreePaneScaffoldNavigator<Drink>,
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
