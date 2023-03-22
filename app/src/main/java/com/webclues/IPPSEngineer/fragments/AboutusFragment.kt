package com.webclues.IPPSEngineer.fragments


import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.JsonObject

import com.webclues.IPPSEngineer.R
import com.webclues.IPPSEngineer.activity.MainActivity
import com.webclues.IPPSEngineer.service.APIClient
import com.webclues.IPPSEngineer.service.checkNetworkState
import com.webclues.IPPSEngineer.utility.Content
import com.webclues.IPPSEngineer.utility.CustomProgress
import com.webclues.IPPSEngineer.utility.Utility
import com.webclues.IPPSEngineer.service.ApiInterface
import kotlinx.android.synthetic.main.fragment_aboutus.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 */
class AboutusFragment : Fragment() {

    lateinit var fragment: Fragment
    lateinit var customProgress: CustomProgress
    lateinit var apiInterface: ApiInterface

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_aboutus, container, false)
        return view
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).txtTitle.setText(resources.getString(R.string.about_us))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initview()
    }

    private fun initview() {
        customProgress = CustomProgress.instance
        WebView.settings.javaScriptEnabled = true
        WebView.settings.displayZoomControls = true
        WebView.settings.setSupportZoom(true)
        WebView.settings.displayZoomControls = true
        WebView.setBackgroundColor(Color.TRANSPARENT)

        if (checkNetworkState(context!!)) {

            loadPage()
        } else {
            Utility().showOkDialog(
                context!!,
                resources.getString(R.string.app_name),
                resources.getString(R.string.error_internet_ln)
            )
        }
    }


    private fun loadPage() {

        customProgress.showProgress(activity!!, getString(R.string.please_wait), false)

        apiInterface = APIClient.getretrofit(context!!).create(ApiInterface::class.java)

        val call = apiInterface.aboutus(Content.ABOUT_US)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                customProgress.hideProgress();
                var jsonObject = JSONObject(response.body().toString())
                var statuscode = jsonObject.optInt("status_code")
                if (statuscode == 200) {
                    if (jsonObject.optBoolean("status")) {
                        var data = jsonObject.getJSONObject("data");
                        var content = data.optString(Content.CONTENT)
                        WebView.loadDataWithBaseURL(null, content, "text/html", "UTF-8", null);
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


}
