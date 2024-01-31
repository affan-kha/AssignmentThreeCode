package com.muhammadaffankhan24333assignment3.app.ui.activities

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.muhammadaffankhan24333assignment3.app.R
import com.muhammadaffankhan24333assignment3.app.databinding.ActivityMainBinding
import com.muhammadaffankhan24333assignment3.app.interfaces.OnItemClickListener
import com.muhammadaffankhan24333assignment3.app.models.User
import com.muhammadaffankhan24333assignment3.app.ui.adapters.UserAdapter
import com.muhammadaffankhan24333assignment3.app.utils.WrapContentLinearLayoutManager
import com.muhammadaffankhan24333assignment3.app.viewmodel.MainViewModel


class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var context:Context

    private val mainViewModel: MainViewModel by viewModels()

    private var userList = mutableListOf<User>()

    private lateinit var userAdapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        context = this

        setUpToolbar()
        mainViewModel.fetchUserDetails(loggedUserId)
        binding.usersRecyclerview.apply {
            layoutManager = WrapContentLinearLayoutManager(context,RecyclerView.VERTICAL,false)
            hasFixedSize()
            userAdapter = UserAdapter(userList,object: OnItemClickListener {
                override fun onItemClick(user: User) {
                    Intent(context, ChatActivity::class.java).apply {
                        putExtra("EXTRA_USER", user)
                        startActivity(this)
                    }
                }
            })
            adapter = userAdapter
            // Add item dividers
            addItemDecoration(getDividerItemDecoration())
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            fetchUsers()
        }

        // FETCH USER LIST
        fetchUsers()
    }

    private fun fetchUsers(){
        if (!binding.swipeRefreshLayout.isRefreshing){
            binding.loadingSpinner.visibility = View.VISIBLE
        }

        mainViewModel.getAllUsers(loggedUserId){ success,list->
            binding.loadingSpinner.visibility = View.GONE

            binding.swipeRefreshLayout.isRefreshing = false

            if(success){

                if(list!!.isNotEmpty()){
                    userList.clear()
                }
                userList.addAll(list)
                userAdapter.notifyItemRangeChanged(0,userList.size)

            }
            toggleViewsVisibility()

        }
    }

    private fun toggleViewsVisibility() {
        if (userList.isEmpty()) {
            binding.usersRecyclerview.visibility = View.GONE
            binding.emptyView.visibility = View.VISIBLE
        } else {
            binding.usersRecyclerview.visibility = View.VISIBLE
            binding.emptyView.visibility = View.GONE
        }
    }

    private fun getDividerItemDecoration(): DividerItemDecoration {
        val itemDivider: Drawable? = ContextCompat.getDrawable(this, R.drawable.item_divider)
        val dividerItemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)

        itemDivider?.let {
            dividerItemDecoration.setDrawable(it)
        }

        return dividerItemDecoration
    }

    private fun setUpToolbar(){
        setSupportActionBar(binding.toolbar)
        binding.toolbar.setTitleTextColor(getColor(R.color.white))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.logout){
            showLogoutConfirmationDialog()
        }
        else if(item.itemId == R.id.profile){
            Intent(context, ProfileActivity::class.java).apply {
                putExtra("EXTRA_USER", loggedUser)
                startActivity(this)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showLogoutConfirmationDialog() {
        val builder = AlertDialog.Builder(this)

        builder.setTitle("Logout Confirmation")
        builder.setMessage("Are you sure you want to logout?")

        // Positive button (Yes/Logout)
        builder.setPositiveButton("Yes") { dialog, which ->
            mainViewModel.logout()
            Intent(context, SignInActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(this)
            }.apply {
                finish()
            }
            dialog.dismiss()
        }

        // Negative button (No/Cancel)
        builder.setNegativeButton("No") { dialog, which ->
            // User clicked No, do nothing or handle accordingly
            dialog.dismiss()
        }

        val dialog: AlertDialog = builder.create()

        // Show the dialog
        dialog.show()
    }
}