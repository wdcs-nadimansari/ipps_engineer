package com.webclues.IPPSEngineer.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.webclues.IPPSEngineer.Modelclass.EngineerResponseItem
import com.webclues.IPPSEngineer.R
import com.webclues.IPPSEngineer.adapter.EngineerAdapter
import kotlinx.android.synthetic.main.activity_engineer_list.*
import kotlinx.android.synthetic.main.custom_toolbar.*

class EngineerListActivity : AppCompatActivity(), View.OnClickListener {

    private var toolbar: Toolbar? = null
    private var EngineerAdapter: EngineerAdapter? = null
    private var EnginnerArraylist: ArrayList<EngineerResponseItem>? = arrayListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_engineer_list)

        initview()
    }

    private fun initview() {
        toolbar = findViewById(R.id.Toolbar)
        setSupportActionBar(toolbar)

        ivBack.setOnClickListener(this)

        SetEngineerdata()
    }

    private fun SetEngineerdata() {
        EnginnerArraylist!!.add(
            EngineerResponseItem(
                R.drawable.change_profile,
                "Matthew Porter", resources.getString(R.string.mechanical_engineer)
            )
        )
        EnginnerArraylist!!.add(
            EngineerResponseItem(
                R.drawable.change_profile,
                "Mike Lynch", resources.getString(R.string.civil_engineer)
            )
        )
        EnginnerArraylist!!.add(
            EngineerResponseItem(
                R.drawable.change_profile,
                "Victoria Mccoy", resources.getString(R.string.mechanical_engineer)
            )
        )
        EnginnerArraylist!!.add(
            EngineerResponseItem(
                R.drawable.change_profile,
                "Kevin Holmes", resources.getString(R.string.mechanical_engineer)
            )
        )

        EnginnerArraylist!!.add(
            EngineerResponseItem(
                R.drawable.change_profile,
                "Barbara Mccoy", resources.getString(R.string.electrical_engineer)
            )
        )
        EnginnerArraylist!!.add(
            EngineerResponseItem(
                R.drawable.change_profile,
                "Beatrice Andrews", resources.getString(R.string.mechanical_engineer)
            )
        )
        EnginnerArraylist!!.add(
            EngineerResponseItem(
                R.drawable.change_profile,
                "Francine Evans", resources.getString(R.string.electrical_engineer)
            )
        )
        EnginnerArraylist!!.add(
            EngineerResponseItem(
                R.drawable.change_profile,
                "Roy Jones", resources.getString(R.string.mechanical_engineer)
            )
        )
        SetAdapter(this, EnginnerArraylist!!)


    }

    private fun SetAdapter(
        context: EngineerListActivity,
        EngineerArraylist: ArrayList<EngineerResponseItem>
    ) {
        rvEngneer.layoutManager = LinearLayoutManager(this)
        rvEngneer.setHasFixedSize(true)
        EngineerAdapter = EngineerAdapter(context, EngineerArraylist);
        rvEngneer.adapter = EngineerAdapter
    }

    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.ivBack -> {
                startActivity(Intent(this, MainActivity::class.java))
            }
        }
    }

    override fun onResume() {
        super.onResume()
        txtTitle.setText(resources.getString(R.string.engineers))
        ivmenu.visibility = View.GONE
        ivback.visibility = View.VISIBLE
    }

}
