package com.example.dreamofyou.main.adapter

import ListUsers
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.view_item_user_list.view.*

class UserListItemView(view: View) : RecyclerView.ViewHolder(view) {

    fun bind(listUser: ListUsers) = with(itemView) {
        Glide.with(context).load(listUser.avatarUrl).into(iv_user_icon)
        tv_username.text = listUser.login
    }
}