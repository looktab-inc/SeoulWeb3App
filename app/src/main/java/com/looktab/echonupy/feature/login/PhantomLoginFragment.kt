package com.looktab.echonupy.feature.login

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.looktab.echonupy.R
import com.looktab.echonupy.base.BaseFragment
import com.looktab.echonupy.databinding.FragmentPhantomLoginBinding
import com.looktab.echonupy.feature.MainViewModel
import com.solana.networking.Network
import com.solana.vendor.TweetNaclFast
import org.bitcoinj.core.Base58
import java.net.URL


class PhantomLoginFragment :
    BaseFragment<FragmentPhantomLoginBinding>(R.layout.fragment_phantom_login) {
    //    private val viewModel by viewModels<PhantomLoginViewModel>()
    private val viewModel: MainViewModel by activityViewModels()

    private val keyPair by lazy {
        TweetNaclFast.Box.keyPair()
    }
    private val phantom by lazy {
        PhantomDeepLink(
            _urlSchema = "nupy",
            _appUrl = URL("https://seoul-web3-admin.vercel.app"),
            _publicKey = keyPair.publicKey,
            _secretKey = keyPair.secretKey,
            _cluster = Network.devnet
        )
    }

    companion object {
        const val TAG = "PhantomLoginFragment"
        const val SESSION_SHARED_PREFS_FILE = "com.looktab.echonupy.sessionStorage"
        const val PHANTOM_SHARED_PREFS_FILE = "com.looktab.echonupy.phantomStorage"
        const val DAPP_PUBLIC_KEY = "dappPublicKey"
        const val DAPP_SECRET_KEY = "dappSecretKey"
        const val OWNER_PUBLIC_KEY = "ownerPublicKey"
        const val SESSION = "sessionId"
        const val PHANTOM_ENCRYPTION_PUBLIC_KEY = "phantomEncryptionPublicKey"
        const val SHARED_SECRET_DAPP = "sharedSecretDapp"

        fun newInstance() = PhantomLoginFragment()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeAuthenticationState(view)
        isSessionAvailable()

        val uri = activity?.intent?.data
        if (uri != null) {
            val preferences = getSharedPrefs(PHANTOM_SHARED_PREFS_FILE)
            if (preferences != null) {
                val editor = preferences.edit()
                val dappPublicKey = preferences.getString(DAPP_PUBLIC_KEY, null)
                val dappSecretKey = preferences.getString(DAPP_SECRET_KEY, null)

                editor.clear()
                editor.apply()

                phantom.dappKeyPair.publicKey = Base58.decode(dappPublicKey)
                phantom.dappKeyPair.secretKey = Base58.decode(dappSecretKey)

                activity?.let { viewModel.handleUrl(it, phantom, uri) }
            }
        }

        binding.loginWithPhantomBtn.setOnClickListener {
            val urlString = phantom.getConnectURL()

            val preferences = getSharedPrefs(PHANTOM_SHARED_PREFS_FILE)
            if (preferences != null) {
                val editor = preferences.edit()
//                 E/publicKey: E1wEHF4iZpzd16FdnLt5zso5dWxt1gXj3rcXrhnwFt5o
//                  E/secretKey: E8EsduBnXZrXb69GzQ6SKX6ME32Jpib9wurevd5BcHYM
                Log.e("publicKey", Base58.encode(phantom.dappKeyPair.publicKey))
                Log.e("secretKey", Base58.encode(phantom.dappKeyPair.secretKey))

                editor.putString(DAPP_PUBLIC_KEY, Base58.encode(phantom.dappKeyPair.publicKey))
                editor.putString(DAPP_SECRET_KEY, Base58.encode(phantom.dappKeyPair.secretKey))
                editor.apply()

                launchURL(urlString, view)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)?.supportActionBar?.hide()
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity?)?.supportActionBar?.show()
    }

    private fun observeAuthenticationState(view: View) {
        viewModel.ownerPublicKey.observe(viewLifecycleOwner, Observer<String> { ownerPublicKey ->
            if (ownerPublicKey != null) {
//                showSnackbar(view, "Public Key: ${viewModel.ownerPublicKey.value.toString()}")
                val bundle = bundleOf("ownerPubKey" to ownerPublicKey.toString())

                Log.e("123123", "Public Key: ${viewModel.ownerPublicKey.value.toString()}")
                viewModel.setViewFlow(MainViewModel.ViewFlow.Main)
                closeFragment()
            } else {
                Log.i(TAG, "Unauthenticated")
            }
        })
    }

    private fun isSessionAvailable() {
        val preferences = getSharedPrefs(SESSION_SHARED_PREFS_FILE)
        if (preferences != null) {
            if (preferences.getString(
                    OWNER_PUBLIC_KEY,
                    null
                ) != null && viewModel.ownerPublicKey.value == null
            ) {
                viewModel.ownerPublicKey.value = preferences.getString(OWNER_PUBLIC_KEY, null)
            }
        }
    }

    private fun launchURL(url: String, view: View) {
        try {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        } catch (e: Error) {
            Log.e(TAG, e.toString())
            showSnackbar(view, "Cannot launch Phantom Deep Link!")
        }
    }

    private fun showSnackbar(contextView: View, message: String) {
        Snackbar.make(contextView, message, Snackbar.LENGTH_LONG)
            .show()
    }

    private fun getSharedPrefs(file: String): SharedPreferences? {
        return activity?.getSharedPreferences(file, MODE_PRIVATE)
    }

    private fun closeFragment() {
        val fragmentManager = activity?.supportFragmentManager
        fragmentManager?.let {
            it.beginTransaction().remove(this@PhantomLoginFragment).commit()
            it.popBackStack()
        }
    }

}