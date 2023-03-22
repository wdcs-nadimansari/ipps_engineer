package com.webclues.IPPSEngineer.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.webclues.IPPSEngineer.Modelclass.SensorResponseItem
import com.webclues.IPPSEngineer.R

class SorterAdapter(var SorterHistoryList: ArrayList<SensorResponseItem>) :
    RecyclerView.Adapter<SorterAdapter.MyViewholder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewholder {
        var view =
            LayoutInflater.from(parent.context).inflate(R.layout.adp_sorter_history, parent, false)
        return MyViewholder(view)

    }

    override fun onBindViewHolder(holder: MyViewholder, position: Int) {

        val rawpos = holder.adapterPosition
        if (rawpos == 0) {
            holder.itemView.apply {
                holder.txtSorter.setText(resources.getString(R.string.sorter))
                holder.txtAddSorter.setText("")

            }

        } else {
            val SensorItem = SorterHistoryList[rawpos - 1]
            holder.itemView.apply {
                holder.txtSorter.setText(SensorItem.sensorname)
                holder.txtSorter.setTextColor(ContextCompat.getColor(context, R.color.grey))
                holder.txtSorter.textSize = 12F
                holder.txtAddSorter.setText(SensorItem.sensorhistory)
                holder.txtAddSorter.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.colorPrimary
                    )
                )
                holder.txtAddSorter.textAlignment = View.TEXT_ALIGNMENT_CENTER
                holder.txtAddSorter.textSize = 12F

            }
        }
    }


    override fun getItemCount(): Int {

        return SorterHistoryList.size + 1

    }

    class MyViewholder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        var txtSorter = itemview.findViewById(R.id.txtSorter) as TextView
        var txtAddSorter = itemview.findViewById(R.id.txtAddSorter) as TextView
    }
}