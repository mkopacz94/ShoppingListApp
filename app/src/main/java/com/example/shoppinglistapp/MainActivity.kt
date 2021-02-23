package com.example.shoppinglistapp

import android.app.AlertDialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.example.shoppinglistapp.Application.ShoppingListApplication
import com.example.shoppinglistapp.Data.ViewModels.ItemUsedViewModel
import com.example.shoppinglistapp.Data.ViewModels.ItemUsedViewModelFactory
import com.example.shoppinglistapp.Data.ViewModels.ShoppingItemViewModel
import com.example.shoppinglistapp.Data.ViewModels.ShoppingItemViewModelFactory

class MainActivity : AppCompatActivity() {

    private val shoppingItemViewModel: ShoppingItemViewModel by viewModels {
        ShoppingItemViewModelFactory((this.application as ShoppingListApplication).repository)
    }

    private val itemUsedViewModel: ItemUsedViewModel by viewModels {
        ItemUsedViewModelFactory((this.application as ShoppingListApplication).itemUsedRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        supportActionBar?.setBackgroundDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.toolbar_gradient));
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.list_toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.actionClearAll -> {
                deleteAllItems()
                true
            }
            R.id.actionClearMostOften -> {
                deleteMostOftenItems()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun deleteAllItems() {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle(getString(R.string.delete_all_alert_title))
        dialogBuilder.setMessage(getString(R.string.delete_all_alert_message))
        dialogBuilder.setPositiveButton(R.string.yes) { _, _ ->
            shoppingItemViewModel.deleteAll()
        }
        dialogBuilder.setNegativeButton(R.string.no) { _,_ -> }
        dialogBuilder.create()
        dialogBuilder.show()
    }

    private fun deleteMostOftenItems() {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle(getString(R.string.delete_most_often_alert_title))
        dialogBuilder.setMessage(getString(R.string.delete_most_often_alert_message))
        dialogBuilder.setPositiveButton(R.string.yes) { _, _ ->
            itemUsedViewModel.deleteAll()
        }
        dialogBuilder.setNegativeButton(R.string.no) { _,_ -> }
        dialogBuilder.create()
        dialogBuilder.show()
    }
}