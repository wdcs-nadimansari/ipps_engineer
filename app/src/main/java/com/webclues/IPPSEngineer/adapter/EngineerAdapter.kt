package com.webclues.IPPSEngineer.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.webclues.IPPSEngineer.Modelclass.EngineerResponseItem
import com.webclues.IPPSEngineer.R
import com.webclues.IPPSEngineer.activity.EngineerDetailsActivity
import com.webclues.IPPSEngineer.activity.EngineerListActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.adp_engineerlist.view.*

class EngineerAdapter(
    var context: EngineerListActivity,
    var EngineerArraylist: ArrayList<EngineerResponseItem>
) :
    RecyclerView.Adapter<EngineerAdapter.MyViewholder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewholder {

        var view = LayoutInflater.from(parent.context).inflate(R.layout.adp_engineerlist, null)
        return MyViewholder(view)

    }


    override fun onBindViewHolder(holder: MyViewholder, position: Int) {

        var engineerResponseItem: EngineerResponseItem = EngineerArraylist.get(position)
        Picasso.get().load(engineerResponseItem.EngneerProfile!!).into(holder.ivEngineerimage)
        holder.txtEngineerName.setText(engineerResponseItem.EngineerName)
        holder.txtEngineerOccupation.setText(engineerResponseItem.EngineerOccupation)
        if (position == itemCount - 1) {
            holder.view.visibility = View.GONE
        }
        holder.itemView.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                context.startActivity(
                    Intent(context, EngineerDetailsActivity::class.java)
                        .putExtra("Engineer_Name", engineerResponseItem.EngineerName)
                        .putExtra("Engineer_Profile", engineerResponseItem.EngneerProfile!!)
                )
            }

        })
    }

    override fun getItemCount(): Int {

        return EngineerArraylist.size
    }

    class MyViewholder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        var ivEngineerimage = itemview.ivEngineerProfile
        var txtEngineerName = itemview.txtEngineerName
        var txtEngineerOccupation = itemview.txtEngineerOccupation
        var view = itemView.view
    }

}