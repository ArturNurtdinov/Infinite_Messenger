package com.infinitevoid.easymessenger.adapters

import android.content.Context
import com.bumptech.glide.Glide
import com.infinitevoid.easymessenger.R
import com.infinitevoid.easymessenger.data.User
import com.infinitevoid.easymessenger.utils.Constants
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.user_row.view.*

class UserItem(val user: User, private val context: Context) : Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.user_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.username.text = user.username

        if (user.profileImageURL != "")
            Glide.with(context)
                .load(user.profileImageURL)
                .into(viewHolder.itemView.profile_picture)
        else
            Glide.with(context)
                .load(Constants.DEFAULT_AVATAR_URL)
                .into(viewHolder.itemView.profile_picture)
    }
}