@file:OptIn(ExperimentalCoroutinesApi::class)

package com.metaplex.lib.modules.nfts.operations

import com.metaplex.lib.Metaplex
import com.metaplex.lib.MetaplexTestUtils
import com.metaplex.lib.modules.nfts.models.NFT
import com.metaplex.lib.readOnlyMainnetMetaplexInstance
import com.solana.core.PublicKey
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class FindNftByMintOnChainOperationHandlerTests {

    val metaplex: Metaplex get() = MetaplexTestUtils.readOnlyMainnetMetaplexInstance

    @Test
    fun testFindNftByMintOnChainOperation() = runTest {
        // given
        val expectedNFTName = "Aurorian #628"
        val expectedNftMintKey = "HG2gLyDxmYGUfNWnvf81bJQj38twnF2aQivpkxficJbn"
        val expectedMasterAccountType = 6

        // when
        val result: NFT? = FindNftByMintOnChainOperationHandler(metaplex)
            .handle(PublicKey(expectedNftMintKey)).getOrNull()

        // then
        Assert.assertNotNull(result) // safe to force unwrap after this check
        Assert.assertEquals(expectedNFTName, result!!.metadataAccount.data.name)
        Assert.assertEquals(expectedNftMintKey, result.metadataAccount.mint.toBase58())
        Assert.assertEquals(expectedMasterAccountType, result.masterEditionAccount?.type)
    }
}