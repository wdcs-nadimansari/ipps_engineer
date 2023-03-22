package com.webclues.IPPSEngineer.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.webclues.IPPSEngineer.Modelclass.UserRespone

import com.webclues.IPPSEngineer.R
import com.webclues.IPPSEngineer.activity.MainActivity
import com.webclues.IPPSEngineer.service.APIClient
import com.webclues.IPPSEngineer.service.checkNetworkState
import com.webclues.IPPSEngineer.utility.*
import com.webclues.IPPSEngineer.service.ApiInterface
import kotlinx.android.synthetic.main.fragment_contact_us.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 */
class ContactUsFragment : Fragment(), View.OnClickListener {
    lateinit var btnSend: Button

    lateinit var customProgress: CustomProgress
    lateinit var apiInterface: ApiInterface
    lateinit var userdetails: UserRespone

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_contact_us, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initview(view)
    }

    private fun initview(view: View) {
        customProgress = CustomProgress.instance
        userdetails = Gson().fromJson(
            TinyDb(activity!!).getString(Content.USER_DATA),
            UserRespone::class.java
        )
        btnSend = view.findViewById(R.id.btnSend)
        btnSend.setOnClickListener(this)
    }


    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.btnSend -> {
                if (CheckValidation()) {
                    contactus(

                        edtSubject.text.toString().trim(),
                        edtMessage.text.toString().trim()
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).txtTitle.setText(resources.getString(R.string.contact_us))
    }

    private fun CheckValidation(): Boolean {
        var isvalid = true
        /*   if (!Utility().isSubjectValid(activity!!, edtSubject)) {
               isvalid = false
           } else if (!Utility().isMessagevalid(activity!!, edtMessage)) {
               isvalid = false
           }*/

        if (!Utility().isSubjectValid(activity!!, edtSubject)) {
            isvalid = false
        }
        if (!Utility().isMessagevalid(activity!!, edtMessage)) {
            isvalid = false
        }
        if (!checkNetworkState(activity!!)) {
            Utility().showOkDialog(
                activity!!,
                resources.getString(R.string.app_name),
                resources.getString(R.string.error_internet_ln)
            )
            isvalid = false
        }
        return isvalid
    }

    private fun contactus(

        subject: String,
        message: String
    ) {
        customProgress.showProgress(activity!!, getString(R.string.please_wait), false)
        apiInterface = APIClient.getretrofit(activity!!).create(ApiInterface::class.java)
        val call: Call<JsonObject> =
            apiInterface.contactus(subject, message)
        call.enqueue(object : Callback<JsonObject> {


            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                val jsonObject = JSONObject(response.body().toString())
                val statuscode = jsonObject.optInt("status_code")
//                dismissloading()
                Log.e("Response_code", "=" + response.code());
                if (statuscode == 200) {
                    customProgress.hideProgress()

                    if (jsonObject.optBoolean("status")) {
                        var data = jsonObject.optJSONObject("data")
                        Toast.makeText(
                            activity!!,
                            jsonObject.optString("message"),
                            Toast.LENGTH_LONG
                        ).show()
                        edtSubject.setText("")
                        edtMessage.setText("")
                    } else {
                        customProgress.hideProgress()

                        Utility().showOkDialog(
                            activity!!,
                            resources.getString(R.string.app_name),
                            jsonObject.optString("message")
                        )
                    }
                } else if (statuscode == 403) {
                    customProgress.hideProgress()

                    Utility().showInactiveDialog(
                        activity!!,
                        resources.getString(R.string.app_name),
                        jsonObject.optString("message")
                    )


                } else {
                    customProgress.hideProgress()
                    Utility().showOkDialog(
                        activity!!,
                        resources.getString(R.string.app_name),
                        jsonObject.optString("message")
                    )
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {

                customProgress.hideProgress()
                Utility().showOkDialog(
                    activity!!,
                    resources.getString(R.string.app_name),
                    getString(R.string.error_something_is_wrong_ln)
                )
            }

        })

    }

}
