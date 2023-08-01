package com.looktab.echonupy.feature

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.looktab.echonupy.base.BaseViewModel
import com.looktab.echonupy.data.source.nft.NftDataSource
import com.looktab.echonupy.feature.campain.model.Campaign
import com.looktab.echonupy.feature.login.PhantomDeepLink
import com.looktab.echonupy.feature.login.PhantomResponse
import com.looktab.echonupy.feature.login.Result
import com.looktab.echonupy.feature.progressroom.model.Chat
import com.solana.Solana
import com.solana.networking.HttpNetworkingRouter
import com.solana.networking.RPCEndpoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(
    val nftService: NftDataSource
) : BaseViewModel() {

    // Main ==================================================================================
    enum class ViewFlow { Main, NftCampaign, NftDetail, Login, PlantLft, Chat, UpPoint }

    private val _flow = MutableLiveData<ViewFlow>()
    val flow: LiveData<ViewFlow> = _flow

    fun setViewFlow(state: ViewFlow) {
        _flow.value = state
    }

    var latitude = 0.0
    var longitude = 0.0

    fun setLoc(lat: Double?, lng: Double?) {
        latitude = lat ?: 0.0
        longitude = lng ?: 0.0
    }

    // Login ==================================================================================

    val ownerPublicKey = MutableLiveData<String>(null)
    fun handleUrl(context: FragmentActivity, phantom: PhantomDeepLink, uri: Uri) {
        when (val phantomConnectResponse = phantom.parserHandleURL(uri)) {
            is Result.Success -> {
                when (val phantomResponse = phantomConnectResponse.value) {
                    is PhantomResponse.OnConnect -> {
                        Log.e("PHANTOMCONNECTIONERROR", phantomResponse.response.toString())

                        phantom.storeSession(
                            context,
                            phantomResponse.response.public_key,
                            phantomResponse.response.session,
                            phantomResponse.phantomEncryptionPublicKey,
                            phantomResponse.sharedSecretDapp
                        )
                        ownerPublicKey.value = phantomResponse.response.public_key
                    }
                }
            }

            is Result.Failure -> {
                Log.e("PHANTOMCONNECTIONERROR", phantomConnectResponse.reason.toString())
                Toast.makeText(context, "Cannot connect to Phantom!", Toast.LENGTH_LONG).show()
            }
        }
    }


    // NftPage ==================================================================================


    private val _nftList = MutableLiveData<List<Campaign>>()
    val nftList: LiveData<List<Campaign>> get() = _nftList

    val endPoint = RPCEndpoint.devnetSolana
    val network = HttpNetworkingRouter(endPoint)
    val solana = Solana(network)

    fun getNft() = CoroutineScope(Dispatchers.IO).launch {
        nftService.getCampaigns()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ it ->
                _nftList.postValue(it)
                Log.e("updateNft", "SUCCESS")

            }, { e ->
                Log.e("updateNft ERROR", e.toString())
            })
    }


    private val _detailNFT = MutableLiveData<Campaign>()
    val detailNFT: LiveData<Campaign> get() = _detailNFT
    fun selectNft(nft: Campaign) {
        _detailNFT.value = nft
        setViewFlow(ViewFlow.NftDetail)
    }

    private val _progressChat = MutableLiveData<List<Chat>>()
    val progressChat: LiveData<List<Chat>> get() = _progressChat

    fun addChat() {
        var list = (_progressChat.value ?: listOf()) +
            Chat(
                id = 0,
                title = "Healthy Carbon",
                icon = "https://bafybeid2kra6koy6s6qi7dlwy6ptsug7ayc725fedgilro2oo4lravtfpu.ipfs.w3s.link/seed_1.png",
                description = "10,000보를 걸었습니다! 100 CO2 배출권을 적립해드렸어요.",
                timeline = "9:54 PM",
            )
        list = list.sortedBy { it.id }
        _progressChat.value = list
    }

    fun updateChat() {
        setViewFlow(ViewFlow.UpPoint)
        _progressChat.value = _progressChat.value?.map {
            if (it.id == 0) {
                Chat(
                    id = 0,
                    title = "Healthy Carbon",
                    icon = "https://raw.githubusercontent.com/Looktab-naer/imgs/main/pp/seed_1.png",
                    description = "1 모바일 영수증 캡쳐 사진 인증 - 10 리워드: 유저가 앱에서 지원하는 가게에서 물건을 구매하고, 구매한 " +
                        "상품의 영수증을 앱에 업로드하여 사진 인증을 할 수 있습니다. 인증된 영수증에 따라 10 포인트가 즉시 지급됩니다. 이렇게 하면 영수증을 이용하여 소비내역을 확인하고 보상으로 ",
                    timeline = "9:56 PM"
                )
            } else it
        }
    }

    fun initProgressChat() {
        _progressChat.value = listOf(

            Chat(
                id = 1,
                title = "Mega Mutant Serum Event",
                icon = "https://raw.githubusercontent.com/Looktab-naer/imgs/main/pp/peat3.png",
                description = "Hello At Mega Mutant serum Event",
                timeline = "12:54 AM",
            ),
            Chat(
                id = 2,
                title = "InstaStar",
                icon = "https://raw.githubusercontent.com/Looktab-naer/imgs/main/pp/peat2.png",
                description = "In order to inform you of the benefits of community membership, we are sending you DM.",
                timeline = "2:00 PM",
            ),
            Chat(
                id = 3,
                title = "CryptoPunk Event",
                icon = "https://raw.githubusercontent.com/Looktab-naer/imgs/main/pp/peat1.png",
                description = "2023 Iddenver visitors' NFT airdrops can be obtained by sharing their location.",
                timeline = "11:34 AM",
            ),
            Chat(
                id = 4,
                title = "Doge Event",
                icon = "https://raw.githubusercontent.com/Looktab-naer/imgs/main/pp/peat4.png",
                description = "Want a cute NFT? That's Doge",
                timeline = "12:54 AM",
            ),
        )
    }
}