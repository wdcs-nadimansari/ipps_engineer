package com.webclues.IPPSEngineer.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.webclues.IPPSEngineer.Modelclass.MetalResponseItem
import com.webclues.IPPSEngineer.R

class EngineerTaskAdapter(
    private val context: Activity,
    private val metalresponsearraylist: ArrayList<MetalResponseItem>
) : RecyclerView.Adapter<EngineerTaskAdapter.Myviewholder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EngineerTaskAdapter.Myviewholder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.adp_joborder, parent, false)
        return Myviewholder(view)
    }

    override fun getItemCount(): Int {

        return metalresponsearraylist.size
    }


    override fun onBindViewHolder(holder: EngineerTaskAdapter.Myviewholder, position: Int) {
        val metalresponseitem: MetalResponseItem = metalresponsearraylist.get(position)
        holder.ivJobImage.setImageResource(metalresponseitem.metalimage!!)
        holder.txtPriority.setText(metalresponseitem.metalpriority)
//        holder.txtJobTitle.setText(metalresponseitem.metalname)
//        holder.txtJobDescription.setText(metalresponseitem.metaldesc)
        holder.txtDate.setText(metalresponseitem.metaldate)
        holder.txtStatus.setText(metalresponseitem.metalstatus)
        if (metalresponseitem.metalstatus!!.equals(context.resources.getString(R.string.completed))) {
            holder.txtStatus.setTextColor(ContextCompat.getColor(context, R.color.green))
        } else if (metalresponseitem.metalstatus!!.equals(context.resources.getString(R.string.decline))) {
            holder.txtStatus.setTextColor(ContextCompat.getColor(context, R.color.red))

        } else if (metalresponseitem.metalstatus!!.equals(context.resources.getString(R.string.ongoing))) {
            holder.txtStatus.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary))

        } else if (metalresponseitem.metalstatus!!.equals(context.resources.getString(R.string.job_request))) {
            holder.txtStatus.setTextColor(ContextCompat.getColor(context, R.color.fontColorBlack50))

        } else if (metalresponseitem.metalstatus.equals(context.resources.getString(R.string.assigned))) {

            holder.txtStatus.setTextColor(ContextCompat.getColor(context, R.color.yellow))
        }


    }

    class Myviewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var ivJobImage = itemView.findViewById(R.id.ivJobImage) as ImageView
        var txtPriority = itemView.findViewById(R.id.txtPriority) as TextView
//        var txtJobTitle = itemView.findViewById(R.id.txtJobTitle) as TextView
//        var txtJobDescription = itemView.findViewById(R.id.txtJobDescription) as TextView
        var txtDate = itemView.findViewById(R.id.txtDate) as TextView
        var txtStatus = itemView.findViewById(R.id.txtStatus) as TextView
    }

}