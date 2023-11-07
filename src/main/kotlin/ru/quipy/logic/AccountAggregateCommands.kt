package ru.quipy.logic

import ru.quipy.api.*
import java.util.*


// Commands : takes something -> returns event
// Here the commands are represented by extension functions, but also can be the class member functions

fun AccountAggregateState.create(): AccountCreatedEvent {
    return AccountCreatedEvent(
        accountId = this.getId()
    )
}

fun AccountAggregateState.createBankAccount(bankAccountId: UUID): BankAccountCreatedEvent {
    if (!this.checkBankAccountsCount()) {
        throw IllegalArgumentException("Bank accounts count limit!")
    }
    return BankAccountCreatedEvent(
        accountId = this.getId(),
        bankAccountId = bankAccountId
    )
}

fun AccountAggregateState.disable(bankAccountId: UUID): AccountDisabledEvent {
    if (!checkEmptyBankAccount(bankAccountId)) {
        throw IllegalArgumentException("Bank Account is not empty!")
    }
    return AccountDisabledEvent(accountId = this.getId(), bankAccountId = bankAccountId)
}

fun AccountAggregateState.withdraw(bankAccountId: UUID, amount: Double): AccountMoneyWithdrawnEvent {
    if (!this.checkEnoughFunds(bankAccountId, amount)) {
        throw IllegalArgumentException("Not enough funds!")
    }
    return AccountMoneyWithdrawnEvent(accountId = this.getId(), bankAccountId = bankAccountId, amount = amount)
}

fun AccountAggregateState.enroll(bankAccountId: UUID, amount: Double): AccountMoneyEnrolledEvent {
    if (!this.checkAccountBalanceLimits(bankAccountId, amount)) {
        throw IllegalArgumentException("Funds limit!")
    }
    return AccountMoneyEnrolledEvent(accountId = this.getId(), bankAccountId = bankAccountId, amount = amount)
}

fun AccountAggregateState.transferBetweenInternal(senderId: UUID,
                                                  receiverId: UUID, amount: Double): AccountMoneyTransferredEvent {
    if (!this.checkEnoughFunds(senderId, amount)) {
        throw IllegalArgumentException("Not enough funds!")
    }
    if (!this.checkBankAccountBalanceLimits(receiverId, amount)) {
        throw IllegalArgumentException("Funds limit!")
    }
    return AccountMoneyTransferredEvent(accountId = this.getId(), senderId = senderId,
        receiverId = receiverId, amount = amount)
}
