package com.example.bartenderjetpack.model

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class MainViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _backStack = mutableStateListOf<Drink>()
    val backStack: List<Drink> get() = _backStack

    private val _categoryBackStack = mutableStateListOf<DrinkCategory>()
    val categoryBackStack: List<DrinkCategory> get() = _categoryBackStack

    private val _selectedCategory = mutableStateOf<DrinkCategory?>(null)
    val selectedCategory: State<DrinkCategory?> get() = _selectedCategory

    fun setSelectedCategory(category: DrinkCategory?) {
        _selectedCategory.value = category
    }

    fun pushBack(item: Drink) = _backStack.add(0, item)
    fun popBack(): Drink? = if (_backStack.isNotEmpty()) _backStack.removeAt(0) else null

    fun pushCategoryBack(category: DrinkCategory) = _categoryBackStack.add(0, category)
    fun popCategoryBack(): DrinkCategory? = if (_categoryBackStack.isNotEmpty()) _categoryBackStack.removeAt(0) else null

    fun clearBackStacks() {
        _backStack.clear()
        _categoryBackStack.clear()
    }
}