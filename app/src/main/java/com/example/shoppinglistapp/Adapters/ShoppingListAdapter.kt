package com.example.shoppinglistapp.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglistapp.Data.Entities.ShoppingItem
import com.example.shoppinglistapp.R

interface AdapterButtonsCallback {
    fun deleteItemOnClickCallback(itemModel: ShoppingItem)
    fun checkboxClickCallback(itemModel: ShoppingItem, checked: Boolean)
}

class ShoppingListAdapter(private var shoppingList: List<ShoppingItem>,
    private val adapterButtonsCallback: AdapterButtonsCallback)
    : RecyclerView.Adapter<ShoppingListAdapter.ViewHolder>(){

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemCheckBox: CheckBox = view.findViewById(R.id.itemCheckbox)
        val deleteItemButton: ImageButton = view.findViewById(R.id.deleteItemButton)
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.shopping_list_item, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.itemCheckBox.text = shoppingList[position].item
        viewHolder.itemCheckBox.isChecked = shoppingList[position].bought
        viewHolder.itemCheckBox.setOnClickListener {
            adapterButtonsCallback.checkboxClickCallback(
                shoppingList[position],
                (it as CheckBox).isChecked)
        }
        viewHolder.deleteItemButton.setOnClickListener {
            adapterButtonsCallback.deleteItemOnClickCallback(shoppingList[position])
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = shoppingList.size

    fun update(newList: List<ShoppingItem>) {
        shoppingList = newList
        notifyDataSetChanged()
    }
}
