package com.looktab.echonupy.feature.campain

import android.content.Context
import androidx.fragment.app.activityViewModels
import com.looktab.echonupy.R
import com.looktab.echonupy.base.BaseFragment
import com.looktab.echonupy.databinding.FragmentNftBinding
import com.looktab.echonupy.feature.MainViewModel
import com.looktab.echonupy.feature.campain.adapter.NftAdapter
import com.looktab.echonupy.feature.campain.model.Campaign


class NftFragment : BaseFragment<FragmentNftBinding>(R.layout.fragment_nft) {

    private val activityViewModel: MainViewModel by activityViewModels()
    lateinit var mContext: Context

    private val nftAdapter by lazy {
        NftAdapter().apply {
            setOnItemClickListener(object : NftAdapter.OnItemClickListener {
                override fun onItemClick(nft: Campaign) {
                    activityViewModel.selectNft(nft)
                }
            })
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun initViews() {
        binding.viewModel = activityViewModel
        activityViewModel.getNft()
        binding.btnBack.setOnClickListener {
            closeFragment()
        }
    }


    override fun initRecyclerView() {
        binding.nftsRecyclerView.adapter = nftAdapter
        activityViewModel.getNft()
    }

    override fun initObserves() {
        activityViewModel.nftList.observe(this) {
            nftAdapter.submitList(it)
        }
    }

    private fun closeFragment() {
        val fragmentManager = activity?.supportFragmentManager
        fragmentManager?.let {
            activityViewModel.addChat()
            it.beginTransaction().remove(this@NftFragment).commit()
            it.popBackStack()
        }
    }

    companion object {
        fun newInstance() = NftFragment()
    }
}