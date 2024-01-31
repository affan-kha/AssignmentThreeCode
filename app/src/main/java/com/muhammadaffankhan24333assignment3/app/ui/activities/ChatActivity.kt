package com.muhammadaffankhan24333assignment3.app.ui.activities

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.view.View.OnLayoutChangeListener
import androidx.activity.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.muhammadaffankhan24333assignment3.app.R
import com.muhammadaffankhan24333assignment3.app.databinding.ActivityChatBinding
import com.muhammadaffankhan24333assignment3.app.models.ChatMessage
import com.muhammadaffankhan24333assignment3.app.models.User
import com.muhammadaffankhan24333assignment3.app.ui.adapters.ChatMessageAdapter
import com.muhammadaffankhan24333assignment3.app.utils.WrapContentLinearLayoutManager
import com.muhammadaffankhan24333assignment3.app.viewmodel.MainViewModel

class ChatActivity : BaseActivity() {

    private lateinit var binding: ActivityChatBinding

    private lateinit var context: Context

    private var user: User?=null
    private val mainViewModel: MainViewModel by viewModels()
    private val messages: MutableList<ChatMessage> = mutableListOf()
    private lateinit var messageAdapter: ChatMessageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        context = this

        if (intent != null && intent.hasExtra("EXTRA_USER")){
            user = intent.getSerializableExtra("EXTRA_USER") as User
        }

        setUpToolbar()

        binding.sendMessageBtn.setOnClickListener {
            val messageText = binding.editChatMessage.text.toString().trim()
            if (messageText.isNotEmpty()) {
                sendMessage(loggedUserId, messageText)
                binding.editChatMessage.text?.clear()
            }
        }
        binding.chatMessageRecyclerview.layoutManager = WrapContentLinearLayoutManager(this,RecyclerView.VERTICAL,false)
        messageAdapter = ChatMessageAdapter(messages, loggedUserId)
        binding.chatMessageRecyclerview.adapter = messageAdapter
        binding.chatMessageRecyclerview.addOnLayoutChangeListener(OnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            binding.chatMessageRecyclerview.scrollToPosition(
                messages.size - 1
            )
        })
        fetchChatHistory()
    }

    private fun fetchChatHistory(){
        val chatId = generateChatId(loggedUserId,user!!.id)
        mainViewModel.getChatHistory(chatId){ success,list->
            if(success){
                if(messages.isNotEmpty()){
                    messages.clear()
                }
                messages.addAll(list)
                messageAdapter.notifyItemRangeChanged(0,messages.size)
                binding.chatMessageRecyclerview.post {
                    if(messages.isNotEmpty()){
                        binding.chatMessageRecyclerview.smoothScrollToPosition(messages.size-1)
                    }
                }
            }
        }
    }

    private fun sendMessage(senderId: String, messageText: String) {
        val timestamp = System.currentTimeMillis()
//        val message = ChatMessage(senderId, messageText, timestamp)
//        messages.add(message)
//        messageAdapter.notifyItemInserted(messages.size - 1)
        val chatId = generateChatId(senderId,user!!.id)
        mainViewModel.apply {
            saveChatMessage(chatId,senderId,messageText,timestamp)
            fetchChatHistory()
        }
    }

    private fun setUpToolbar(){
        setSupportActionBar(binding.toolbar)
        binding.toolbar.apply {
            setTitleTextColor(getColor(R.color.white))
        }
        supportActionBar?.apply {
            title = user?.name
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