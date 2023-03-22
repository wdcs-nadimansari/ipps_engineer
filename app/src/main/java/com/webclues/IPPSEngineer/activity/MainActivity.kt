package com.webclues.IPPSEngineer.activity

import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.webclues.IPPSEngineer.Modelclass.UserRespone
import com.webclues.IPPSEngineer.R
import com.webclues.IPPSEngineer.application.mApplicationClass
import com.webclues.IPPSEngineer.fragments.*
import com.webclues.IPPSEngineer.service.APIClient
import com.webclues.IPPSEngineer.utility.*
import com.squareup.picasso.Picasso
import com.webclues.IPPSEngineer.service.ApiInterface
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.nav_drawer.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var tabLayout: TabLayout

    //    var viewpager: CustomViewPager? = null
    lateinit var toolbar: Toolbar
    lateinit var fragmentManager: FragmentManager
    lateinit var fragmentTransaction: FragmentTransaction
    lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    lateinit var userDetails: UserRespone
    lateinit var txtTitle: TextView
    private val tabIcons = intArrayOf(
            R.drawable.joborder,
            R.drawable.jobrequest,
            R.drawable.notification,
            R.drawable.chat
    )
    lateinit var customProgress: CustomProgress
    lateinit var context: Context
    lateinit var apiInterface: ApiInterface
    lateinit var engineername: TextView
    var BadgeJobStatus: TextView? = null
    var BadgeWorkOrder: TextView? = null
    var BadgeNotification: TextView? = null
    var notificationReciever: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent != null && intent.action.equals("Notify_Job")) {
                var jobRequestCount = intent.getIntExtra("unread_job_request_count", 0)
                var notificationCount = intent.getIntExtra("unread_notification_count", 0)
                var workOrderCount = intent.getIntExtra("unread_workorder_count", 0)
                if (notificationCount != 0) {
                    BadgeNotification!!.visibility = View.VISIBLE
                } else {
                    BadgeNotification!!.visibility = View.GONE
                }

                if (jobRequestCount != 0) {
                    BadgeJobStatus!!.visibility = View.VISIBLE
                } else {
                    BadgeJobStatus!!.visibility = View.GONE
                }
                if (workOrderCount != 0) {
                    BadgeWorkOrder!!.visibility = View.VISIBLE

                } else {
                    BadgeWorkOrder!!.visibility = View.GONE

                }
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        context = this
        Log.e("MainActivity-->", "onCreate")
        initview()
    }

    private fun initview() {
        customProgress = CustomProgress.instance
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        viewpager!!.setPagingEnabled(false)
        tabLayout = findViewById(R.id.tablayout)

        txtTitle = findViewById(R.id.txtTitle)
        engineername = findViewById(R.id.txtEngineerName);
        ivmenu.setOnClickListener(this)
        rlProfile.setOnClickListener(this)
        ivclose.setOnClickListener(this)
        txtHome.setOnClickListener(this)
        txtEngineers.setOnClickListener(this)
        txtAboutUs.setOnClickListener(this)
        txtChangePass.setOnClickListener(this)
        txtContactUs.setOnClickListener(this)
        txtFaq.setOnClickListener(this)
        txtLogout.setOnClickListener(this)
        setnavigationdrawer()


        if (intent.getStringExtra("From") != null) {
            ChangeFragment(ProfileFragment(), false)
        } else {
            ChangeFragment(HomeFragment(), false)
        }
        LocalBroadcastManager.getInstance(this).registerReceiver(notificationReciever, IntentFilter("Notify_Job"))

    }

    private fun setnavigationdrawer() {
        fragmentManager = supportFragmentManager
        actionBarDrawerToggle = object : ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        ) {
            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
                drawer_layout.openDrawer(nav_drawer)
                UpdateNavigationdrawer()
            }

            override fun onDrawerClosed(drawerView: View) {
                super.onDrawerClosed(drawerView)
                drawer_layout.closeDrawer(nav_drawer)
                if (drawer_layout.isDrawerOpen(GravityCompat.END)) {
                    drawer_layout.closeDrawer(GravityCompat.END)
                }
            }
        }
        drawer_layout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        toolbar.navigationIcon = null


    }

    fun ChangeFragment(fragment: Fragment, backenable: Boolean) {

        fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_container, fragment)
        if (backenable) {
            fragmentTransaction.addToBackStack(null)
        }
        fragmentTransaction.commit()

    }

    private fun UpdateNavigationdrawer() {

        userDetails =
                Gson().fromJson(TinyDb(context).getString(Content.USER_DATA), UserRespone::class.java)

        engineername.setText(userDetails.first_name + " " + userDetails.last_name)
        Picasso.get().load(userDetails.profile_pic).placeholder(R.drawable.ic_placeholder_profile)
                .error(R.drawable.ic_placeholder_profile).into(ivprofile)
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ivmenu -> {
                drawer_layout.openDrawer(nav_drawer)
            }
            R.id.ivclose -> {
                drawer_layout.closeDrawer(nav_drawer)
            }
            R.id.rlProfile -> {
//                startActivity(Intent(context, ProfileActivity::class.java))
                if (getCurrentFragment() !is ProfileFragment) {
                    ChangeFragment(ProfileFragment(), true)
                }
                Utility.hideKeyboard(this)
                drawer_layout.closeDrawer(nav_drawer)
            }
            R.id.txtHome -> {
                if (getCurrentFragment() !is HomeFragment) {

                    ChangeFragment(HomeFragment(), false)
                } else {
                    (getCurrentFragment() as HomeFragment).bottomnavigationview?.selectedItemId =
                            R.id.jobordermenu
                }
                Utility.hideKeyboard(this)

                drawer_layout.closeDrawer(nav_drawer)
            }
            R.id.txtEngineers -> {
                startActivity(Intent(this, EngineerListActivity::class.java))
            }
            R.id.txtAboutUs -> {
                if (getCurrentFragment() !is AboutusFragment) {
                    ChangeFragment(AboutusFragment(), true)
                }
                Utility.hideKeyboard(this)

                drawer_layout.closeDrawer(nav_drawer)
            }
            R.id.txtChangePass -> {
                if (getCurrentFragment() !is ChangePasswordFragment) {
                    ChangeFragment(ChangePasswordFragment(), true)
                }
                Utility.hideKeyboard(this)

                drawer_layout.closeDrawer(nav_drawer)
            }
            R.id.txtContactUs -> {
                if (getCurrentFragment() !is ContactUsFragment) {
                    ChangeFragment(ContactUsFragment(), true)
                }
                Utility.hideKeyboard(this)

                drawer_layout.closeDrawer(nav_drawer)
            }
            R.id.txtFaq -> {
                if (getCurrentFragment() !is FaqFragment) {
                    ChangeFragment(FaqFragment(), true)
                }
                Utility.hideKeyboard(this)

                drawer_layout.closeDrawer(nav_drawer)
            }

            R.id.txtLogout -> {
                drawer_layout.closeDrawer(nav_drawer)
                showDialogue(this)

            }
        }
    }

    fun showDialogue(context: Context) {
        val dialog = Dialog(context)
        val window = dialog.window!!
        window.setBackgroundDrawableResource(android.R.color.transparent)
        window.requestFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.getWindow()!!.setBackgroundDrawableResource(R.drawable.rectangle_background);
        dialog.setContentView(R.layout.popup_yes_or_no)
        dialog.show()

        val title = dialog.findViewById(R.id.textview_title) as TextView
        val text_message = dialog.findViewById(R.id.text_message) as TextView
        val text_yes = dialog.findViewById(R.id.text_yes) as TextView
        val text_no = dialog.findViewById(R.id.text_no) as TextView
        title.setText(context.resources.getString(R.string.logout))
        text_message.setText(context.resources.getString(R.string.clear_history_text))

        /* text_message.setText(message)
         title.setText(title)*/
        text_yes.setText(context.resources.getString(R.string.yes))
        text_no.setText(context.resources.getString(R.string.no))
        text_no.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                dialog.dismiss()
            }

        })
        text_yes.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                dialog.dismiss()
                doLogout(TinyDb(context).getString(Content.FCM_TOKEN))
            }

        })
    }


    private fun getCurrentFragment(): Fragment? {
        return supportFragmentManager.findFragmentById(R.id.frame_container)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        getCurrentFragment()?.onActivityResult(requestCode, resultCode, data)
    }


    fun doLogout(fcm_token: String) {
        customProgress.showProgress(context, getString(R.string.please_wait), false)
        apiInterface = APIClient.getretrofit(this).create(ApiInterface::class.java)
        val call: Call<JsonObject> = apiInterface.logout(fcm_token)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                customProgress.hideProgress()
                Log.e("Response_code", "=" + response.code());
                if (response.code() == 200) {
                    val jsonObject = JSONObject(response.body().toString())
                    if (jsonObject.optBoolean("status")) {
                        var data = jsonObject.optJSONObject("data")
                        mApplicationClass.getInstance()?.removeFCMToken()
                        TinyDb(context).clear(context)
                        Toast.makeText(context, jsonObject.optString("message"), Toast.LENGTH_LONG).show()
                        startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                        finishAffinity()
                    } else {
                        customProgress.hideProgress()

                        Utility().showOkDialog(
                                context,
                                resources.getString(R.string.app_name),
                                jsonObject.optString("message")
                        )
                    }
                } else {
                    customProgress.hideProgress()
                    if (response.errorBody() != null) {
                        val `object` = JSONObject(response.errorBody()!!.string())

                        Utility().showOkDialog(
                                context,
                                resources.getString(R.string.app_name),
                                `object`.optString("message")
                        )
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                customProgress.hideProgress()
                Utility().showOkDialog(
                        context,
                        resources.getString(R.string.app_name),
                        getString(R.string.error_something_is_wrong_ln)
                )

            }
        })


    }

    override fun onBackPressed() {

        if (getCurrentFragment() is HomeFragment) {
            finish()
        } else {
            super.onBackPressed()

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(context!!).unregisterReceiver(notificationReciever)
    }
}
