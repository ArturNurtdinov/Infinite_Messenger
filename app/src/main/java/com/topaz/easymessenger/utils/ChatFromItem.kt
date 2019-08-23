package com.topaz.easymessenger.utils

import com.topaz.easymessenger.R
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder

class ChatFromItem: Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.chat_from_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {

    }
}