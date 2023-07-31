package com.metaplex.lib.modules.token.operations

import com.metaplex.lib.Metaplex
import com.metaplex.lib.drivers.solana.Connection
import com.metaplex.lib.modules.token.models.FungibleToken
import com.metaplex.lib.programs.token_metadata.accounts.MetadataAccount
import com.metaplex.lib.shared.OperationError
import com.metaplex.lib.shared.OperationHandler
import com.solana.core.PublicKey
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FindFungibleTokenByMintOnChainOperationHandler(override val connection: Connection,
                                                     override val dispatcher: CoroutineDispatcher = Dispatchers.IO)
    : OperationHandler<PublicKey, FungibleToken> {

    constructor(metaplex: Metaplex) : this(metaplex.connection)

    override suspend fun handle(input: PublicKey): Result<FungibleToken> = withContext(dispatcher) {
        FindTokenMetadataAccountOperation(connection).run(MetadataAccount.pda(input).getOrThrows())
            .mapCatching {
                it.data?.let { FungibleToken(it) } ?: throw OperationError.NilDataOnAccount
            }
    }
}