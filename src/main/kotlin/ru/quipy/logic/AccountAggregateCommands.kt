package ru.quipy.logic

import ru.quipy.api.*
import java.util.*


// Commands : takes something -> returns event
// Here the commands are represented by extension functions, but also can be the class member functions

fun AccountAggregateState.create(accountId: UUID): AccountCreatedEvent {
    return AccountCreatedEvent(accountId)
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

fun AccountAggregateState.disable(bankAccountId: UUID): BankAccountDisabledEvent {
    if (!checkEmptyBankAccount(bankAccountId)) {
        throw IllegalArgumentException("Bank Account is not empty!")
    }

    return BankAccountDisabledEvent(accountId = this.getId(), bankAccountId = bankAccountId)
}

fun AccountAggregateState.withdraw(bankAccountId: UUID, amount: Double): BankAccountMoneyWithdrawnEvent {
    if (!this.checkEnoughFunds(bankAccountId, amount)) {
        throw IllegalArgumentException("Not enough funds!")
    }

    return BankAccountMoneyWithdrawnEvent(accountId = this.getId(), bankAccountId = bankAccountId, amount = amount)
}

fun AccountAggregateState.enroll(bankAccountId: UUID, amount: Double): BankAccountMoneyEnrolledEvent {
    if (!this.checkEnrollAccountBalanceLimits(bankAccountId, amount)) {
        throw IllegalArgumentException("Funds limit!")
    }

    return BankAccountMoneyEnrolledEvent(accountId = this.getId(), bankAccountId = bankAccountId, amount = amount)
}

fun AccountAggregateState.transferBetweenInternal(
    senderId: UUID, receiverId: UUID, amount: Double
): BankAccountMoneyTransferredEvent {
    if (!this.checkEnoughFunds(senderId, amount)) {
        throw IllegalArgumentException("Not enough funds!")
    }

    if (!this.checkBankAccountBalanceLimit(receiverId, amount)) {
        throw IllegalArgumentException("Funds limit!")
    }

    return BankAccountMoneyTransferredEvent(
        accountId = this.getId(), senderId = senderId,
        receiverId = receiverId, amount = amount
    )
}
