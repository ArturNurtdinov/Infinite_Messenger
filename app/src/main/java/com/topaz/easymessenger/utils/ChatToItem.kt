package com.topaz.easymessenger.utils

import com.topaz.easymessenger.R
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.chat_to_row.view.*

class ChatToItem(private val message: String): Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.message.text = message
    }

    override fun getLayout(): Int {
        return R.layout.chat_to_row
    }
}