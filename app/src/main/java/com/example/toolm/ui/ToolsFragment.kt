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
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.toolm.app.Constants
import com.example.toolm.R
import com.example.toolm.db.LendRecordViewModel
import androidx.lifecycle.Observer
import com.example.toolm.app.Utility
import com.example.toolm.app.Tool
import com.example.toolm.db.LendRecord

class ToolsFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView

    private lateinit var viewManager: RecyclerView.LayoutManager


    private lateinit var lendRecordViewModel: LendRecordViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_tools, container, false)
        lendRecordViewModel = ViewModelProvider(this).get(LendRecordViewModel::class.java)

        viewManager = LinearLayoutManager(activity)
        val toolsAdapter =  ToolsAdapter(activity,object:ToolsAdapter.ToolSelectedListener {
            override fun onItemSelected(toolId:Int, tool: Tool?) {
                showLendDialog(activity, toolId, tool)
            }
        })

        recyclerView = rootView.findViewById<RecyclerView>(R.id.tools_recycler_view).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = toolsAdapter
        }
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                context,
                LinearLayoutManager.HORIZONTAL
            )
        )

        lendRecordViewModel.allLendRecords.observe(this, Observer { lendRecords ->
            val tools = Utility.lendRecordsToolsMapper(lendRecords)

            toolsAdapter.setTools(tools)
        })

        return rootView
    }


    fun showLendDialog(
        context: FragmentActivity?,
        toolId: Int,
        tool: Tool?
    ) {
        val friends = Constants.friends
        var selectedFriendId = -1
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Choose the friend to lend.")

        builder.setSingleChoiceItems(friends, -1,
            DialogInterface.OnClickListener { dialog, which ->
                selectedFriendId = which
            })
            .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, which ->
                if (selectedFriendId < 0) {
                    Toast.makeText(
                        context,
                        "Please select a friend to lend!", Toast.LENGTH_LONG
                    ).show()
                }else if (tool != null) {
                    if(tool.borrowed >= Constants.toolsInHand[toolId].total){
                        Toast.makeText(
                            context,
                            "Not enough tools to lend!", Toast.LENGTH_LONG
                        ).show()
                    }else{
                        lendRecordViewModel.lend(LendRecord(toolId,selectedFriendId,1))
                    }
                }

            })
            .setNegativeButton("Nope", null)

        val dialog = builder.create()
        dialog.show()
    }

}