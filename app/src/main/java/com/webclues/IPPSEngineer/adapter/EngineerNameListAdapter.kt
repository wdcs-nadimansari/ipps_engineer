package com.webclues.IPPSEngineer.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.webclues.IPPSEngineer.Interface.ItemClickListner
import com.webclues.IPPSEngineer.R
import com.webclues.IPPSEngineer.activity.JobDetailActivity

class EngineerNameListAdapter(
    var context: JobDetailActivity,
    var EngineerList: ArrayList<String>,
    var itemclicklistner: ItemClickListner
) :
    RecyclerView.Adapter<EngineerNameListAdapter.MyViewholder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewholder {

        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.adp_engineername, parent, false)
        return MyViewholder(view)
    }

    override fun getItemCount(): Int {
        return EngineerList.size
    }

    override fun onBindViewHolder(holder: MyViewholder, position: Int) {
        val engineelist = EngineerList.get(position)
        holder.txtEngineer.setText(engineelist)
        if (position == itemCount - 1) {
            holder.view.visibility = View.GONE
        }
        holder.itemView.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                itemclicklistner.onclick(v!!, position)
            }

        })

    }

    fun SetItemclicklistner(itemClickListner: ItemClickListner) {
        this.itemclicklistner = itemClickListner
    }


    class MyViewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val txtEngineer: TextView = itemView.findViewById(R.id.txtEngineerName)
        val view: View = itemView.findViewById(R.id.View)
    }

}