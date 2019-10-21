package com.infinitevoid.easymessenger.adapters

import android.content.Context
import android.view.View
import com.bumptech.glide.Glide
import com.infinitevoid.easymessenger.R
import com.infinitevoid.easymessenger.data.ChatMessage
import com.infinitevoid.easymessenger.data.User
import com.infinitevoid.easymessenger.utils.Constants
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.chat_from_row.view.*
import java.text.SimpleDateFormat
import java.util.*

class ChatFromItem(
    private val chatMessage: ChatMessage,
    private val user: User,
    private val context: Context,
    private val click: (String) -> Unit
) :
    Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.chat_from_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        val date = Date(chatMessage.timestamp * 1000)
        val dateFormat =
            if (date.day == Date(System.currentTimeMillis()).day) {
                SimpleDateFormat("HH:mm", Locale.getDefault())
            } else {
                SimpleDateFormat("MMM d", Locale.getDefault())
            }
        viewHolder.itemView.message.text = chatMessage.message
        viewHolder.itemView.date_text.text = dateFormat.format(date)
        if (user.profileImageURL != "") {
            Glide.with(context)
                .load(user.profileImageURL)
                .into(viewHolder.itemView.profile_picture)
        } else {
            Glide.with(context)
                .load(Constants.DEFAULT_AVATAR_URL)
                .into(viewHolder.itemView.profile_picture)
        }

        if (chatMessage.imageURL != "") {
            Glide.with(context)
                .load(chatMessage.imageURL)
                .into(viewHolder.itemView.message_image)
            viewHolder.itemView.message_image.setOnClickListener { click(chatMessage.imageURL) }
            viewHolder.itemView.message_image.visibility = View.VISIBLE
        } else {
            viewHolder.itemView.message_image.visibility = View.GONE
        }

    }
}