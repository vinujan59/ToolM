package com.example.toolm.ui

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.toolm.R
import com.example.toolm.app.Utility
import com.example.toolm.app.Friend
import com.example.toolm.db.LendRecord
import com.example.toolm.db.LendRecordViewModel

class FriendsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView

    private lateinit var viewManager: RecyclerView.LayoutManager


    private lateinit var lendRecordViewModel: LendRecordViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_friends, container, false)
        lendRecordViewModel = ViewModelProvider(this).get(LendRecordViewModel::class.java)

        viewManager = LinearLayoutManager(activity)
        val friendsAdapter =
            FriendsAdapter(activity, object : FriendsAdapter.FriendSelectedListener {
                override fun onItemSelected(friendId: Int, friend: Friend) {
                    if (friend.borrowedTools.values.isEmpty()) {
                        Toast.makeText(
                            context,
                            "${friend.name} did not borrow any tools!", Toast.LENGTH_LONG
                        ).show()
                    } else {
                        showLendDialog(activity, friendId, friend)
                    }
                }
            })

        recyclerView = rootView.findViewById<RecyclerView>(R.id.friends_recycler_view).apply {
            layoutManager = viewManager
            adapter = friendsAdapter
        }

        recyclerView.addItemDecoration(
            DividerItemDecoration(
                context,
                LinearLayoutManager.HORIZONTAL
            )
        )

        lendRecordViewModel.allLendRecords.observe(this, Observer { lendRecords ->
            val friends = Utility.lendRecordsFriendsMapper(lendRecords)

            friendsAdapter.setFriends(friends)
        })

        return rootView
    }

    fun showLendDialog(
        context: FragmentActivity?,
        friendId: Int,
        friend: Friend
    ) {
        val tools = friend.borrowedTools.values.toTypedArray()
        val friends = tools.map { tool -> "${tool.borrowed} of ${tool.name}" }.toTypedArray()
        var selectedToolIndex = -1
        val builder = AlertDialog.Builder(context)
        builder.setTitle("${friend.name} gave")

        builder.setSingleChoiceItems(friends, -1,
            DialogInterface.OnClickListener { dialog, which ->
                selectedToolIndex = which
            })
            .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, which ->
                if (selectedToolIndex < 0) {
                    Toast.makeText(
                        context,
                        "Please select a item to settle!", Toast.LENGTH_LONG
                    ).show()
                } else {
                    val selectedToolName = tools[selectedToolIndex].name
                    for ((k, v) in friend.borrowedTools) {
                        if (v.name.equals(selectedToolName)) {
                            lendRecordViewModel.settle(LendRecord(k,friendId,v.borrowed))
                            break
                        }
                    }
                }
            })
            .setNegativeButton("Nope", null)

        val dialog = builder.create()
        dialog.show()
    }
}