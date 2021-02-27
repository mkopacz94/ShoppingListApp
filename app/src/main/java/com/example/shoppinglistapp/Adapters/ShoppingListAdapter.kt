package com.example.shoppinglistapp.Adapters

import android.graphics.Paint
import android.os.Build
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglistapp.Data.Entities.ShoppingItem
import com.example.shoppinglistapp.R


interface AdapterButtonsCallback {
    fun deleteItemOnClickCallback(itemModel: ShoppingItem)
    fun checkboxClickCallback(itemModel: ShoppingItem, checked: Boolean)
    fun itemNameUpdatedCallback(itemModel: ShoppingItem, newName: String)
    fun emptyItemAlertCallback()
    fun hideKeyboard()
}

class ShoppingListAdapter(private var shoppingList: List<ShoppingItem>,
    private val adapterButtonsCallback: AdapterButtonsCallback)
    : RecyclerView.Adapter<ShoppingListAdapter.ViewHolder>(){

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemLayout: ConstraintLayout = view.findViewById(R.id.itemLayout)
        val itemCheckBox: CheckBox = view.findViewById(R.id.itemCheckbox)
        val editItemButton: ImageButton = view.findViewById(R.id.editItemButton)
        val deleteItemButton: ImageButton = view.findViewById(R.id.deleteItemButton)

        val editItemLayout: ConstraintLayout = view.findViewById(R.id.editItemLayout)
        val itemEditText: EditText = view.findViewById(R.id.itemEditText)
        val acceptEditButton: ImageButton = view.findViewById(R.id.acceptEditButton)
        val cancelEditButton: ImageButton = view.findViewById(R.id.cancelEditButton)
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.shopping_list_item, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        viewHolder.itemLayout.visibility = View.VISIBLE
        viewHolder.editItemLayout.visibility = View.GONE

        viewHolder.itemCheckBox.text = shoppingList[position].item
        changeItemState(viewHolder.itemCheckBox, shoppingList[position].bought)

        viewHolder.itemCheckBox.setOnClickListener {
            adapterButtonsCallback.checkboxClickCallback(
                    shoppingList[position],
                    viewHolder.itemCheckBox.isChecked)
            adapterButtonsCallback.hideKeyboard()
        }

        viewHolder.editItemButton.setOnClickListener {
            viewHolder.itemLayout.visibility = View.GONE
            viewHolder.editItemLayout.visibility = View.VISIBLE
            viewHolder.itemEditText.setText(viewHolder.itemCheckBox.text)
        }
        viewHolder.deleteItemButton.setOnClickListener {
            adapterButtonsCallback.deleteItemOnClickCallback(shoppingList[position])
            adapterButtonsCallback.hideKeyboard()
        }
        viewHolder.acceptEditButton.setOnClickListener {
            tryToUpdateItemName(
                viewHolder.itemEditText.text.toString(),
                position,
                viewHolder
            )
        }
        viewHolder.itemEditText.setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                tryToUpdateItemName(
                    viewHolder.itemEditText.text.toString(),
                    position,
                    viewHolder
                )
                return@OnKeyListener true
            }
            false
        })
        viewHolder.cancelEditButton.setOnClickListener {
            viewHolder.itemLayout.visibility = View.VISIBLE
            viewHolder.editItemLayout.visibility = View.GONE
            adapterButtonsCallback.hideKeyboard()
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = shoppingList.size

    fun update(newList: List<ShoppingItem>) {
        shoppingList = newList
        notifyDataSetChanged()
    }

    private fun changeItemState(itemCheckbox: CheckBox, checked: Boolean) {
        itemCheckbox.isChecked = checked

        if(checked) {
            itemCheckbox.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG or itemCheckbox.paintFlags
        } else {
            itemCheckbox.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG.inv() and itemCheckbox.paintFlags
        }
    }

    private fun tryToUpdateItemName(newName: String, position: Int, viewHolder: ViewHolder) {
        if(newName.isNullOrEmpty()) {
            adapterButtonsCallback.emptyItemAlertCallback()
        } else {
            adapterButtonsCallback.itemNameUpdatedCallback(shoppingList[position],
                viewHolder.itemEditText.text.toString())
            viewHolder.itemLayout.visibility = View.VISIBLE
            viewHolder.editItemLayout.visibility = View.GONE
            adapterButtonsCallback.hideKeyboard()
        }
    }

}
