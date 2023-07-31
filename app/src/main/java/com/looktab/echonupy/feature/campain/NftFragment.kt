package com.looktab.echonupy.feature.campain

import android.content.Context
import android.util.Log
import androidx.fragment.app.activityViewModels
import com.looktab.echonupy.R
import com.looktab.echonupy.base.BaseFragment
import com.looktab.echonupy.databinding.FragmentNftBinding
import com.looktab.echonupy.feature.MainViewModel
import com.looktab.echonupy.feature.campain.adapter.NftAdapter
import com.looktab.echonupy.feature.campain.model.NftRow
import com.solana.core.PublicKey
import com.solana.rxsolana.api.getConfirmedSignaturesForAddress2
import com.solana.rxsolana.api.getConfirmedTransaction
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class NftFragment : BaseFragment<FragmentNftBinding>(R.layout.fragment_nft) {

    private val activityViewModel: MainViewModel by activityViewModels()
    lateinit var mContext: Context

    private val nftAdapter by lazy {
        NftAdapter().apply {
            setOnItemClickListener(object : NftAdapter.OnItemClickListener {
                override fun onItemClick(nft: NftRow) {
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
        test()
    }

    fun test() {
        //getTransaction

// getConfirmedTransaction 호출

// getConfirmedTransaction 호출
//        val confirmedTransaction = activityViewModel.solana.api.getTransaction("5T7XbNMHTpaAz6vriJSny53S9iCgEZbhAtXpb4D6dAVhKHjFHyCgcrjxuUFudNrH2wbjToN4XifGqnZAVFthpPtd")


// 결과 출력

        // 결과 출력
//        System.out.println(
//            confirmedTransaction.blockingGet().transaction
//        )
        fun b() =
            activityViewModel.solana.api.getConfirmedTransaction("5T7XbNMHTpaAz6vriJSny53S9iCgEZbhAtXpb4D6dAVhKHjFHyCgcrjxuUFudNrH2wbjToN4XifGqnZAVFthpPtd")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Log.e("transaction transaction ", it.transaction.toString().toString())

                    it.slot
                    it.meta?.let {
                        Log.e("meta preBalances ", it.preBalances.toString())
                        Log.e("meta preTokenBalances ", it.preTokenBalances.toString())
                        Log.e("meta postBalances ", it.postBalances.toString())
                        Log.e("meta postTokenBalances ", it.postTokenBalances.toString())
                        Log.e("meta postTokenBalances ", it.postTokenBalances.toString())
                        Log.e("meta innerInstructions ", it.innerInstructions.toString())
                        Log.e("meta fee ", it.fee.toString())
                        Log.e("meta status ", it.status.toString())
                    }

                }, { e ->
                    Log.e("getConfirmedTransaction ERR ", e.toString())
                }
                )

        fun a() = activityViewModel.solana.api.getConfirmedSignaturesForAddress2(
            account = PublicKey(activityViewModel.ownerPublicKey.value ?: ""),
            limit = 10
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Log.e("getConfirmedSignaturesForAddress2 ", it.toString())
            }, { e ->
                Log.e("getConfirmedSignaturesForAddress2 ERR ", e.toString())
            }
            )
        a()
//        b()

    }

    override fun initRecyclerView() {
        binding.nftsRecyclerView.adapter = nftAdapter
//        activityViewModel.getNft()
    }

    override fun initObserves() {
        activityViewModel.dataList.observe(this) {
            activityViewModel.setNft()
        }
        activityViewModel.nftList.observe(this) {
            nftAdapter.submitList(it)
        }
    }

    companion object {
        fun newInstance() = NftFragment()
    }
}