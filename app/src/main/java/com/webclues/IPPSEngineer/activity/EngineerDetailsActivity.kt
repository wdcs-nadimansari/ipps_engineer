package com.webclues.IPPSEngineer.activity

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.webclues.IPPSEngineer.R
import com.webclues.IPPSEngineer.fragments.EngineerTaskOrderFragment
import com.webclues.IPPSEngineer.fragments.JobOrderList.*
import com.webclues.IPPSEngineer.utility.CustomViewPager
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_chat.ivBack
import kotlinx.android.synthetic.main.adp_engineerlist.*

class EngineerDetailsActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var EngineerName: String
    private var EngineerProfile: Int? = null
    lateinit var edtEmail: EditText
    lateinit var edtPosition: EditText
    lateinit var edtPhoneNumber: EditText
    lateinit var edtCompny: EditText
    private var onclick: Boolean? = false

    var tabLayout: TabLayout? = null
    var viewpager: CustomViewPager? = null
    var tvpriority: TextView? = null
    lateinit var lnPriority: LinearLayout
    private var prioritytypes: ArrayList<String>? = arrayListOf()
    private lateinit var priorityDropdownView: ListPopupWindow

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_engineer_details)
        initview()
    }

    private fun initview() {

        edtEmail = findViewById(R.id.edtEmailAddress)
        edtPosition = findViewById(R.id.edtPosition)
        edtPhoneNumber = findViewById(R.id.edtPhoneNumber)
        edtCompny = findViewById(R.id.edtCompany)


        viewpager = findViewById(R.id.viewpager)
        tvpriority = findViewById(R.id.tvpriority)
        lnPriority = findViewById(R.id.lnPriority)
        setupwithviewpager(viewpager!!)
        viewpager!!.setPagingEnabled(false)

        tabLayout = findViewById(R.id.tablayout)
        tabLayout!!.setupWithViewPager(viewpager)

        ivBack.setOnClickListener(this)

        val intent: Intent = getIntent();
        if (intent.extras != null) {
            EngineerName = intent.getStringExtra("Engineer_Name")
            EngineerProfile = intent.getIntExtra("Engineer_Profile", 0)

            txtEngineerName.setText(EngineerName)
            Picasso.get().load(EngineerProfile.toString()).into(ivEngineerProfile)
        }

        setData()
        setcustomtabicons()
        Setpriority()

    }


    private fun setData() {
        edtEmail.keyListener = null
        edtPosition.keyListener = null
        edtCompny.keyListener = null
        edtPhoneNumber.keyListener = null

        val face = Typeface.createFromAsset(
            getAssets(),
            "font/lato_bold.ttf"
        )


        edtEmail.setTextColor(ContextCompat.getColor(this, R.color.fontColorBlack))
        edtEmail.setTypeface(face)
        edtPosition.setTextColor(ContextCompat.getColor(this, R.color.fontColorBlack))
        edtPosition.setTypeface(face)
        edtPhoneNumber.setTextColor(ContextCompat.getColor(this, R.color.fontColorBlack))
        edtPhoneNumber.setTypeface(face)
        edtCompny.setTextColor(ContextCompat.getColor(this, R.color.fontColorBlack))
        edtCompny.setTypeface(face)

        edtEmail.setText("denise.barrett@mail.com")
        edtPosition.setText("Mechanical Engineer")
        edtCompny.setText("(208)615-4860")
        edtPhoneNumber.setText("Dictum nulla pvt ltd")
    }

    private fun setcustomtabicons() {

        tabLayout!!.getTabAt(0)!!.setText(resources.getString(R.string.all))


//        tabLayout!!.getTabAt(1)!!.setText(resources.getString(R.string.job_request))

        tabLayout!!.getTabAt(1)!!.setText(resources.getString(R.string.ongoing))

        tabLayout!!.getTabAt(2)!!.setText(
            resources
                .getString(R.string.assigned)
        )



        tabLayout!!.getTabAt(3)!!.setText(resources.getString(R.string.completed))

        val typeface = Typeface.createFromAsset(assets, "font/lato_semibold.ttf")
        tabLayout!!.applyFont(typeface)
//        tabLayout!!.getTabAt(4)!!.setText(resources.getString(R.string.kiv))


    }


    private fun setupwithviewpager(viewPager: ViewPager) {

        val adapter = ViewpagerAdapter(this, supportFragmentManager)
        adapter.addfragment(EngineerTaskOrderFragment(), resources.getString(R.string.all))
//        adapter.addfragment(JobRequestFragment(), resources.getString(R.string.job_request))
        adapter.addfragment(OnGoingFragment(), resources.getString(R.string.ongoing))
        adapter.addfragment(AssignedFragment(), resources.getString(R.string.assigned))
        adapter.addfragment(CompletedFragment(), resources.getString(R.string.completed))
//        adapter.addfragment(KivFragment(), resources.getString(R.string.kiv))
        viewPager.adapter = adapter

    }


    class ViewpagerAdapter(var context: Context, fragmentManager: FragmentManager) :
        FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        private val mFragmentList: ArrayList<Fragment> = arrayListOf()
        private val mFragmentTitleList: ArrayList<String> = ArrayList()

        override fun getCount(): Int {
            return mFragmentList.size
        }

        override fun getItem(position: Int): Fragment {
            return mFragmentList.get(position)
        }

        fun addfragment(fragment: Fragment, title: String) {

            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)

        }

        override fun getPageTitle(position: Int): CharSequence? {


            var title: String? = null
            if (position == 0) {
                title = context.resources.getString(R.string.all)
            } /*else if (position == 1) {
                title = context.resources.getString(R.string.job_request)

            } */
            else if (position == 1) {
                title = context.resources.getString(R.string.ongoing)

            } else if (position == 2) {
                title = context.resources.getString(R.string.assigned)

            } else if (position == 3) {
                title = context.resources.getString(R.string.completed)

            }

//            return mFragmentTitleList.get(position)
            /*   val mTypeface = Typeface.createFromAsset(context.getAssets(), "font/lato_semibold.ttf")

               val typefaceSpan = CalligraphyTypefaceSpan(mTypeface)
               val s = SpannableStringBuilder()
               s.append(title)
               s.setSpan(typefaceSpan, 0, title!!.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
               return SpannableString.valueOf(s)*/

            return title
        }


        /* override fun onClick(v: View?) {

             when (v?.id) {
                 R.id.ivBack -> {
                     onBackPressed()
                 }
             }
         }*/
    }

    private fun Setpriority() {
        prioritytypes =
            getResources().getStringArray(R.array.priority_item).toCollection(ArrayList())
        priorityDropdownView = ListPopupWindow(this)
        priorityDropdownView.setAdapter(
            ArrayAdapter(
                this,
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
        }
        tvpriority!!.setOnClickListener {
            priorityDropdownView.show()
        }

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


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ivBack -> {
                onBackPressed()
            }
        }
    }

}


