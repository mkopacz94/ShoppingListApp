package com.example.shoppinglistapp

import android.app.AlertDialog
import android.content.Context
import android.media.Image
import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglistapp.Adapters.AdapterButtonsCallback
import com.example.shoppinglistapp.Adapters.ItemUsedAdapterButtonsCallback
import com.example.shoppinglistapp.Adapters.ItemsUsedListAdapter
import com.example.shoppinglistapp.Adapters.ShoppingListAdapter
import com.example.shoppinglistapp.Application.ShoppingListApplication
import com.example.shoppinglistapp.Data.Entities.ItemUsed
import com.example.shoppinglistapp.Data.Entities.ShoppingItem
import com.example.shoppinglistapp.Data.ViewModels.ItemUsedViewModel
import com.example.shoppinglistapp.Data.ViewModels.ItemUsedViewModelFactory
import com.example.shoppinglistapp.Data.ViewModels.ShoppingItemViewModel
import com.example.shoppinglistapp.Data.ViewModels.ShoppingItemViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment(), AdapterButtonsCallback, ItemUsedAdapterButtonsCallback {

    private val shoppingItemViewModel: ShoppingItemViewModel by viewModels {
        ShoppingItemViewModelFactory((requireActivity().application as ShoppingListApplication).repository)
    }

    private val itemUsedViewModel: ItemUsedViewModel by viewModels {
        ItemUsedViewModelFactory((requireActivity().application as ShoppingListApplication).itemUsedRepository)
    }

    private lateinit var itemEditText: EditText
    private lateinit var itemsList: RecyclerView
    private val shoppingListAdapter = ShoppingListAdapter(emptyList(), this)

    private lateinit var usedItemsList: List<ItemUsed>
    private lateinit var usedItemsRecyclerView: RecyclerView
    private val usedItemsListAdapter = ItemsUsedListAdapter(emptyList(), this)

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_first, container, false)

        itemEditText = view.findViewById(R.id.itemEditText)
        itemEditText.setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
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

        usedItemsRecyclerView = view.findViewById(R.id.usedItemsList)
        initializeUsedItemsList()

        itemUsedViewModel.allUsedItems.observe(viewLifecycleOwner, Observer { itemsUsed ->
            usedItemsList = itemsUsed
            refreshUsedItemsList(itemsUsed)
            setUsedItemsVisibility(itemsUsed.size)
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

    private fun initializeUsedItemsList() {

        val usedItemsLayoutManager = LinearLayoutManager(activity)
        usedItemsLayoutManager.orientation = LinearLayoutManager.HORIZONTAL

        usedItemsRecyclerView.apply {
            layoutManager = usedItemsLayoutManager
            adapter = usedItemsListAdapter
        }
    }

    private fun refreshItemsList(shoppingItems: List<ShoppingItem>) {

        shoppingListAdapter.update(shoppingItems)

        val cartImageView = view?.findViewById<ImageView>(R.id.cartImageView)

        if(shoppingItems.isEmpty()) {
            cartImageView?.visibility = View.VISIBLE
        } else {
            cartImageView?.visibility = View.GONE
        }
    }

    private fun refreshUsedItemsList(usedItems: List<ItemUsed>) {

        if(usedItemsList.count() >= 10) {
            usedItemsListAdapter.update(usedItems
                    .sortedByDescending { i -> i.timesUsed }
                    .subList(0, 10))
        } else
        {
            usedItemsListAdapter.update(usedItems
                    .sortedByDescending { i -> i.timesUsed })
        }
    }

    private fun setUsedItemsVisibility(usedItemLength: Int) {
        val usedItemsTextView = view?.findViewById<TextView>(R.id.mostOftenTextView)
        val usedItemsRecyclerView = view?.findViewById<RecyclerView>(R.id.usedItemsList)

        if(usedItemLength == 0) {
            usedItemsTextView?.visibility = View.GONE
            usedItemsRecyclerView?.visibility = View.GONE
        } else {
            usedItemsTextView?.visibility = View.VISIBLE
            usedItemsRecyclerView?.visibility = View.VISIBLE
        }
    }

    private val addItemClickListener = View.OnClickListener { _ ->
        addFromTextInput()
    }

    private fun addFromTextInput() {
        if(!itemEditText.text.isNullOrEmpty()) {
            addItemToList(itemEditText.text.toString())
            itemEditText.text.clear()
        }
    }

    private fun addItemToList(itemName: String) {
        val itemToAdd = ShoppingItem(0, itemName, false)
        shoppingItemViewModel.insertItem(itemToAdd)
        addToUsedItems(itemToAdd.item)
    }

    private fun addToUsedItems(itemName: String) {
        val itemToUpdate = usedItemsList.firstOrNull { i ->
            i.item.toLowerCase() == itemName.toLowerCase()
        }

        if(itemToUpdate == null)
        {
            val itemToInsert = ItemUsed(0, itemName.toLowerCase(), 1)
            itemUsedViewModel.insertItem(itemToInsert)
        } else {
            itemToUpdate.timesUsed += 1
            itemUsedViewModel.updateItem(itemToUpdate)
        }
    }

    override fun deleteItemOnClickCallback(itemModel: ShoppingItem) {
        shoppingItemViewModel.deleteItem(itemModel)
    }

    override fun checkboxClickCallback(itemModel: ShoppingItem, checked: Boolean) {
        itemModel.bought = checked
        shoppingItemViewModel.updateItem(itemModel)
    }

    override fun itemNameUpdatedCallback(itemModel: ShoppingItem, newName: String) {
        itemModel.item = newName
        shoppingItemViewModel.updateItem(itemModel)
    }

    override fun emptyItemAlertCallback() {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.setTitle(getString(R.string.empty_item_alert_title))
        dialogBuilder.setMessage(getString(R.string.empty_item_alert_message))
        dialogBuilder.setPositiveButton(R.string.ok) { _, _ -> }
        dialogBuilder.create()
        dialogBuilder.show()
    }

    override fun hideKeyboard() {
        val inputMethodManager = getSystemService(requireContext(), InputMethodManager::class.java)
        inputMethodManager?.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    override fun usedItemClicked(usedItemModel: ItemUsed) {
        addItemToList(usedItemModel.item.capitalize())
        hideKeyboard()
    }
}