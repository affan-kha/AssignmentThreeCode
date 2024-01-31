package com.muhammadaffankhan24333assignment3.app.ui.activities

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import com.muhammadaffankhan24333assignment3.app.R
import com.muhammadaffankhan24333assignment3.app.databinding.ActivityProfileBinding
import com.muhammadaffankhan24333assignment3.app.models.User


class ProfileActivity : BaseActivity() {

    private lateinit var binding: ActivityProfileBinding

    private lateinit var context: Context

    private var user: User?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        context = this

        if (intent != null && intent.hasExtra("EXTRA_USER")){
            user = intent.getSerializableExtra("EXTRA_USER") as User
        }

        setUpToolbar()

        user?.apply {
            binding.nameView.text = user?.name
            binding.userNameView.text = user?.username
            binding.emailView.text = user?.email
        }
    }

    private fun setUpToolbar(){
        setSupportActionBar(binding.toolbar)
        binding.toolbar.apply {
            setTitleTextColor(getColor(R.color.white))
        }
        supportActionBar?.apply {
            title = getString(R.string.personal_info)
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_back_arrow_white)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home){
            onBackPressedDispatcher.onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}