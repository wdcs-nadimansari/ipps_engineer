package com.webclues.IPPSEngineer.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.webclues.IPPSEngineer.Modelclass.Image
import com.webclues.IPPSEngineer.R
import com.webclues.IPPSEngineer.activity.JobDetailActivity
import com.webclues.IPPSEngineer.utility.RoundRectCornerImageView
import com.webclues.IPPSEngineer.utility.RoundedTransformation
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.lang.Exception

class JobImageViewpagerAdapter(
    private val context: JobDetailActivity,
    val jobimagearray: List<Image>
) : PagerAdapter() {
    private var layoutInflater: LayoutInflater? = null


    override fun getCount(): Int {
        return jobimagearray.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val jobDetailModel: Image = jobimagearray.get(position)

        layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = layoutInflater!!.inflate(R.layout.adp_jobdetail, null)
        val imageView = view.findViewById(R.id.ivJobImage) as RoundRectCornerImageView
        val progress = view.findViewById(R.id.progress) as ProgressBar

        Picasso.get().load(jobDetailModel.image_url).transform(RoundedTransformation(10, 10))
            .placeholder(R.drawable.ic_placeholder_job)
            .error(R.drawable.ic_placeholder_job)
            .into(imageView, object : Callback {
                override fun onError(e: Exception?) {
                    Toast.makeText(context, e?.message, Toast.LENGTH_LONG).show()
                }

                override fun onSuccess() {
                    progress.visibility = View.GONE
                    imageView.visibility = View.VISIBLE
                }

            })


        val vp = container as ViewPager
        vp.addView(view, 0)
        return view

    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {

        val vp = container as ViewPager
        val view = `object` as View
        vp.removeView(view)

    }
}