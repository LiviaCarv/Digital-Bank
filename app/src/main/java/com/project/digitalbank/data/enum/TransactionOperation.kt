package com.project.digitalbank.data.enum

enum class TransactionOperation {
    DEPOSIT;

    companion object {
        fun getType(operation: TransactionOperation) : Char {
            return when(operation) {
                DEPOSIT -> 'D'
            }
        }
    }
}