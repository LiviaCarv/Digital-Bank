package com.project.digitalbank.di

import com.project.digitalbank.data.repository.auth.AuthFirebaseDataSource
import com.project.digitalbank.data.repository.auth.AuthFirebaseDataSourceImpl
import com.project.digitalbank.data.repository.deposit.DepositDataSource
import com.project.digitalbank.data.repository.deposit.DepositDataSourceImpl
import com.project.digitalbank.data.repository.recharge.RechargeDataSource
import com.project.digitalbank.data.repository.recharge.RechargeDataSourceImpl
import com.project.digitalbank.data.repository.transaction.TransactionDataSource
import com.project.digitalbank.data.repository.transaction.TransactionDataSourceImpl
import com.project.digitalbank.data.repository.transfer.TransferDataSource
import com.project.digitalbank.data.repository.transfer.TransferDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class DomainModule {
    @Binds
    abstract fun bindsAuthDataSource(
        authFirebaseDataSourceImpl: AuthFirebaseDataSourceImpl
    ) : AuthFirebaseDataSource

    @Binds
    abstract fun bindsDepositDataSource(
        depositDataSourceImpl: DepositDataSourceImpl
    ) : DepositDataSource

    @Binds
    abstract fun bindsTransactionDataSource(
        transactionDataSourceImpl: TransactionDataSourceImpl
    ) : TransactionDataSource

    @Binds
    abstract fun bindsRechargeDataSource(
        rechargeDataSourceImpl: RechargeDataSourceImpl
    ) : RechargeDataSource

    @Binds
    abstract fun bindsTransferDataSource(
        transferDataSourceImpl: TransferDataSourceImpl
    ) : TransferDataSource
}