package com.example.shoppinglistapp

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.example.shoppinglistapp.Application.ShoppingListApplication
import com.example.shoppinglistapp.Data.Entities.ShoppingItem
import com.example.shoppinglistapp.Data.ViewModels.ItemUsedViewModel
import com.example.shoppinglistapp.Data.ViewModels.ItemUsedViewModelFactory
import com.example.shoppinglistapp.Data.ViewModels.ShoppingItemViewModel
import com.example.shoppinglistapp.Data.ViewModels.ShoppingItemViewModelFactory
import java.lang.StringBuilder

class MainActivity : AppCompatActivity() {

    private val shoppingItemViewModel: ShoppingItemViewModel by viewModels {
        ShoppingItemViewModelFactory((this.application as ShoppingListApplication).repository)
    }

    private val itemUsedViewModel: ItemUsedViewModel by viewModels {
        ItemUsedViewModelFactory((this.application as ShoppingListApplication).itemUsedRepository)
    }

    private lateinit var allItemsList : List<ShoppingItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        supportActionBar?.setBackgroundDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.toolbar_gradient));

        shoppingItemViewModel.allItems.observe(this, Observer { items ->
            allItemsList = items
        })
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
            R.id.actionShare -> {
                createShareActivity()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun createShareActivity() {
        if(!allItemsList.isEmpty()) {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, getListContentString())
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        } else {
            showEmptyListAlert()
        }
    }

    private fun getListContentString() : String {

        val sb = StringBuilder()

        for (i in allItemsList.indices) {
            if(i == allItemsList.count() - 1) {
                sb.append(allItemsList[i].item)
            } else {
                sb.append(allItemsList[i].item + ", ")
            }
        }

        return sb.toString()
    }

    private fun showEmptyListAlert() {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle(getString(R.string.empty_list_alert_title))
        dialogBuilder.setMessage(getString(R.string.empty_list_alert_message))
        dialogBuilder.setPositiveButton(R.string.ok) { _, _ -> }
        dialogBuilder.create()
        dialogBuilder.show()
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