package com.looktab.echonupy.feature.progressroom

import android.content.Context
import androidx.fragment.app.activityViewModels
import com.looktab.echonupy.R
import com.looktab.echonupy.base.BaseFragment
import com.looktab.echonupy.databinding.FragmentProgressBinding
import com.looktab.echonupy.feature.MainViewModel
import com.looktab.echonupy.feature.progressroom.adapter.ChatRoomAdapter

class ProgressFragment : BaseFragment<FragmentProgressBinding>(
    R.layout.fragment_progress
) {

    private val activityViewModel: MainViewModel by activityViewModels()
    lateinit var mContext: Context

    private val chatRoomAdapter by lazy {
        ChatRoomAdapter().apply {
            setOnItemClickListener(object : ChatRoomAdapter.OnItemClickListener {
                override fun onItemClick(id: String) {
                    activityViewModel.setViewFlow(MainViewModel.ViewFlow.Chat)
                }
            })
        }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun initViews() {
    }

    override fun initRecyclerView() {
        binding.rvChat.apply {
            adapter = chatRoomAdapter
            itemAnimator = null
        }
        activityViewModel.initProgressChat()
    }

    override fun initObserves() {
        activityViewModel.progressChat.observe(this) {
            chatRoomAdapter.submitList(it)
        }
    }


    companion object {
        fun newInstance() = ProgressFragment()
    }
}