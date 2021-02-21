package com.example.shoppinglistapp.Data.ViewModels

import androidx.lifecycle.*
import com.example.shoppinglistapp.Data.Entities.ShoppingItem
import com.example.shoppinglistapp.Data.Repositories.ShoppingItemsRepository
import kotlinx.coroutines.launch

class ShoppingItemViewModel(private val repository: ShoppingItemsRepository) : ViewModel() {

    val allItems: LiveData<List<ShoppingItem>> = repository.allItems.asLiveData()

    fun insertItem(item: ShoppingItem) = viewModelScope.launch {
        repository.insert(item)
    }

    fun updateItem(item: ShoppingItem) = viewModelScope.launch {
        repository.update(item)
    }

    fun deleteItem(item: ShoppingItem) = viewModelScope.launch {
        repository.delete(item)
    }

    fun deleteAll() = viewModelScope.launch {
        repository.deleteAll()
    }
}

class ShoppingItemViewModelFactory(private val repository: ShoppingItemsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShoppingItemViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ShoppingItemViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}