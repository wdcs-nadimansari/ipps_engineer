package com.webclues.IPPSEngineer.fragments.JobOrderList


import android.content.Context
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.ListPopupWindow
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.webclues.IPPSEngineer.Modelclass.JobStatusModel

import com.webclues.IPPSEngineer.R
import com.webclues.IPPSEngineer.activity.MainActivity
import com.webclues.IPPSEngineer.adapter.JobOrderAdapter
import com.webclues.IPPSEngineer.adapter.PaginationScrollListener
import com.webclues.IPPSEngineer.service.APIClient
import com.webclues.IPPSEngineer.utility.Content
import com.webclues.IPPSEngineer.utility.CustomProgress
import com.webclues.IPPSEngineer.utility.Utility
import com.webclues.IPPSEngineer.service.ApiInterface
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 */
class WorkOrderFragment : Fragment() {


    lateinit var srlJobs: SwipeRefreshLayout
    lateinit var rvJobs: RecyclerView
    lateinit var customProgress: CustomProgress
    lateinit var apiInterface: ApiInterface
    var jobOrderAdapter: JobOrderAdapter? = null
    var jobstatuslist: ArrayList<JobStatusModel>? = arrayListOf()
    var page: Int = 1
    var tvpriority: TextView? = null
    lateinit var lnPriority: LinearLayout
    private var prioritytypes: ArrayList<String>? = arrayListOf()
    private lateinit var priorityDropdownView: ListPopupWindow
    var PAGE_START: Int = 1
    var currentpage: Int = PAGE_START
    var isloading: Boolean = false
    var islastpage: Boolean = false
    var priority: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_work_order, container, false)
        initView(view)
        return view
    }


    fun initView(view: View) {
        priority = Content.ALL
        apiInterface = APIClient.getretrofit(activity!!).create(ApiInterface::class.java)
        customProgress = CustomProgress.instance
        isloading = false
        islastpage = false

        Content.JOB_TYPE = Content.WORKORDER_STATUS

        srlJobs = view.findViewById(R.id.srlJobs)
        rvJobs = view.findViewById(R.id.rvJobs)
        tvpriority = view.findViewById(R.id.tvpriority)
        lnPriority = view.findViewById(R.id.lnPriority)

        rvJobs.setLayoutManager(
            WrapContentLinearLayoutManager(
                activity!!
            )
        )
        rvJobs.setHasFixedSize(true)
        jobOrderAdapter = JobOrderAdapter(activity!!)
        rvJobs.adapter = jobOrderAdapter

        Setpriority()

        loadFirstpage()


        rvJobs.addOnScrollListener(object :
            PaginationScrollListener(rvJobs.layoutManager as LinearLayoutManager) {
            override fun loadMoreItems() {
                isloading = true
                currentpage += 1
                loadNextPage(currentpage, Content.ASSIGN_WORKORDER, priority, "")
            }

            override fun isLoading(): Boolean {

                return isloading
            }

            override fun isLastPage(): Boolean {

                return islastpage
            }


        })

        srlJobs.setOnRefreshListener {
            islastpage = false
            jobOrderAdapter!!.jobstatuslist!!.clear()
            loadFirstpage()
        }
    }

    private fun Setpriority() {
        prioritytypes =
            getResources().getStringArray(R.array.workorder_priority_item).toCollection(ArrayList())
        priorityDropdownView = ListPopupWindow(activity!!)
        priorityDropdownView.setAdapter(
            ArrayAdapter(
                activity!!,
                android.R.layout.simple_spinner_dropdown_item,
                prioritytypes!!
            )
        )

        priorityDropdownView.anchorView = lnPriority
        priorityDropdownView.setModal(true)
        lnPriority.post {
            priorityDropdownView.setDropDownGravity(Gravity.END)
            priorityDropdownView.width = 350
//            priorityDropdownView.height = 400
            priorityDropdownView.horizontalOffset = -15
        }
        priorityDropdownView.setOnItemClickListener { adapterView, view, position, viewId ->
            tvpriority!!.setText(prioritytypes!!.get(position))
            priorityDropdownView.dismiss()


            if (prioritytypes!!.get(position).equals(resources.getString(R.string.all))) {
                priority = Content.ALL
            } else if (prioritytypes!!.get(position).equals(resources.getString(R.string.low))) {
                priority = Content.LOW

            } else if (prioritytypes!!.get(position).equals(resources.getString(R.string.medium))) {
                priority = Content.MEDIUM

            } else if (prioritytypes!!.get(position).equals(resources.getString(R.string.high))) {
                priority = Content.HIGH

            }
//            isloading = false


            islastpage = false
//            isloading=false
            jobOrderAdapter!!.jobstatuslist!!.clear()
            loadFirstpage()

        }
        tvpriority!!.setOnClickListener {
            priorityDropdownView.show()
            tvpriority!!.setAllCaps(true);
        }

    }


    fun loadFirstpage(            //load first page in workorder

    ) {
        srlJobs.isRefreshing = false
        currentpage = PAGE_START
        customProgress.showProgress(activity!!, getString(R.string.please_wait), false)
        val call: Call<JsonObject> =
            apiInterface.jobstatus(currentpage, Content.ASSIGN_WORKORDER, priority, "")
        call.enqueue(object : Callback<JsonObject> {


            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                customProgress.hideProgress()

                val jsonObject = JSONObject(response.body().toString())
                val statuscode = jsonObject.optInt("status_code")
                if (statuscode == 200) {
                    if (jsonObject.optBoolean("status")) {
                        val data = jsonObject.optJSONObject("data")
                        val joblist = data.optJSONArray("job_list")
                        if (data.optInt("unread_notification_count") != 0) {
                            (activity as MainActivity).BadgeNotification!!.visibility = View.VISIBLE

                        } else {
                            (activity as MainActivity).BadgeNotification!!.visibility = View.GONE

                        }
                        if (data.optInt("unread_job_request_count") != 0) {
                            (activity as MainActivity).BadgeJobStatus!!.visibility = View.VISIBLE

                        } else {
                            (activity as MainActivity).BadgeJobStatus!!.visibility = View.GONE

                        }
                        if (data.optInt("unread_workorder_count") != 0) {
                            (activity as MainActivity).BadgeWorkOrder!!.visibility = View.VISIBLE

                        } else {
                            (activity as MainActivity).BadgeWorkOrder!!.visibility = View.GONE

                        }

                        if (joblist != null && joblist.length() > 0) {
                            jobstatuslist!!.clear()

                            jobstatuslist!!.addAll(
                                Gson().fromJson(
                                    joblist.toString(),
                                    Array<JobStatusModel>::class.java
                                ).toList()
                            )
                            jobOrderAdapter!!.addall(jobstatuslist)


                            if (currentpage.toString() < jsonObject.optString("total_pages")) jobOrderAdapter!!.addloadingfooter()
                            else islastpage = true


                        }

                    } else {
                        Utility().showOkDialog(
                            context!!,
                            resources.getString(R.string.app_name),
                            jsonObject.optString("message")
                        )
                    }

                } else if (statuscode == 403) {
                    Utility().showInactiveDialog(
                        context!!,
                        resources.getString(R.string.app_name),
                        jsonObject.optString("message")
                    )
                } else {
                    Utility().showOkDialog(
                        context!!,
                        resources.getString(R.string.app_name),
                        jsonObject.optString("message")
                    )
                }
                jobOrderAdapter!!.notifyDataSetChanged()
            }


            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                customProgress.hideProgress()
                Utility().showOkDialog(
                    context!!,
                    resources.getString(R.string.app_name),
                    getString(R.string.error_something_is_wrong_ln)
                )
            }

        })
    }


    private fun loadNextPage(              //load next page in workorder
        pagenumber: Int,
        jobstatus: Int,
        filterpriority: Int,
        engineerid: String
    ) {

        val call: Call<JsonObject> =
            apiInterface.jobstatus(pagenumber, jobstatus, filterpriority, engineerid)
        call.enqueue(object : Callback<JsonObject> {


            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                jobOrderAdapter!!.removeloadingfooter()
                isloading = false


                if (response.code() == 200) {

                    val jsonObject = JSONObject(response.body().toString())
                    if (jsonObject.optBoolean("status")) {


                        val data = jsonObject.optJSONObject("data")
                        val joblist = data.optJSONArray("job_list")
                        if (joblist != null && joblist.length() > 0) {
                            jobstatuslist!!.clear()

                            jobstatuslist!!.addAll(
                                Gson().fromJson(
                                    joblist.toString(),
                                    Array<JobStatusModel>::class.java
                                ).toList()
                            )
                            jobOrderAdapter!!.addall(jobstatuslist)

                            if (currentpage.toString() != jsonObject.optString("total_pages")) jobOrderAdapter!!.addloadingfooter()
                            else islastpage = true


                        }
                    } else {
                        customProgress.hideProgress()

                        Utility().showOkDialog(
                            context!!,
                            resources.getString(R.string.app_name),
                            jsonObject.optString("message")
                        )
                    }
                }
            }


            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Utility().showOkDialog(
                    context!!,
                    resources.getString(R.string.app_name),
                    getString(R.string.error_something_is_wrong_ln)
                )
            }

        })

    }


    override fun onResume() {
        super.onResume()
        (activity as MainActivity).txtTitle.setText(resources.getString(R.string.workorder))
    }

    inner class WrapContentLinearLayoutManager(context: Context) : LinearLayoutManager(context) {
        //... constructor
        override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State) {
            try {
                super.onLayoutChildren(recycler, state)
            } catch (e: IndexOutOfBoundsException) {
            }

        }
    }

}
