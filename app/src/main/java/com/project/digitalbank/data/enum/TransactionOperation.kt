package com.project.digitalbank.data.enum

enum class TransactionOperation {
    DEPOSIT, RECHARGE, TRANSFER;

    companion object {
        fun getType(operation: TransactionOperation) : Char {
            return when(operation) {
                DEPOSIT -> 'D'
                RECHARGE -> 'R'
                TRANSFER -> 'T'
            }
        }
    }
}