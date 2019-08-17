package com.topaz.easymessenger.adapters

import com.squareup.picasso.Picasso
import com.topaz.easymessenger.R
import com.topaz.easymessenger.data.User
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.user_row.view.*

class UserItem(private val user: User) : Item<ViewHolder>() {
    private val DEFAULT_AVATAR_URL = "https://icons-for-free.com/iconfiles/png/512/incognito-131979013062197646.png"
    override fun getLayout(): Int {
        return R.layout.user_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.username.text = user.username

        if (user.profileImageURL != "")
            Picasso.get().load(user.profileImageURL).into(viewHolder.itemView.profile_picture)
        else
            Picasso.get().load(DEFAULT_AVATAR_URL).into(viewHolder.itemView.profile_picture)
    }
}