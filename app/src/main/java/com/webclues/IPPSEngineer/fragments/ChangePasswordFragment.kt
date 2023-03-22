package com.webclues.IPPSEngineer.fragments


import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import com.google.gson.JsonObject

import com.webclues.IPPSEngineer.R
import com.webclues.IPPSEngineer.activity.MainActivity
import com.webclues.IPPSEngineer.service.APIClient
import com.webclues.IPPSEngineer.service.checkNetworkState
import com.webclues.IPPSEngineer.utility.CustomProgress
import com.webclues.IPPSEngineer.utility.Log
import com.webclues.IPPSEngineer.utility.Utility
import com.webclues.IPPSEngineer.service.ApiInterface
import kotlinx.android.synthetic.main.fragment_change_password.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 */
class ChangePasswordFragment : Fragment(), View.OnClickListener {

    lateinit var customProgress: CustomProgress
    lateinit var apiInterface: ApiInterface

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_change_password, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }


    private fun initView() {
        customProgress = CustomProgress.instance
        btnChangePassword.setOnClickListener(this)
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnChangePassword -> {
                if (checkValidation()) {
                    changepassword(
                        edtOldPassword.text.toString().trim(),
                        edtNewPassWord.text.toString().trim(),
                        edtConformPass.text.toString().trim()
                    )
                }
            }
        }
    }

    private fun checkValidation(): Boolean {

        var isValid = true
        if (!Utility().isCurrentPasswordValid(activity!!, edtOldPassword)) {
            isValid = false
        }
        if (!Utility().isPassvalid(activity!!, edtNewPassWord)) {
            isValid = false
        }
        if (!Utility().isConfirmPassvalid(activity!!, edtNewPassWord, edtConformPass)) {
            isValid = false
        }
        if (!checkNetworkState(activity!!)) {
            Utility().showOkDialog(
                activity!!,
                resources.getString(R.string.app_name),
                resources.getString(R.string.error_internet_ln)
            )
            isValid = false
        }
        return isValid
    }

    private fun showpopup() {

        val dialog = Dialog(activity!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.getWindow()!!.setBackgroundDrawableResource(R.drawable.rectangle_background);
        dialog.setContentView(
            R.layout.dialog_change_password
        )
        val btnOk: Button = dialog.findViewById(R.id.btnOk)
        btnOk.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                dialog.dismiss()
                ClearPassword()

//                startActivity(Intent(activity!!, LoginActivity::class.java))

            }

        })
        dialog.show()
    }


    private fun changepassword(oldpassword: String, newpassword: String, confirmpassword: String) {
        customProgress.showProgress(activity!!, getString(R.string.please_wait), false)
        apiInterface = APIClient.getretrofit(activity!!).create(ApiInterface::class.java)
        val call: Call<JsonObject> = apiInterface.changepassword(
            oldpassword, newpassword, confirmpassword

        )
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val jsonObject = JSONObject(response.body().toString())
                val statuscode = jsonObject.optInt("status_code")
                Log.e("Response_code", "=" + response.code());

                if (statuscode == 200) {
                    customProgress.hideProgress()

                    if (jsonObject.optBoolean("status")) {
                        var data = jsonObject.optJSONObject("data")
                        /*     Toast.makeText(
                                 activity!!,
                                 jsonObject.optString("message"),
                                 Toast.LENGTH_LONG
                             ).show()*/
                        Utility().showOkDialog(
                            activity!!,
                            resources.getString(R.string.app_name),
                            jsonObject.optString("message")
                        )
                        ClearPassword()
//                        showpopup()
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

    private fun ClearPassword() {

        edtOldPassword.setText("")
        edtNewPassWord.setText("")
        edtConformPass.setText("")
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).txtTitle.setText(resources.getString(R.string.change_password))
    }
}
