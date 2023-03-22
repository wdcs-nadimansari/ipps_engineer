package com.webclues.IPPSEngineer.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.gson.JsonObject
import com.webclues.IPPSEngineer.R
import com.webclues.IPPSEngineer.service.APIClient
import com.webclues.IPPSEngineer.service.checkNetworkState
import com.webclues.IPPSEngineer.utility.Content
import com.webclues.IPPSEngineer.utility.CustomProgress
import com.webclues.IPPSEngineer.utility.Log
import com.webclues.IPPSEngineer.utility.Utility
import com.webclues.IPPSEngineer.service.ApiInterface
import kotlinx.android.synthetic.main.activity_forget_password.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgetPasswordActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var apiInterface: ApiInterface
    lateinit var customProgress: CustomProgress
    lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password)
        context = this
        initview()
    }

    private fun initview() {
        customProgress = CustomProgress.instance
        tvsend.setOnClickListener(this)
        tvhome.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tvsend -> if (isvalidate()) {
                forgotpassword(edtEmailForgot.text.toString().trim())
            }
            R.id.tvhome -> {
                finish()
            }
        }
    }

    private fun isvalidate(): Boolean {

        var isValid = true
        if (!Utility().isEmailValid(context, edtEmailForgot)) {
            isValid = false
        }
        if (!checkNetworkState(context)) {
            Utility().showOkDialog(
                context,
                resources.getString(R.string.app_name),
                resources.getString(R.string.error_internet_ln)
            )
            isValid = false
        }
        return isValid
    }


    fun forgotpassword(email: String) {
        customProgress.showProgress(this, getString(R.string.please_wait), false)
        apiInterface = APIClient.getretrofit(this).create(ApiInterface::class.java)
        val call: Call<JsonObject> = apiInterface.forgetpassword(
            email, Content.ENGINEER_POSITION

        )
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                customProgress.hideProgress()
                val jsonObject = JSONObject(response.body().toString())
                val statuscode = jsonObject.optInt("status_code")
                Log.e("Response_code", "=" + response.code());
//                dismissprogressdialog()
                if (statuscode == 200) {
                    if (jsonObject.optBoolean("status")) {
                        var data = jsonObject.optJSONObject("data")
                        Toast.makeText(context, jsonObject.optString("message"), Toast.LENGTH_LONG)
                            .show()
                    } else {

                        Utility().showOkDialog(
                            context,
                            resources.getString(R.string.app_name),
                            jsonObject.optString("message")
                        )
                    }
                } else if (statuscode == 403) {
                    Utility().showInactiveDialog(
                        context,
                        resources.getString(R.string.app_name),
                        jsonObject.optString("message")
                    )


                } else {
                    Utility().showOkDialog(
                        context,
                        resources.getString(R.string.app_name),
                        jsonObject.optString("message")
                    )
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
}
