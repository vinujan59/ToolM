package com.example.toolm.app

import com.example.toolm.db.LendRecord

class Utility {
    companion object{
        fun lendRecordsToolsMapper(lendRecords: List<LendRecord>): Array<Tool?> {
            val tools = copyTools()
            for (lendRecord in lendRecords) {
                if(lendRecord.count > 0){
                    var currentTool = tools[lendRecord.tool_id]
                    currentTool!!.borrowed = currentTool.borrowed + lendRecord.count
                }
            }
            return tools
        }

        fun copyTools(): Array<Tool?> {
            val tools = arrayOfNulls<Tool>(Constants.toolsInHand.size)
            for (i in Constants.toolsInHand.indices) {
                val originalTool = Constants.toolsInHand[i]
                tools[i] = Tool(originalTool.name, originalTool.total,0)
            }
            return tools;
        }


        fun copyTool(toolId: Int): Tool{
            val currentTool = Constants.toolsInHand[toolId]
            return Tool(currentTool.name, currentTool.total, 0)
        }

        fun lendRecordsFriendsMapper(lendRecords: List<LendRecord>): List<Friend> {
            val friends = Constants.friends.map { name -> Friend(name, HashMap<Int,Tool>() ) }
            for (lendRecord in lendRecords) {
                if(lendRecord.count > 0){
                    val currentFriend = friends.get(lendRecord.friend_id)
                    val currentTool =
                        copyTool(lendRecord.tool_id)
                    if (currentFriend.borrowedTools.contains(lendRecord.tool_id)) {
                        currentFriend.borrowedTools.get(lendRecord.tool_id)!!.borrowed += lendRecord.count
                    } else {
                        currentTool.borrowed += lendRecord.count
                        currentFriend.borrowedTools.put(lendRecord.tool_id, currentTool)
                    }
                }
            }
            return friends
        }
    }
}