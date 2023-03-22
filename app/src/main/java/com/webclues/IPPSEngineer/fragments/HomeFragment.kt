package com.webclues.IPPSEngineer.fragments


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationView

import com.webclues.IPPSEngineer.R
import com.webclues.IPPSEngineer.activity.JobRequestActivity
import com.webclues.IPPSEngineer.activity.MainActivity
import com.webclues.IPPSEngineer.fragments.JobOrderList.JobRequestFragment
import com.webclues.IPPSEngineer.fragments.JobOrderList.WorkOrderFragment
import com.webclues.IPPSEngineer.utility.Content
import com.webclues.IPPSEngineer.utility.Log

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment(), BottomNavigationView.OnNavigationItemSelectedListener {

    lateinit var bottomnavigationview: BottomNavigationView
    var selectedMenu: Int = 0
    lateinit var selectedstatus: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_home, container, false)
        initview(view)
        return view
    }

    private fun initview(view: View) {
        Content.FILTER_PRIORITY = Content.ALL

        bottomnavigationview = view.findViewById(R.id.bottomnavigation)
        bottomnavigationview.setOnNavigationItemSelectedListener(this)

        Log.e("selectedMenu-->", selectedMenu)
        if (selectedMenu == 0) {
            bottomnavigationview.selectedItemId = R.id.jobordermenu
        } else {

            bottomnavigationview.selectedItemId = selectedMenu
//            bottomnavigationview?.get(selectedMenu)?.isSelected=true


        }

        showjobstatusbadge(context, bottomnavigationview, R.id.jobordermenu)
        showWorkorderBadge(context, bottomnavigationview, R.id.workordermenu)
        showNotificationsBadge(context, bottomnavigationview, R.id.notificationmenu)

    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.jobordermenu -> {
                if (getCurrentFragment() !is JoborderFragment) {
                    selectedMenu = R.id.jobordermenu

                    changefragment(JoborderFragment(), false)
                }

            }
            R.id.jobrequestmenu -> {

                startActivityForResult(
                    Intent(activity, JobRequestActivity::class.java),
                    Content.REQ_JOB
                )

            }
            R.id.workordermenu -> {
                if (getCurrentFragment() !is JobRequestFragment) {
                    selectedMenu = R.id.workordermenu
                    changefragment(WorkOrderFragment(), false)
                }
//                changefragment(ReportFragment(), false)
            }
            R.id.notificationmenu -> {

                if (getCurrentFragment() !is NewNotificationsFragment) {
                    selectedMenu = R.id.notificationmenu
                    changefragment(NewNotificationsFragment(), false)
                }
            }
            R.id.chatmenu -> {
                if (getCurrentFragment() !is ChatFragment) {
                    selectedMenu = R.id.chatmenu

                    changefragment(ChatFragment(), false)
                }

            }
        }
        return true
    }

    private fun changefragment(fragment: Fragment, isbackenable: Boolean) {
        val transaction = activity!!.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        if (isbackenable) {
            transaction.addToBackStack(null)
        }
        transaction.commit()
    }

    private fun getCurrentFragment(): Fragment? {
        return childFragmentManager.findFragmentById(R.id.fragment_container)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Content.REQ_JOB) {
            if (resultCode == Activity.RESULT_CANCELED) {
                bottomnavigationview.selectedItemId = selectedMenu
            }
        } else {
            if (requestCode == 100) {
                if (resultCode == Activity.RESULT_OK) {
                    if (Content.JOB_TYPE.equals(Content.NOTIFICATION)) {
                        changefragment(NewNotificationsFragment(), false)

                    } else if (Content.JOB_TYPE.equals(Content.WORKORDER_STATUS)) {
                        changefragment(WorkOrderFragment(), false)
                    } else {
                        val fragment = JoborderFragment()
                        val args = Bundle()
                        args.putString("JobType", Content.JOB_TYPE)
                        fragment.setArguments(args)
                        fragmentManager!!.beginTransaction().replace(R.id.fragment_container, fragment)
                            .commit()

                    }

                }
            }
        }
    }

    fun showjobstatusbadge(
        context: Context?, bottomNavigationView: BottomNavigationView,
        itemId: Int
    ) {

        val itemjobstatus: BottomNavigationItemView = bottomNavigationView.findViewById(itemId)
        val badgejobstatus: View = LayoutInflater.from(context)
            .inflate(R.layout.jobstatus_badge, bottomNavigationView, false)
        (activity as MainActivity).BadgeJobStatus = badgejobstatus.findViewById(R.id.BadgeJobStatus)

        itemjobstatus.addView(badgejobstatus)

    }

    fun showWorkorderBadge(
        context: Context?,
        bottomNavigationView: BottomNavigationView,
        itemid: Int
    ) {
        val itemworkorderstatus: BottomNavigationItemView =
            bottomNavigationView.findViewById(itemid)
        val badgeworkorderstatus: View = LayoutInflater.from(context)
            .inflate(R.layout.workorder_badge, bottomNavigationView, false)
        (activity as MainActivity).BadgeWorkOrder =
            badgeworkorderstatus.findViewById(R.id.BadgeWorkOrder)
        itemworkorderstatus.addView(badgeworkorderstatus)
    }


    fun showNotificationsBadge(
        context: Context?,
        bottomNavigationView: BottomNavigationView,
        itemid: Int
    ) {
        val itemNotificationstatus: BottomNavigationItemView =
            bottomNavigationView.findViewById(itemid)
        val badgeNotificationstatus: View = LayoutInflater.from(context)
            .inflate(R.layout.notification_badge, bottomNavigationView, false)
        (activity as MainActivity).BadgeNotification =
            badgeNotificationstatus.findViewById(R.id.BadgeNotification)
        itemNotificationstatus.addView(badgeNotificationstatus)
    }
}
