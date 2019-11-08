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
import com.example.toolm.app.Tool
import com.example.toolm.db.LendRecordViewModel

class ToolsAdapter internal constructor(context: FragmentActivity?, toolSelectedListener:ToolSelectedListener) :
    RecyclerView.Adapter<ToolsAdapter.ViewHolder>() {
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var tools = emptyArray<Tool?>()
    private lateinit var lendRecordViewModel: LendRecordViewModel
    private val toolSelectedListener:ToolSelectedListener

    init {
        this.toolSelectedListener = toolSelectedListener
    }

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val tvName: TextView
        val tvTotal: TextView
        val tvBorrowed: TextView
        val imageView: ImageView

        init {
            v.setOnClickListener {
                //call the interface to push the selected Object to the Fragment
                toolSelectedListener.onItemSelected(adapterPosition, tools.get(adapterPosition))
            }

            tvName = v.findViewById(R.id.name)
            tvTotal = v.findViewById(R.id.total)
            tvBorrowed = v.findViewById(R.id.borrowed)
            imageView = v.findViewById(R.id.tool_icon)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val v = inflater.inflate(R.layout.tools_row_item, viewGroup, false)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        Log.d(TAG, "Element $position set.")

        val currentTool: Tool = tools[position]!!
        viewHolder.tvName.text = currentTool.name
        viewHolder.tvTotal.text = "total ${currentTool.total}"
        viewHolder.tvBorrowed.text = "borrowed ${currentTool.borrowed}"

        if(currentTool.name.equals("Wrench")){
            viewHolder.imageView.setImageResource(R.drawable.ic_wrench)
        }else if (currentTool.name.equals("Cutter")){
            viewHolder.imageView.setImageResource(R.drawable.ic_cutter)
        }else if (currentTool.name.equals("Pliers")){
            viewHolder.imageView.setImageResource(R.drawable.ic_plier)
        }else if (currentTool.name.equals("Screwdriver")){
            viewHolder.imageView.setImageResource(R.drawable.ic_screwdriver)
        }else if (currentTool.name.equals("Welding Machine")){
            viewHolder.imageView.setImageResource(R.drawable.ic_welding_machine)
        }else if (currentTool.name.equals("Welding Glasses")){
            viewHolder.imageView.setImageResource(R.drawable.ic_welding_glasses)
        }else if (currentTool.name.equals("Hammer")){
            viewHolder.imageView.setImageResource(R.drawable.ic_hammer)
        }else if (currentTool.name.equals("Measuring Tape")){
            viewHolder.imageView.setImageResource(R.drawable.ic_measuring_tape)
        }else if (currentTool.name.equals("Alan Key Set")){
            viewHolder.imageView.setImageResource(R.drawable.ic_allen_keys)
        }else if (currentTool.name.equals("Air Compressor")){
            viewHolder.imageView.setImageResource(R.drawable.ic_air_compressor)
        }else {
            viewHolder.imageView.setImageResource(R.drawable.ic_wrench)
        }
    }

    internal fun setTools(tools: Array<Tool?>) {
        this.tools = tools
        notifyDataSetChanged()
    }

    override fun getItemCount() = tools.size


    interface ToolSelectedListener {
        fun onItemSelected(toolID:Int, tool: Tool?)
    }

    companion object {
        private val TAG = "ToolsAdapter"

    }
}