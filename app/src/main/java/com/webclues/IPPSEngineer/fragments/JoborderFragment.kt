package com.webclues.IPPSEngineer.fragments


import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.tabs.TabLayout

import com.webclues.IPPSEngineer.R
import com.webclues.IPPSEngineer.activity.MainActivity
import com.webclues.IPPSEngineer.fragments.JobOrderList.*
import com.webclues.IPPSEngineer.utility.CustomViewPager
import android.content.BroadcastReceiver
import android.content.IntentFilter
import android.view.*
import android.widget.*
import androidx.fragment.app.FragmentTransaction
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.webclues.IPPSEngineer.utility.Content


/**
 * A simple [Fragment] subclass.
 */
class JoborderFragment : Fragment() {


    lateinit var tabLayout: TabLayout
    lateinit var viewpager: CustomViewPager
    lateinit var tvpriority: TextView
    lateinit var lnPriority: LinearLayout
    private var prioritytypes: ArrayList<String>? = arrayListOf()
    private lateinit var priorityDropdownView: ListPopupWindow
    lateinit var frameLayout: FrameLayout
    lateinit var fragment: Fragment
    lateinit var manager: FragmentManager
    lateinit var fragmentTransaction: FragmentTransaction


    private val mReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            // Get extra data included in the Intent
            val status = intent.getStringExtra("selectedstatus")
            if (status.equals(resources.getString(R.string.completed))) {
                viewpager.currentItem = 1
            } else if (status.equals(resources.getString(R.string.Incomplete))) {
                viewpager.currentItem = 2
            } else {
                viewpager.currentItem = 0
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LocalBroadcastManager.getInstance(activity!!)
            .registerReceiver(mReceiver, IntentFilter("selectedstatus"))

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_joborder, container, false)
        initview(view)
        return view
    }

    private fun initview(view: View) {

        Content.FILTER_PRIORITY = Content.ALL

        frameLayout = view.findViewById(R.id.frame_container)
        viewpager = view.findViewById(R.id.viewpager)
        tvpriority = view.findViewById(R.id.tvpriority)
        lnPriority = view.findViewById(R.id.lnPriority)
        viewpager.setPagingEnabled(false)

        tabLayout = view.findViewById(R.id.tablayout)


        if (arguments != null) {

            var value = getArguments()!!.getString("JobType")
            if (value.equals(Content.JOBREQUEST_STATUS)) {
                tabLayout.getTabAt(0)!!.select()
                changefragment(JobRequestFragment(), false)
            } else if (value.equals(Content.COMPLETED_STATUS)) {
                tabLayout.getTabAt(1)!!.select()
                changefragment(CompletedFragment(), false)
            } else {
                tabLayout.getTabAt(2)!!.select()
                changefragment(DeclineFragment(), false)
            }

        } else {
            fragment = JobRequestFragment()
            manager = this.childFragmentManager
            fragmentTransaction = manager.beginTransaction()
            fragmentTransaction.replace(R.id.frame_container, fragment)
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            fragmentTransaction.commit()

        }


        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab!!.position) {
                    0 -> {
                        fragment = JobRequestFragment()
                    }
                    1 -> {
                        fragment = CompletedFragment()
                    }
                    2 -> {
                        fragment = DeclineFragment()
                    }

                }
                var fm: FragmentManager = childFragmentManager
                var ft = fm.beginTransaction()
                ft.replace(R.id.frame_container, fragment)
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                ft.commit()

            }

        })
        val typeface = Typeface.createFromAsset(context!!.assets, "font/lato_semibold.ttf")
        tabLayout.applyFont(typeface)

        Setpriority()
    }


    fun TabLayout.applyFont(typeface: Typeface) {
        val viewGroup = getChildAt(0) as ViewGroup
        val tabsCount = viewGroup.childCount
        for (j in 0 until tabsCount) {
            val viewGroupChildAt = viewGroup.getChildAt(j) as ViewGroup
            val tabChildCount = viewGroupChildAt.childCount
            for (i in 0 until tabChildCount) {
                val tabViewChild = viewGroupChildAt.getChildAt(i)
                if (tabViewChild is TextView) {
                    tabViewChild.typeface = typeface
                }
            }
        }
    }


    private fun changefragment(fragment: Fragment, Backenable: Boolean) {

        manager = childFragmentManager
        fragmentTransaction = manager.beginTransaction()
        fragmentTransaction.add(R.id.frame_container, fragment)
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        if (Backenable) {
            fragmentTransaction.addToBackStack(null)
        }
        fragmentTransaction.commit()
    }


    private fun Setpriority() {
        prioritytypes =
            getResources().getStringArray(R.array.priority_item).toCollection(ArrayList())
        priorityDropdownView = ListPopupWindow(activity!!)
        priorityDropdownView.setAdapter(ArrayAdapter(activity!!, android.R.layout.simple_spinner_dropdown_item, prioritytypes!!))
        priorityDropdownView.anchorView = lnPriority
        priorityDropdownView.setModal(true)
        lnPriority.post {
            priorityDropdownView.setDropDownGravity(Gravity.END)
            priorityDropdownView.width = 350
//            priorityDropdownView.height = 300
            priorityDropdownView.horizontalOffset = -15
        }
        priorityDropdownView.setOnItemClickListener { adapterView, view, position, viewId ->
            tvpriority.setText(prioritytypes!!.get(position))
            priorityDropdownView.dismiss()
            val intent = Intent("SelectedPriority")
            if (prioritytypes!!.get(position).equals(getString(R.string.pending))) {
                Content.STATUS_PRIORITY = getString(R.string.pending)
            } else if (prioritytypes!!.get(position).equals(getString(R.string.low))) {
                Content.STATUS_PRIORITY = getString(R.string.low)
            } else if (prioritytypes!!.get(position).equals(getString(R.string.medium))) {
                Content.STATUS_PRIORITY = getString(R.string.medium)
            } else if (prioritytypes!!.get(position).equals(getString(R.string.high))) {
                Content.STATUS_PRIORITY = getString(R.string.high)
            } else {
                Content.STATUS_PRIORITY = getString(R.string.all)
            }

            intent.putExtra(Content.PRIORITY, Content.STATUS_PRIORITY)

            LocalBroadcastManager.getInstance(activity!!).sendBroadcast(intent)

        }
        tvpriority.setOnClickListener {
            priorityDropdownView.show()
        }

    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).txtTitle.setText(resources.getString(R.string.maintenance_app))
    }


    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(activity!!).unregisterReceiver(mReceiver)
    }


}


