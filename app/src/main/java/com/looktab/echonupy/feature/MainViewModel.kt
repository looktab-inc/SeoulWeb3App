package com.looktab.echonupy.feature

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.looktab.echonupy.base.BaseViewModel
import com.looktab.echonupy.feature.campain.model.NftRow
import com.looktab.echonupy.feature.login.PhantomDeepLink
import com.looktab.echonupy.feature.login.PhantomResponse
import com.looktab.echonupy.feature.login.Result
import com.metaplex.lib.Metaplex
import com.metaplex.lib.drivers.indenty.ReadOnlyIdentityDriver
import com.metaplex.lib.drivers.storage.OkHttpSharedStorageDriver
import com.metaplex.lib.modules.nfts.models.NFT
import com.metaplex.lib.modules.nfts.models.Value
import com.metaplex.lib.solana.SolanaConnectionDriver
import com.solana.Solana
import com.solana.api.getBalance
import com.solana.core.PublicKey
import com.solana.networking.HttpNetworkingRouter
import com.solana.networking.RPCEndpoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel : BaseViewModel() {

    // Main ==================================================================================
    enum class ViewFlow { Main, NFT_Campain, NftDetail, Login }

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

            else -> {}
        }
    }


    // NftPage ==================================================================================

    val dataList = MutableLiveData<List<NFT>>()

    private val _nftList = MutableLiveData<List<NftRow>>()
    val nftList: LiveData<List<NftRow>> get() = _nftList

    val endPoint = RPCEndpoint.devnetSolana
    val network = HttpNetworkingRouter(endPoint)
    val solana = Solana(network)

    var metaplex: Metaplex? = null

    fun getNft() = CoroutineScope(Dispatchers.IO).launch {
        val ownerPublicKey = PublicKey(ownerPublicKey.value ?: "")
        val solanaConnection = SolanaConnectionDriver(endPoint)
        val solanaIdentityDriver = ReadOnlyIdentityDriver(ownerPublicKey, solanaConnection)
        val storageDriver = OkHttpSharedStorageDriver()
        metaplex = Metaplex(solanaConnection, solanaIdentityDriver, storageDriver)

        metaplex?.nft?.findAllByOwner(ownerPublicKey)
            ?.onSuccess { nfts ->
                dataList.postValue(nfts.filterNotNull())
                Log.e("metaplex.nft.findAllByOwner", "onSuccess")
            }
            ?.onFailure {
                Log.e("metaplex.nft onFailure)", it.toString())
            }
    }

    fun setNft() {
        _nftList.postValue(listOf())
        dataList.value?.forEach { nft ->
            nft.metadata(metaplex!!) { result ->
                result.onSuccess { res ->
                    var status = ""
                    var lat = 0.0
                    var lng = 0.0
                    var store_address = ""
                    var discount_type = ""
                    var discount_rate = 0.0
                    var discount_amount = 0.0
                    var started_at = ""
                    var ended_at = ""
                    res.attributes?.forEach {
                        when (it.value) {
                            is Value.number -> {
                                val num = (it.value as Value.number).value
                                when (it.trait_type) {
                                    "store_location_lat" -> {
                                        lat = num
                                    }

                                    "store_location_lng" -> {
                                        lng = num
                                    }

                                    "discount_rate" -> {
                                        discount_rate = num
                                    }

                                    "discount_amount" -> {
                                        discount_amount = num
                                    }
                                }
                            }

                            is Value.string -> {
                                val str = (it.value as Value.string).value
                                when (it.trait_type) {
                                    "status" -> {
                                        status = str
                                    }

                                    "store_address" -> {
                                        store_address = str
                                    }

                                    "discount_type" -> {
                                        discount_type = str
                                    }

                                    "started_at" -> {
                                        started_at = str
                                    }

                                    "ended_at" -> {
                                        ended_at = str
                                    }
                                }
                            }

                            is Value.unkown -> println("Unknown value")
                            null -> println("Value is null")
                        }
                    }
                }
            }
        }

    }

    private val _detailNFT = MutableLiveData<NftRow>()
    val detailNFT: LiveData<NftRow> get() = _detailNFT
    fun selectNft(nft: NftRow) {
        _detailNFT.value = nft
        setViewFlow(ViewFlow.NftDetail)
    }


    fun getSolana() {
        CoroutineScope(Dispatchers.IO).launch {
            val balance = solana.api.getBalance(PublicKey(ownerPublicKey.value ?: "")).getOrThrow()
            Log.e("balance", balance.toString())
        }
    }

}