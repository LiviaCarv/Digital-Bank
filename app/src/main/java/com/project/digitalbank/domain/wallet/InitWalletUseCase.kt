package com.project.digitalbank.domain.wallet

import com.project.digitalbank.data.model.Wallet
import com.project.digitalbank.data.repository.wallet.WalletDataSourceImpl
import javax.inject.Inject

class InitWalletUseCase @Inject constructor(
    private val walletDataSourceImpl: WalletDataSourceImpl
) {
    suspend operator fun invoke(wallet: Wallet) {
        return walletDataSourceImpl.initWallet(wallet)
    }
}