package com.webclues.IPPSEngineer.fragments


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.webclues.IPPSEngineer.Modelclass.NotificationResponseItem

import com.webclues.IPPSEngineer.R
import com.webclues.IPPSEngineer.activity.MainActivity
import com.webclues.IPPSEngineer.adapter.NotificationAdapter
import kotlinx.android.synthetic.main.fragment_notification.*

/**
 * A simple [Fragment] subclass.
 */
class NotificationFragment : Fragment() {

    lateinit var notificationAdapter: NotificationAdapter
    private var notificationarraylist: ArrayList<NotificationResponseItem>? = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view= inflater.inflate(R.layout.fragment_notification, container, false)
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        SetNotificationData()
    }

    private fun SetNotificationData() {
        notificationarraylist!!.add(
            NotificationResponseItem(
                "15 Minutes ago",
                "Duis rhoncus dui venenatis consequat porttitor. Etiam aliquet congue consequat.In posuere."
            )
        );
        notificationarraylist!!.add(
            NotificationResponseItem(
                "15 Minutes ago",
                "Duis rhoncus dui venenatis consequat porttitor. Etiam aliquet congue consequat.In posuere."
            )
        );
        notificationarraylist!!.add(
            NotificationResponseItem(
                "50 Minutes ago",
                "Duis rhoncus dui venenatis consequat porttitor. Etiam aliquet congue consequat.In posuere."
            )
        );

        notificationarraylist!!.add(
            NotificationResponseItem(
                "15 Minutes ago",
                "Duis rhoncus dui venenatis consequat porttitor. Etiam aliquet congue consequat.In posuere."
            )
        );
        notificationarraylist!!.add(
            NotificationResponseItem(
                "5 Days ago",
                "Duis rhoncus dui venenatis consequat porttitor. Etiam aliquet congue consequat.In posuere."
            )
        );


        SetAdapter(activity!!, notificationarraylist!!)

    }

    private fun SetAdapter(
        context: Context,
        notificationarraylist: ArrayList<NotificationResponseItem>
    ) {
        rvNotifications.layoutManager = LinearLayoutManager(context)
        rvNotifications.setHasFixedSize(true)

        notificationAdapter = NotificationAdapter(context, notificationarraylist)
        rvNotifications.adapter = notificationAdapter
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).txtTitle.setText(resources.getString(R.string.notifications))
    }

}
