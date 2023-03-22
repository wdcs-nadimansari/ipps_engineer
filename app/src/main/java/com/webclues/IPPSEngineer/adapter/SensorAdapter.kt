package com.webclues.IPPSEngineer.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.webclues.IPPSEngineer.Modelclass.SensorResponseItem
import com.webclues.IPPSEngineer.R
import android.graphics.Typeface
import com.webclues.IPPSEngineer.activity.SensorAndSorterActivity
import com.webclues.IPPSEngineer.activity.SensorDetailHistoryActivity


class SensorAdapter(
    var context: SensorAndSorterActivity,
    var SensorHistoryList: ArrayList<SensorResponseItem>
) :
    RecyclerView.Adapter<SensorAdapter.Myviewholder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SensorAdapter.Myviewholder {

        var view = LayoutInflater.from(parent.context).inflate(R.layout.adp_sensor_history, null)
        return Myviewholder(view)
    }

    override fun getItemCount(): Int {
        return SensorHistoryList.size + 1
    }

    override fun onBindViewHolder(holder: SensorAdapter.Myviewholder, position: Int) {

        val rawpos = holder.adapterPosition
        if (rawpos == 0) {
            holder.itemView.apply {
                holder.txtSensor.setText(resources.getString(R.string.sensor))
                holder.txtAddSensor.setText(resources.getString(R.string.add_sensor))

            }

        } else {
            val SensorItem = SensorHistoryList[rawpos - 1]
            holder.itemView.apply {
                holder.txtSensor.setText(SensorItem.sensorname)
                holder.txtSensor.setTextColor(ContextCompat.getColor(context, R.color.grey))
                holder.txtSensor.textSize = 12F
                holder.txtAddSensor.setText(SensorItem.sensorhistory)
                holder.txtAddSensor.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.colorPrimary
                    )
                )
                val face = Typeface.createFromAsset(
                    context.getAssets(),
                    "font/lato_medium.ttf"
                )
                holder.txtAddSensor.setTypeface(face)
                holder.txtAddSensor.textSize = 12F

            }

            holder.itemView.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    context.startActivity(Intent(context, SensorDetailHistoryActivity::class.java))
                }

            })
        }
    }

    class Myviewholder(itemview: View) : RecyclerView.ViewHolder(itemview) {

        var txtSensor = itemview.findViewById(R.id.txtSensor) as TextView
        var txtAddSensor = itemview.findViewById(R.id.txtAddSensor) as TextView
    }
}