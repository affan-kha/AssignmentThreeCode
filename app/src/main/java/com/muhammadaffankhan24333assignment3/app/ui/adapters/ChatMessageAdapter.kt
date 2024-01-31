package com.muhammadaffankhan24333assignment3.app.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.muhammadaffankhan24333assignment3.app.databinding.ItemChatMessageReplyBinding
import com.muhammadaffankhan24333assignment3.app.databinding.ItemChatMessageSenderBinding
import com.muhammadaffankhan24333assignment3.app.models.ChatMessage
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChatMessageAdapter(private val messages: List<ChatMessage>, private val senderId: String) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class MessageSenderViewHolder(private val binding: ItemChatMessageSenderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindData(message: ChatMessage) {
            binding.messageTextView.text = message.message
            binding.timestampTextView.text = SimpleDateFormat("hh:mm a", Locale.getDefault())
                .format(Date(message.timestamp))
        }
    }

    class MessageReplyViewHolder(private val binding: ItemChatMessageReplyBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindData(message: ChatMessage) {
            binding.messageTextView.text = message.message
            binding.timestampTextView.text = SimpleDateFormat("hh:mm a", Locale.getDefault())
                .format(Date(message.timestamp))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == VIEW_TYPE_SENDER) {
            val view =
                ItemChatMessageSenderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            return MessageSenderViewHolder(view)
        }
        else{
            val view =
                ItemChatMessageReplyBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            return MessageReplyViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]
        if(getItemViewType(position) == VIEW_TYPE_SENDER){
            val senderHolder = holder as MessageSenderViewHolder
            senderHolder.bindData(message)
        }
        else{
            val replyHolder = holder as MessageReplyViewHolder
            replyHolder.bindData(message)
        }
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].senderId == senderId) {
            VIEW_TYPE_SENDER
        } else {
            VIEW_TYPE_REPLY
        }
    }

    companion object {
        private const val VIEW_TYPE_SENDER = 1
        private const val VIEW_TYPE_REPLY = 2
    }
}