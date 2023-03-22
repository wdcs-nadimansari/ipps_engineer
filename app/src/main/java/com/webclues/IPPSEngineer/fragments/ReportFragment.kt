package com.webclues.IPPSEngineer.fragments


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import com.webclues.IPPSEngineer.Modelclass.ReportResponseItem

import com.webclues.IPPSEngineer.R
import com.webclues.IPPSEngineer.activity.MainActivity
import com.webclues.IPPSEngineer.activity.SensorAndSorterActivity
import com.webclues.IPPSEngineer.adapter.ReportAdapter
import kotlinx.android.synthetic.main.fragment_report.*

/**
 * A simple [Fragment] subclass.
 */
class ReportFragment : Fragment(), View.OnClickListener {

    lateinit var ivback: ImageView
    var ReportArraylist: ArrayList<ReportResponseItem>? = arrayListOf()
    lateinit var ReportAdapter: ReportAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view: View = inflater.inflate(R.layout.fragment_report, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initview(view)
    }

    private fun initview(view: View) {
        ivback = view.findViewById(R.id.ivback)
        ivback?.setOnClickListener(this)
        txtSensor.setOnClickListener(this)
        rvReport.layoutManager = LinearLayoutManager(activity!!)
        rvReport.setHasFixedSize(true)
        ReportAdapter = ReportAdapter(activity!!, ReportArraylist!!)
        rvReport.adapter = ReportAdapter

        SetData()
    }


    private fun SetData() {

        ReportArraylist!!.add(ReportResponseItem("Machine 1", "Number Of Report:10"))
        ReportArraylist!!.add(ReportResponseItem("Machine 1", "Number Of Report:10"))

        ReportAdapter?.notifyDataSetChanged()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ivback -> {
//                startActivity(Intent(activity!!, MainActivity::class.java))
            }
            R.id.txtSensor -> {
                startActivity(Intent(activity!!, SensorAndSorterActivity::class.java))
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as MainActivity).txtTitle.setText(resources.getString(R.string.report))
    }


}
