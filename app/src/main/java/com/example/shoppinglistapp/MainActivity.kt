package com.example.shoppinglistapp

import android.app.AlertDialog
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.example.shoppinglistapp.Application.ShoppingListApplication
import com.example.shoppinglistapp.Data.ViewModels.ShoppingItemViewModel
import com.example.shoppinglistapp.Data.ViewModels.ShoppingItemViewModelFactory

class MainActivity : AppCompatActivity() {

    private val shoppingItemViewModel: ShoppingItemViewModel by viewModels {
        ShoppingItemViewModelFactory((this.application as ShoppingListApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        supportActionBar?.setBackgroundDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.toolbar_gradient));

        val deleteAllButton = findViewById<FloatingActionButton>(R.id.deleteAllButton)
        deleteAllButton.setOnClickListener(deleteAllClickListener)
    }

    private val deleteAllClickListener = View.OnClickListener { _ ->
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
}