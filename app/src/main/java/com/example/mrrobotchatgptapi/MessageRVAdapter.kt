package com.example.mrrobotchatgptapi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MessageRVAdapter(private val messageList: ArrayList<MessageRVModel>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class UserMessageViewHolder(ItemView: View): RecyclerView.ViewHolder(ItemView) {
        val userMsgTV: TextView = itemView.findViewById(R.id.idTVUser)
    }
    class BotMessageViewHolder(ItemView: View): RecyclerView.ViewHolder(ItemView) {
        val botMsgTV: TextView = itemView.findViewById(R.id.idTVBot)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View
        return if(viewType == 0) {
            view = LayoutInflater.from(parent.context).inflate(R.layout.user_message_rv_item, parent, false)
            UserMessageViewHolder(view)
        } else {
            view = LayoutInflater.from(parent.context).inflate(R.layout.bot_message_rv_item, parent, false)
            BotMessageViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (messageList[position].sender) {
            "user" -> (holder as UserMessageViewHolder).userMsgTV.text = messageList[position].message
            "bot" -> (holder as  BotMessageViewHolder).botMsgTV.text = messageList[position].message
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun getItemViewType(position: Int): Int {
        return when(messageList[position].sender) {
            "user" -> 0
            "bot" -> 1
            else -> 1
        }
    }
}












