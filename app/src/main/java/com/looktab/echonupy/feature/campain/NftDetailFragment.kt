package com.looktab.echonupy.feature.campain

import android.content.Context
import androidx.fragment.app.activityViewModels
import com.looktab.echonupy.R
import com.looktab.echonupy.base.BaseFragment
import com.looktab.echonupy.databinding.FragmentNftDetailsBinding
import com.looktab.echonupy.feature.MainViewModel

class NftDetailFragment : BaseFragment<FragmentNftDetailsBinding>(
    R.layout.fragment_nft_details
) {

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
        binding.btnComplete.setOnClickListener {
            closeFragment()
        }
    }

    override fun initObserves() {
    }

    private fun closeFragment() {
        val fragmentManager = activity?.supportFragmentManager
        fragmentManager?.let {
            it.beginTransaction().remove(this@NftDetailFragment).commit()
            it.popBackStack()
        }
    }

    companion object {
        fun newInstance() = NftDetailFragment()
    }
}