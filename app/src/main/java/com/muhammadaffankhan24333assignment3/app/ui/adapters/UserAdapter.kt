package com.muhammadaffankhan24333assignment3.app.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.muhammadaffankhan24333assignment3.app.databinding.UserItemRowBinding
import com.muhammadaffankhan24333assignment3.app.interfaces.OnItemClickListener
import com.muhammadaffankhan24333assignment3.app.models.User

class UserAdapter(private val userList: List<User>, private val itemClickListener: OnItemClickListener) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = UserItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]
        holder.bind(user,itemClickListener)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class UserViewHolder(private val binding: UserItemRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User, itemClickListener: OnItemClickListener) {
            binding.userName.text = user.name
            binding.userEmail.text = user.email

            itemView.setOnClickListener {
                itemClickListener.onItemClick(user)
            }
        }
    }
}