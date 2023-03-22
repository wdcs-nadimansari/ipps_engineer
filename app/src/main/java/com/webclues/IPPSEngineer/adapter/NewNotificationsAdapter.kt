package com.webclues.IPPSEngineer.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.webclues.IPPSEngineer.Modelclass.NotificationListModel
import com.webclues.IPPSEngineer.R
import com.webclues.IPPSEngineer.activity.JobDetailActivity
import com.webclues.IPPSEngineer.utility.Content
import com.webclues.IPPSEngineer.utility.Timeago

class NewNotificationsAdapter(
    var context: Activity

) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var ITEM: Int = 0
    private var LOADING = 1
    var isloadingadded = false
    var notificationarraylist: ArrayList<NotificationListModel>? = arrayListOf()


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {


        var viewholder: RecyclerView.ViewHolder? = null

        val inflater = LayoutInflater.from(parent.context)
        when (viewType) {
            ITEM -> {
                val viewItem = inflater.inflate(R.layout.adp_notifications, parent, false)
                viewholder = MyViewholder(viewItem)

            }

            LOADING -> {
                val viewLoading = inflater.inflate(R.layout.pagination_progressbar, parent, false)
                viewholder = LoadingViewholder(viewLoading)
            }


        }


        return viewholder!!
    }

    override fun getItemCount(): Int {
        return if (notificationarraylist == null) 0 else notificationarraylist!!.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val NotificationListModel = notificationarraylist!!.get(position)
        when (getItemViewType(position)) {
            ITEM -> {
                val holder = holder as MyViewholder
                holder.txtTime.text = Timeago(context).convertedtimeago(NotificationListModel.date_time!!)
                holder.txtDesc.text = NotificationListModel.content
            }
            LOADING -> {
                showloadingview(holder as LoadingViewholder, position)
            }
        }
        holder.itemView.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val intent = Intent(context, JobDetailActivity::class.java)
                intent.putExtra(Content.JOB_ID, NotificationListModel.job_id)
                intent.putExtra(Content.JOB, Content.JOB_TYPE)
                context.startActivityForResult(intent, 100)
            }

        })
    }


    fun add(NotificationListModel: NotificationListModel) {
        notificationarraylist!!.add(NotificationListModel)
        notifyItemInserted(notificationarraylist!!.size - 1)
    }

    fun addall(list: ArrayList<NotificationListModel>) {
        for (NotificationListModel in list) {
            add(NotificationListModel)
        }
    }

    fun remove(NotificationListModel: NotificationListModel) {
        var position: Int = notificationarraylist!!.indexOf(NotificationListModel)
        if (position > -1) {
            notificationarraylist!!.removeAt(position)
            notifyItemRemoved(position)
        }
    }


    fun clear() {
        isloadingadded = false
        while (itemCount > 0) {
            remove(getItem(0))
        }
    }

    fun isEmpty(): Boolean {
        return itemCount == 0
    }


    fun addloadingfooter() {
        isloadingadded = true
        add(NotificationListModel())

    }

    fun removeloadingfooter() {
        isloadingadded = false

        val position = notificationarraylist!!.size - 1
        val result = getItem(position)

        if (result != null) {
            notificationarraylist!!.removeAt(position)
            notifyItemRemoved(position)
        }

    }

    override fun getItemViewType(position: Int): Int {


        if (position == notificationarraylist!!.size - 1 && isloadingadded) {
            return LOADING

        } else {
            return ITEM
        }
    }


    fun getItem(position: Int): NotificationListModel {
        return notificationarraylist!!.get(position)
    }

    fun showloadingview(viewholder: LoadingViewholder, position: Int) {

    }

    class MyViewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txtTime = itemView.findViewById(R.id.txtTime) as TextView
        var txtDesc = itemView.findViewById(R.id.txtDescription) as TextView
    }

    class LoadingViewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var progressBar = itemView.findViewById(R.id.progressBar) as ProgressBar
    }
}