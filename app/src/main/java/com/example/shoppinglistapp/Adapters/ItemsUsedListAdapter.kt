package com.example.shoppinglistapp.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglistapp.Data.Entities.ItemUsed
import com.example.shoppinglistapp.Data.Entities.ShoppingItem
import com.example.shoppinglistapp.R

interface ItemUsedAdapterButtonsCallback {
    fun usedItemClicked(usedItemModel: ItemUsed)
}

class ItemsUsedListAdapter(private var usedItemsList: List<ItemUsed>,
    private val itemUsedAdapterButtonsCallback: ItemUsedAdapterButtonsCallback)
    :  RecyclerView.Adapter<ItemsUsedListAdapter.ViewHolder>(){

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val usedItemButton: Button = view.findViewById(R.id.usedItemButton)
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.used_items_list_item, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.usedItemButton.text = usedItemsList[position].item
        viewHolder.usedItemButton.setOnClickListener {
            itemUsedAdapterButtonsCallback.usedItemClicked(usedItemsList[position])
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = usedItemsList.size

    fun update(newList: List<ItemUsed>) {
        usedItemsList = newList
        notifyDataSetChanged()
    }
}