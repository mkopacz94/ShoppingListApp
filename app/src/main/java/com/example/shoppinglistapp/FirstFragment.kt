package com.example.shoppinglistapp

import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglistapp.Adapters.AdapterButtonsCallback
import com.example.shoppinglistapp.Adapters.ShoppingListAdapter
import com.example.shoppinglistapp.Application.ShoppingListApplication
import com.example.shoppinglistapp.Data.Entities.ShoppingItem
import com.example.shoppinglistapp.Data.ViewModels.ShoppingItemViewModel
import com.example.shoppinglistapp.Data.ViewModels.ShoppingItemViewModelFactory

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment(), AdapterButtonsCallback {

    private val shoppingItemViewModel: ShoppingItemViewModel by viewModels {
        ShoppingItemViewModelFactory((requireActivity().application as ShoppingListApplication).repository)
    }

    private lateinit var itemEditText: EditText
    private lateinit var itemsList: RecyclerView
    private val shoppingListAdapter = ShoppingListAdapter(emptyList(), this)

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_first, container, false)

        itemEditText = view.findViewById(R.id.itemEditText)
        itemEditText.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                addFromTextInput()
                return@OnKeyListener true
            }
            false
        })
        itemsList = view.findViewById(R.id.itemsList)
        initializeShoppingList()

        shoppingItemViewModel.allItems.observe(viewLifecycleOwner, Observer { items ->
            refreshItemsList(items)
        })

        view.findViewById<ImageButton>(R.id.addItemButton)
            .setOnClickListener(addItemClickListener)

        return view
    }

    private fun initializeShoppingList() {

        itemsList.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = shoppingListAdapter
        }
    }

    private fun refreshItemsList(shoppingItems: List<ShoppingItem>) {

        shoppingListAdapter.update(shoppingItems)
    }

    private val addItemClickListener = View.OnClickListener { _ ->
        addFromTextInput()
    }

    private fun addFromTextInput() {
        if(!itemEditText.text.isNullOrEmpty()) {
            val itemToAdd = ShoppingItem(0, itemEditText.text.toString(), false)
            shoppingItemViewModel.insertItem(itemToAdd)
            itemEditText.text.clear()
        }
    }

    override fun deleteItemOnClickCallback(itemModel: ShoppingItem) {
        shoppingItemViewModel.deleteItem(itemModel)
    }

    override fun checkboxClickCallback(itemModel: ShoppingItem, checked: Boolean) {
        itemModel.bought = checked
        shoppingItemViewModel.updateItem(itemModel)
    }
}