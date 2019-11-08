package com.example.toolm.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView

import com.example.toolm.R
import com.example.toolm.app.Friend
import com.example.toolm.app.Tool
import com.example.toolm.db.LendRecordViewModel

class FriendsAdapter internal constructor(context: FragmentActivity?, friendSelectedListener:FriendSelectedListener) :
    RecyclerView.Adapter<FriendsAdapter.ViewHolder>() {
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var friends = emptyList<Friend>()
    private lateinit var lendRecordViewModel: LendRecordViewModel
    private val friendSelectedListener:FriendSelectedListener

    init {
        this.friendSelectedListener = friendSelectedListener
    }

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val tvName: TextView
        val tvBorrowed: TextView

        init {
            v.setOnClickListener {
                //call the interface to push the selected Object to the Fragment
                friendSelectedListener.onItemSelected(adapterPosition, friends.get(adapterPosition))
            }

            tvName = v.findViewById(R.id.name)
            tvBorrowed = v.findViewById(R.id.borrowed)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val v = inflater.inflate(R.layout.friends_row_item, viewGroup, false)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        Log.d(TAG, "Element $position set.")

        val currentFriend: Friend = friends[position]!!
        viewHolder.tvName.text = currentFriend.name
        val borrowedTotal = currentFriend.borrowedTools.values.map { tool -> tool.borrowed }.sum()
        viewHolder.tvBorrowed.text = "Borrowed ${borrowedTotal}"
    }

    internal fun setFriends(friends: List<Friend>) {
        this.friends = friends
        notifyDataSetChanged()
    }

    override fun getItemCount() = friends.size


    interface FriendSelectedListener {
        fun onItemSelected(friendId:Int, tool: Friend)
    }

    companion object {
        private val TAG = "FriendssAdapter"

    }
}