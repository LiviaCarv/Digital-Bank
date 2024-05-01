package com.project.digitalbank.data.repository.wallet

import com.project.digitalbank.data.model.Wallet

interface WalletDataSource {
    suspend fun initWallet(wallet: Wallet)
}