package com.looktab.echonupy.feature.web

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.webkit.WebViewClient
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.looktab.echonupy.R
import com.looktab.echonupy.base.BaseFragment
import com.looktab.echonupy.databinding.FragmentWebBinding
import com.looktab.echonupy.feature.MainViewModel

class WebFragment : BaseFragment<FragmentWebBinding>(
    R.layout.fragment_web
) {

    private val activityViewModel: MainViewModel by activityViewModels()
    lateinit var mContext: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun initViews() {
        binding.btnBack.setOnClickListener {
            closeFragment()
        }

        Glide.with(this)
            .load("https://raw.githubusercontent.com/Looktab-naer/imgs/main/pp/seed_1.png")
            .error(
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.ic_upload_img_default,
                    null
                )
            )
            .circleCrop()
            .placeholder(ColorDrawable(Color.parseColor("#DEE2E6")))
            .into(binding.icon)
        binding.webView.apply {
            settings.javaScriptEnabled = true // JavaScript를 사용하려면 설정해야 함
            // WebViewClient를 설정하여 웹 페이지 내부의 링크를 WebView 안에서 열도록 합니다.
            webViewClient = WebViewClient()
            // 웹 페이지 로드
            loadUrl("https://seoul-web3-admin.vercel.app/mobile/chat/mintAddress") // 원하는 웹 페이지 주소로 변경
        }
        binding.btnBack.setOnClickListener {
            closeFragment()
        }
    }

    override fun initObserves() {
    }

    private fun closeFragment() {
        val fragmentManager = activity?.supportFragmentManager
        fragmentManager?.let {
            activityViewModel.updateChat()
            it.beginTransaction().remove(this@WebFragment).commit()
            it.popBackStack()
        }
    }

    companion object {
        fun newInstance() = WebFragment()
    }
}