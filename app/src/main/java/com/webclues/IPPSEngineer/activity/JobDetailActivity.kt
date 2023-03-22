package com.webclues.IPPSEngineer.activity

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.webclues.IPPSEngineer.Modelclass.JobDetailModel
import com.webclues.IPPSEngineer.R
import com.webclues.IPPSEngineer.adapter.JobImageViewpagerAdapter
import com.webclues.IPPSEngineer.service.APIClient
import com.webclues.IPPSEngineer.service.checkNetworkState
import com.webclues.IPPSEngineer.utility.Content
import com.webclues.IPPSEngineer.utility.CustomProgress
import com.webclues.IPPSEngineer.utility.TinyDb
import com.webclues.IPPSEngineer.utility.Utility
import com.webclues.IPPSEngineer.service.ApiInterface
import kotlinx.android.synthetic.main.activity_job_detail.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class JobDetailActivity : AppCompatActivity(), View.OnClickListener {


    var dotscount: Int = 0
    lateinit var dots: Array<ImageView?>
    lateinit var viewpagerAdapter: JobImageViewpagerAdapter
    var type: Int = 0
    lateinit var selected: String
    lateinit var context: Context
    lateinit var apiInterface: ApiInterface
    lateinit var jobDetailModel: JobDetailModel
    lateinit var customProgress: CustomProgress
    lateinit var JobStaus: String
    var JobId: Int = 0
    var priority: Int = 0
    var status: Int = 0
    lateinit var time_duration: String
    lateinit var JobPriority: String
    var changedStatus = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_job_detail)
        context = this
        initview()
    }

    private fun initview() {

        customProgress = CustomProgress.instance
        apiInterface = APIClient.getretrofit(this).create(ApiInterface::class.java)

        if (intent.extras != null) {
            JobId = intent.getIntExtra(Content.JOB_ID, 0)
            status = intent.getIntExtra(Content.JOB_STATUS, 0)
            priority = intent.getIntExtra(Content.JOB_PRIORITY, 0)
        }


        SetListner()


        if (checkNetworkState(this)) {
            jobdetail(JobId)
        } else {
            Utility().showOkDialog(
                this,
                resources.getString(R.string.app_name),
                resources.getString(R.string.error_internet_ln)
            )
        }

    }


    private fun SetListner() {

        ivBack.setOnClickListener(this)
        btnEndJob.setOnClickListener(this)
        txtStartJob.setOnClickListener(this)
        btnAccept.setOnClickListener(this)
        btnDecline.setOnClickListener(this)

    }


    private fun jobdetail(jobid: Int) {                  //JobDetail Data
        customProgress.showProgress(this, getString(R.string.please_wait), false)
        val call: Call<JsonObject> = apiInterface.jobdetails(jobid)
        call.enqueue(object : Callback<JsonObject> {


            override fun onResponse(
                call: Call<JsonObject>,
                response: Response<JsonObject>
            ) {
                customProgress.hideProgress()

                val jsonObject = JSONObject(response.body().toString())
                val statuscode = jsonObject.optInt("status_code")
                if (statuscode == 200) {
                    if (jsonObject.optBoolean("status")) {
                        val data = jsonObject.optJSONObject("data")
                        if (data != null) {
                            changedStatus = true
                            jobDetailModel =
                                Gson().fromJson(data.toString(), JobDetailModel::class.java)
                            SetJobDetailData(jobDetailModel)
//                            setimageviewpageradapter(jobDetailModel)

                        }
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

    private fun Setstatusandpriority(jobDetailModel: JobDetailModel) {


        if (jobDetailModel.priority == Content.PENDING) {
            JobPriority = getString(R.string.pending)
        } else if (jobDetailModel.priority == Content.ALL) {
            JobPriority = getString(R.string.all)

        } else if (jobDetailModel.priority == Content.LOW) {
            JobPriority = getString(R.string.low)

        } else if (jobDetailModel.priority == Content.MEDIUM) {
            JobPriority = getString(R.string.medium)

        } else if (jobDetailModel.priority == Content.HIGH) {
            JobPriority = getString(R.string.high)
        }
    }

    private fun SetJobDetailData(jobDetailModel: JobDetailModel) {


        Setstatusandpriority(jobDetailModel)

        if (jobDetailModel.job_status == Content.JOB_REQUEST) {
            JobStaus = getString(R.string.job_request)


            txtStatus.setTextColor(ContextCompat.getColor(this, R.color.fontColorRegularGrey))
            txtStatus.setBackgroundResource(R.drawable.rounded_lightgrey)

            txtStatusPriority.setBackgroundResource(R.drawable.round_lightblue_bg)

            txtOperatorName.text =
                jobDetailModel.created_user_name + " | " + Utility.getDateTime(jobDetailModel.created_time)

            txtMachineName.text = jobDetailModel.machine_name
            txtLocation.text = jobDetailModel.location_name
            txtProblem.text = jobDetailModel.problem_name

            if (!jobDetailModel.comment.isNullOrEmpty()) {
                llDescriptions.visibility = View.VISIBLE
                txtCommentDesc.text = jobDetailModel.comment
            }


            llEngineerName.visibility = View.GONE
            txtJobStartTime.visibility = View.GONE
            llJobDuration.visibility = View.GONE
            txtStartJob.visibility = View.GONE
            llRequest.visibility = View.GONE

        } else if (jobDetailModel.job_status == Content.COMPLETED) {
            JobStaus = getString(R.string.completed)


            txtStatus.setTextColor(ContextCompat.getColor(this, R.color.green))
            txtStatus.setBackgroundResource(R.drawable.round_lightgreen_bg)

            txtStatusPriority.setBackgroundResource(R.drawable.round_lightblue_bg)
            llMetalDesc.setBackgroundColor(ContextCompat.getColor(this, R.color.extraLightGrey))

            txtOperatorName.text =
                jobDetailModel.created_user_name + " | " + Utility.getDateTime(jobDetailModel.created_time)


            txtEngineerName.text = jobDetailModel.engineer_name
            txtLocation.text = jobDetailModel.location_name
            txtMachineName.text = jobDetailModel.machine_name
            txtProblem.text = jobDetailModel.problem_name
            txtJobDuration.text = jobDetailModel.job_duration

            txtJobStartTime.text = Utility.getDateTime(jobDetailModel.job_start_time.toLong())

            if (!jobDetailModel.comment.isNullOrEmpty()) {
                llDescriptions.visibility = View.VISIBLE
                txtCommentDesc.text = jobDetailModel.comment
            }


            llJobStartTime.visibility = View.VISIBLE

        } else if (jobDetailModel.job_status == Content.ASSIGNED) {
            JobStaus = getString(R.string.assigned)

            txtStatus.setTextColor(ContextCompat.getColor(this, R.color.yellow))
            txtStatus.setBackgroundResource(R.drawable.round_lightblue_bg)

            txtStatusPriority.setBackgroundResource(R.drawable.round_lightblue_bg)
            txtOperatorName.text =
                jobDetailModel.created_user_name + " | " + Utility.getDateTime(jobDetailModel.created_time)

            txtEngineerName.text = jobDetailModel.engineer_name
            txtMachineName.text = jobDetailModel.machine_name
            txtLocation.text = jobDetailModel.location_name
            txtProblem.text = jobDetailModel.problem_name

            if (!jobDetailModel.comment.isNullOrEmpty()) {
                llDescriptions.visibility = View.VISIBLE
                txtCommentDesc.text = jobDetailModel.comment
            }


            if (TinyDb(context).getString(Content.USER_ID).equals(jobDetailModel.engineer_id)) {
                llRequest.visibility = View.VISIBLE
            } else {
                llRequest.visibility = View.GONE
            }

            llJobDuration.visibility = View.GONE
            llEngineerName.visibility = View.VISIBLE
            edtJobDuration.visibility = View.GONE
            btnEndJob.visibility = View.GONE


        } else if (jobDetailModel.job_status == Content.WORKORDER) {
            JobStaus = getString(R.string.workorder)


            txtStatus.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
            txtStatus.setBackgroundResource(R.drawable.round_lightblue_bg)

            txtStatusPriority.setBackgroundResource(R.drawable.round_lightblue_bg)
            txtOperatorName.text =
                jobDetailModel.created_user_name + " | " + Utility.getDateTime(jobDetailModel.created_time)

            txtMachineName.text = jobDetailModel.machine_name
            txtLocation.text = jobDetailModel.location_name
            txtProblem.text = jobDetailModel.problem_name
            txtEngineerName.text = jobDetailModel.engineer_name

            if (!jobDetailModel.comment.isNullOrEmpty()) {
                llDescriptions.visibility = View.VISIBLE
                txtCommentDesc.text = jobDetailModel.comment
            }

            txtJobStartTime.text = Utility.getDateTime(jobDetailModel.job_start_time.toLong())


            txtEndJobDuration.setOnChronometerTickListener(object :
                Chronometer.OnChronometerTickListener {
                @Override
                public override fun onChronometerTick(chronometer: Chronometer) {
                    val calander: Calendar = Calendar.getInstance()
                    val time: Long =
                        calander.timeInMillis - jobDetailModel.job_start_time.toLong();
                    val h: Long = (time / 3600000)
                    val m: Int = ((time - h * 3600000) / 60000).toInt()
                    val s: Int = ((time - h * 3600000 - m * 60000) / 1000).toInt()
                    /* val t:String  = (h < 10 ? "0"+h: h)+":"+(m < 10 ? "0"+m: m)+":"+ (s < 10 ? "0"+s: s)
                     val t:String =*/
                    time_duration =
                        (if (h < 10) "0" + h else h).toString() + ":" + (if (m < 10) "0" + m else m) + ":" + (if (s < 10) "0" + s else s)
                    chronometer.setText(time_duration);
                }
            });

            txtEndJobDuration.start();

            llJobStartTime.visibility = View.VISIBLE
            edtJobDuration.visibility = View.VISIBLE
            llJobDuration.visibility = View.GONE
            llEngineerName.visibility = View.VISIBLE
            edtJobDuration.visibility = View.VISIBLE
            btnEndJob.visibility = View.VISIBLE


        } else if (jobDetailModel.job_status == Content.DECLINE) {
            JobStaus = getString(R.string.decline)

            txtStatus.setBackgroundResource(R.drawable.round_lightred_bg)
            txtStatus.setTextColor(ContextCompat.getColor(this, R.color.red))

            txtStatusPriority.setBackgroundResource(R.drawable.round_lightblue_bg)
            llMetalDesc.setBackgroundColor(ContextCompat.getColor(this, R.color.extraLightGrey))
            txtOperatorName.text =
                jobDetailModel.created_user_name + " | " + Utility.getDateTime(jobDetailModel.created_time)


            txtMachineName.text = jobDetailModel.machine_name
            txtLocation.text = jobDetailModel.location_name
            txtProblem.text = jobDetailModel.problem_name

            txtDeclineBy.setText(context.resources.getString(R.string.decline_by) + " " + jobDetailModel.declined_by + ":")
            txtDeclineByUser.text = jobDetailModel.declined_by_user
            txtDeclineReason.text = jobDetailModel.decline_reason

            if (!jobDetailModel.comment.isNullOrEmpty()) {
                llDescriptions.visibility = View.VISIBLE
                txtCommentDesc.text = jobDetailModel.comment
            }


            llComments.visibility = View.GONE
            llJobDuration.visibility = View.GONE
            llEngineerName.visibility = View.GONE
            llDeclineBy.visibility = View.VISIBLE
            view.visibility = View.VISIBLE
            txtJobStartTime.visibility = View.GONE
//            btnUpdateJob.visibility = View.VISIBLE

        } else if (jobDetailModel.job_status == Content.INCOMPLETE) {
            JobStaus = getString(R.string.Incomplete)


            txtStatus.setBackgroundResource(R.drawable.orange_bg)
            txtStatus.setTextColor(ContextCompat.getColor(this, R.color.orange))

            txtStatusPriority.setBackgroundResource(R.drawable.round_lightblue_bg)
            llMetalDesc.setBackgroundColor(ContextCompat.getColor(this, R.color.extraLightGrey))
            txtOperatorName.text =
                jobDetailModel.created_user_name + " | " + Utility.getDateTime(jobDetailModel.created_time)

            txtMachineName.text = jobDetailModel.machine_name
            txtLocation.text = jobDetailModel.location_name
            txtProblem.text = jobDetailModel.problem_name
            txtJobDuration.text = jobDetailModel.job_duration
            txtEngineerName.text = jobDetailModel.engineer_name

            txtIncompleteReason.text = jobDetailModel.incomplete_reason
            if (!jobDetailModel.job_start_time.equals("")) {
                txtJobStartTime.text = Utility.getDateTime(jobDetailModel.job_start_time.toLong())
            }

            if (!jobDetailModel.comment.isNullOrEmpty()) {
                llDescriptions.visibility = View.VISIBLE
                txtCommentDesc.text = jobDetailModel.comment
            }


            llEngineerComment.visibility = View.GONE
            llJobDuration.visibility = View.VISIBLE
            llEngineerName.visibility = View.VISIBLE
            view.visibility = View.VISIBLE
            llIncompleteReason.visibility = View.VISIBLE
            llJobStartTime.visibility = View.VISIBLE

        }
        txtStatus.setText(JobStaus)
        txtStatusPriority.setText(JobPriority)


        viewpagerAdapter = JobImageViewpagerAdapter(this, jobDetailModel.images)
        ivViewPager.adapter = viewpagerAdapter
        SetImagecount()

    }


    private fun SetImagecount() {
        dotscount = viewpagerAdapter.count
        dots = arrayOfNulls<ImageView>(dotscount)
        for (i in 0..dotscount - 1) {
            dots[i] = ImageView(this)
            dots[i]!!.setImageDrawable(
                ContextCompat.getDrawable(
                    applicationContext,
                    R.drawable.non_active_dot
                )
            )

            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )

            params.setMargins(8, 0, 8, 0)

            Indicator.addView(dots[i], params)
        }


        dots[0]!!.setImageDrawable(
            ContextCompat.getDrawable(
                applicationContext,
                R.drawable.non_active_dot
            )
        );
        ivViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {

                for (i in 0 until dotscount) {
                    dots[i]!!.setImageDrawable(
                        ContextCompat.getDrawable(
                            applicationContext,
                            R.drawable.non_active_dot
                        )
                    )
                }

                dots[position]!!.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.active_dots
                    )
                )
            }
        })
    }


    override fun onClick(v: View?) {

        when (v!!.id) {
            R.id.ivBack -> {
                onBackPressed()
            }


            R.id.txtStartJob -> {
                txtStartJob.visibility = View.GONE
                edtJobDuration.visibility = View.VISIBLE
                btnEndJob.visibility = View.VISIBLE
            }
            R.id.btnAccept -> {

                statjob(JobId)
            }
            R.id.btnDecline -> {
                ShowDecline()
            }
            R.id.btnEndJob -> {
                ShowEndJob()
            }

        }
    }

    private fun statjob(Jobid: Int) {
        customProgress.showProgress(this, getString(R.string.please_wait), false)
        val call: Call<JsonObject> = apiInterface.startjob(Jobid)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.code() == 200) {
                    customProgress.hideProgress()
                    val jsonObject = JSONObject(response.body().toString())
                    if (jsonObject.optBoolean("status")) {
                        val data = jsonObject.optJSONObject("data")
                        val job_start_datetime = data.optString("job_start_datetime")

                        setworkorderdata(job_start_datetime)
                    } else {
                        customProgress.hideProgress()
                        Utility().showOkDialog(
                            context,
                            resources.getString(R.string.app_name),
                            jsonObject.optString("message")
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

    private fun setworkorderdata(jobstarttime: String) {
        txtStatus.setText(resources.getString(R.string.workorder))
        txtStatus.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
        txtStatus.setBackgroundResource(R.drawable.round_lightblue_bg)
        txtStatusPriority.setBackgroundResource(R.drawable.round_lightblue_bg)


        llRequest.visibility = View.GONE
        edtJobDuration.visibility = View.VISIBLE
        btnEndJob.visibility = View.VISIBLE
        llJobStartTime.visibility = View.VISIBLE

        txtJobStartTime.text = Utility.getDateTime(jobstarttime.toLong())

        txtEndJobDuration.setOnChronometerTickListener(object :
            Chronometer.OnChronometerTickListener {
            @Override
            public override fun onChronometerTick(chronometer: Chronometer) {
                val calander: Calendar = Calendar.getInstance()
                val time: Long =
                    calander.timeInMillis - jobstarttime.toLong();
                val h: Long = (time / 3600000)
                val m: Int = ((time - h * 3600000) / 60000).toInt()
                val s: Int = ((time - h * 3600000 - m * 60000) / 1000).toInt()

                time_duration =
                    (if (h < 10) "0" + h else h).toString() + ":" + (if (m < 10) "0" + m else m) + ":" + (if (s < 10) "0" + s else s)
                chronometer.setText(time_duration);
            }
        });

        txtEndJobDuration.start();


    }


    private fun ShowDecline() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.getWindow()!!.setBackgroundDrawableResource(R.drawable.rectangle_background);
        dialog.setContentView(
            R.layout.dialog_decline
        )
        val edtComment: EditText = dialog.findViewById(R.id.edtAddComment)
        val btnSend: Button = dialog.findViewById(R.id.btnSend)
        btnSend.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if (edtComment.text.toString().isEmpty()) {
                    edtComment.setError(getString(R.string.error_decline_comment_blank))
                    return
                }
                Declinejob(JobId, edtComment.text.toString().trim(), dialog)

            }

        })
        dialog.show()
    }


    private fun Declinejob(jobid: Int, comment: String, dialog: Dialog) {
        customProgress.showProgress(context, getString(R.string.please_wait), false)
        val call: Call<JsonObject> = apiInterface.declinejob(jobid, comment)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val jsonObject = JSONObject(response.body().toString())
                val statuscode = jsonObject.optInt("status_code")
                if (statuscode == 200) {
                    customProgress.hideProgress()
                    if (jsonObject.optBoolean("status")) {
                        Toast.makeText(
                            context, jsonObject.optString("message"),
                            Toast.LENGTH_LONG
                        ).show()
                        dialog.dismiss()
                        startActivity(Intent(context, MainActivity::class.java))
                        finishAffinity()
                    } else {
                        customProgress.hideProgress()
                        Utility().showOkDialog(
                            context,
                            resources.getString(R.string.app_name),
                            jsonObject.optString("message")
                        )
                    }
                } else if (statuscode == 403) {
                    customProgress.hideProgress()
                    Utility().showInactiveDialog(
                        context,
                        resources.getString(R.string.app_name),
                        jsonObject.optString("message")
                    )
                } else {
                    customProgress.hideProgress()
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

    private fun ShowEndJob() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.getWindow()!!.setBackgroundDrawableResource(R.drawable.rectangle_background);
        dialog.setContentView(
            R.layout.dialog_end_job
        )
        val edtAddcomment = dialog.findViewById(R.id.edtAddComment) as EditText
        val rbcompleted: RadioButton = dialog.findViewById(R.id.rbCompleted)
        val rbenabletocmplete: RadioButton = dialog.findViewById(R.id.rbEnableToComplete)
        val btnSave: Button = dialog.findViewById(R.id.btnSave)
        val RadioGroup: RadioGroup = dialog.findViewById(R.id.RadioGroup)
        selected = resources.getString(R.string.job_request)
        edtAddcomment.setText("")
        rbcompleted.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                if (isChecked) {
                    rbcompleted.isChecked = true
                    rbenabletocmplete.isChecked = false
                }
                edtAddcomment.isClickable = false
                edtAddcomment.isEnabled = false
                edtAddcomment.setText("")
                selected = resources.getString(R.string.completed)
                type = 1

            }

        })
        rbenabletocmplete.setOnCheckedChangeListener(object :
            CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                type = 2
                if (isChecked) {
                    rbcompleted.isChecked = false
                    rbenabletocmplete.isChecked = true
                }
                edtAddcomment.isClickable = true
                edtAddcomment.isEnabled = true
                edtAddcomment.setText("no spare parts")
                selected = resources.getString(R.string.Incomplete)
                type = 2
            }
        })
        btnSave.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if (RadioGroup.checkedRadioButtonId == -1) {
                    Toast.makeText(
                        getApplicationContext(),
                        getString(R.string.radio_option_blnk),
                        Toast.LENGTH_SHORT
                    ).show();
                    return
                }
                if (type == 2) {
                    if (edtAddcomment.text.toString().isEmpty()) {
                        Toast.makeText(
                            getApplicationContext(),
                            getString(R.string.error_decline_comment_blank),
                            Toast.LENGTH_SHORT
                        ).show();
                        return
                    }
                }
                endjob(JobId, time_duration, type, edtAddcomment.text.toString().trim(), dialog)
            }

        })

        dialog.show()
    }


    private fun endjob(
        jobid: Int,
        timeduration: String,
        type: Int,
        comment: String,
        dialog: Dialog
    ) {
        customProgress.showProgress(this, getString(R.string.please_wait), false)
        val call: Call<JsonObject> = apiInterface.endjob(jobid, timeduration, type, comment)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                customProgress.hideProgress()
                if (response.code() == 200) {
                    val jsonObject = JSONObject(response.body().toString())
                    if (jsonObject.optBoolean("status")) {
                        Toast.makeText(
                            context, jsonObject.optString("message"),
                            Toast.LENGTH_LONG
                        ).show()
                        dialog.dismiss()
                        startActivity(Intent(context, MainActivity::class.java))
                        finishAffinity()

                    } else {
                        customProgress.hideProgress()
                        Utility().showOkDialog(
                            context,
                            resources.getString(R.string.app_name),
                            jsonObject.optString("message")
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
        if (changedStatus) {
            val intent = Intent()
            intent.putExtra("JobType", Content.JOB_TYPE)
            setResult(Activity.RESULT_OK, intent)
            finish()
        } else {
            super.onBackPressed()
        }
    }

}
