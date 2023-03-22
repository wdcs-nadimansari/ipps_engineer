package com.webclues.IPPSEngineer.Interface

import com.webclues.IPPSEngineer.Modelclass.GroupListModel


interface OnChatListUpdate {
    fun UpdateList(chatList : ArrayList<GroupListModel>)
}