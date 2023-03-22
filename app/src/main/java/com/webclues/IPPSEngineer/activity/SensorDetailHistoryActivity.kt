package com.webclues.IPPSEngineer.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.webclues.IPPSEngineer.R

class SensorDetailHistoryActivity : AppCompatActivity(),View.OnClickListener {

    lateinit var ivBack:ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sensor_detail_history)

        initview()
    }

    private fun initview(){
        ivBack=findViewById(R.id.ivBack)
        ivBack?.setOnClickListener(this)
    }

    override fun onClick(v: View?) {

        when(v?.id){
            R.id.ivBack->{
                onBackPressed()
            }
        }
    }
}
