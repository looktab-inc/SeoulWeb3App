package com.looktab.echonupy.feature.plantnft

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.looktab.echonupy.R
import com.looktab.echonupy.base.BaseFragment
import com.looktab.echonupy.databinding.FragmentPlantNftBinding
import com.looktab.echonupy.feature.MainViewModel
import com.looktab.echonupy.feature.plantnft.model.Plant


class PlantNftFragment : BaseFragment<FragmentPlantNftBinding>(R.layout.fragment_plant_nft) {

    private val activityViewModel: MainViewModel by activityViewModels()
    lateinit var mContext: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun initViews() {
        binding.viewModel = activityViewModel
        binding.btnBack.setOnClickListener {
            closeFragment()
        }
        val list = ArrayList<Plant>()

        list.add(
            Plant(
                name = "데이지 씨앗", description = "LV 0, 0co2", visible = true,
                url = "https://raw.githubusercontent.com/Looktab-naer/imgs/main/pp/plant1.png"
            )
        )
        list.add(
            Plant(
                name = "장미꽃 새싹", description = "LV 1, 100co2", visible = true,
                url = "https://raw.githubusercontent.com/Looktab-naer/imgs/main/pp/plant6.png"
            )
        )
        list.add(
            Plant(
                name = "장미꽃", description = "LV 3, 300co2", visible = false,
                url = "https://raw.githubusercontent.com/Looktab-naer/imgs/main/pp/plant3.png"
            )
        )
        list.add(
            Plant(
                name = "소나무", description = "LV 2, 230co2", visible = true,
                url = "https://raw.githubusercontent.com/Looktab-naer/imgs/main/pp/plant4.png"
            )
        )
        list.add(
            Plant(
                name = "동백나무", description = "LV 5, 1130co2", visible = true,
                url = "https://raw.githubusercontent.com/Looktab-naer/imgs/main/pp/plant5.png"
            )
        )
        val adater = ViewPager2Adater(list, mContext)

        binding.viewpager2.offscreenPageLimit = 5
        binding.viewpager2.getChildAt(0).overScrollMode = View.OVER_SCROLL_NEVER
        binding.viewpager2.adapter = adater


        setupOnBoardingIndicators()
        setCurrentOnboardingIndicator(0)

        var transform = CompositePageTransformer()
        transform.addTransformer(MarginPageTransformer(8))

        transform.addTransformer(ViewPager2.PageTransformer { view: View, fl: Float ->
            var v = 1 - Math.abs(fl)
            view.scaleY = 0.8f + v * 0.2f
        })

        binding.viewpager2.setPageTransformer(transform)


        binding.viewpager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                setCurrentOnboardingIndicator(position)
            }

        })

    }

    private fun setupOnBoardingIndicators() {
        val indicators =
            arrayOfNulls<ImageView>(5)

        var layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )

        layoutParams.setMargins(8, 0, 8, 0)

        for (i in indicators.indices) {
            indicators[i] = ImageView(mContext)
            indicators[i]?.setImageDrawable(
                ContextCompat.getDrawable(
                    mContext,
                    R.drawable.onboarding_indicator_inactivie
                )
            )

            indicators[i]?.layoutParams = layoutParams

            binding.indicators.addView(indicators[i])
        }
    }

    private fun setCurrentOnboardingIndicator(index: Int) {
        val childCount = binding.indicators.childCount
        for (i in 0 until childCount) {
            val imageView = binding.indicators.getChildAt(i) as ImageView
            if (i == index) {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        mContext,
                        R.drawable.onboarding_indicator_active
                    )
                )
            } else {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        mContext,
                        R.drawable.onboarding_indicator_inactivie
                    )
                )
            }
        }
    }

    private fun closeFragment() {
        val fragmentManager = activity?.supportFragmentManager
        fragmentManager?.let {
            it.beginTransaction().remove(this@PlantNftFragment).commit()
            it.popBackStack()
        }
    }

    companion object {
        fun newInstance() = PlantNftFragment()
    }
}