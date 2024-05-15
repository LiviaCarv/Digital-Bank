package com.project.digitalbank.ui.features.transfer.receipt

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.project.digitalbank.data.model.Transfer
import com.project.digitalbank.domain.transfer.SaveTransferUseCase
import com.project.digitalbank.util.StateView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class TransferReceiptViewModel @Inject constructor(
) : ViewModel(){
}