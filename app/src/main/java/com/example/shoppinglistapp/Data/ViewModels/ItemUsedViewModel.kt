package com.example.shoppinglistapp.Data.ViewModels

import androidx.lifecycle.*
import com.example.shoppinglistapp.Data.Entities.ItemUsed
import com.example.shoppinglistapp.Data.Entities.ShoppingItem
import com.example.shoppinglistapp.Data.Repositories.ItemsUsedRepository
import com.example.shoppinglistapp.Data.Repositories.ShoppingItemsRepository
import kotlinx.coroutines.launch

class ItemUsedViewModel(private val repository: ItemsUsedRepository) : ViewModel() {
    val allUsedItems: LiveData<List<ItemUsed>> = repository.allUsedItems.asLiveData()

    fun insertItem(item: ItemUsed) = viewModelScope.launch {
        repository.insert(item)
    }

    fun updateItem(item: ItemUsed) = viewModelScope.launch {
        repository.update(item)
    }

    fun deleteItem(item: ItemUsed) = viewModelScope.launch {
        repository.delete(item)
    }

    fun deleteAll() = viewModelScope.launch {
        repository.deleteAll()
    }
}

class ItemUsedViewModelFactory(private val repository: ItemsUsedRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ItemUsedViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ItemUsedViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}