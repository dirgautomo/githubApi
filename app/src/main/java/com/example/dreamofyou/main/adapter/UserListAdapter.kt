package com.example.dreamofyou.main.adapter

import ListUsers
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dreamofyou.R


class UserListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val userList = arrayListOf<ListUsers>()

    private val VIEW_TYPE_ITEM = 0

    private val VIEW_TYPE_LOADING = 1

    fun setData(userList: List<ListUsers>) {
        this.userList.clear()
        this.userList.addAll(userList)
        notifyDataSetChanged()
    }

    fun updateData(userList: List<ListUsers>) {
        this.userList.addAll(userList)
        notifyDataSetChanged()
    }

    fun removeLoadMore() {
        userList.map { user ->
            if (user.id == 101) {
                userList.remove(user)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType === VIEW_TYPE_ITEM) {
            val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.view_item_user_list, parent, false)
            UserListItemView(view)
        } else {
            val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.view_load_more, parent, false)
            LoadMoreItemView(view)
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is UserListItemView) {
            holder.bind(userList[position])
        } else {
            // Do something else
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (userList[position].id == 101) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }

}