package com.project.digitalbank.di

import com.project.digitalbank.data.repository.auth.AuthFirebaseDataSource
import com.project.digitalbank.data.repository.auth.AuthFirebaseDataSourceImpl
import com.project.digitalbank.data.repository.deposit.DepositDataSource
import com.project.digitalbank.data.repository.deposit.DepositDataSourceImpl
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
}