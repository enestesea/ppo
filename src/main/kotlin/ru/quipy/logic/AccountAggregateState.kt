package ru.quipy.logic

import ru.quipy.api.*
import ru.quipy.core.annotations.StateTransitionFunc
import ru.quipy.domain.AggregateState
import java.util.*

// Service's business logic
class AccountAggregateState : AggregateState<UUID, AccountAggregate> {
    private lateinit var accountId: UUID
    val bankAccounts = mutableMapOf<UUID, BankAccount>()
    val countLimit = 5
    val bankAccountBalanceLimit = 10000000
    val accountBalanceLimit = 25000000

    override fun getId() = accountId

    fun checkBankAccountsCount() : Boolean {
        return bankAccounts.count { it.value.status == BankAccountStatus.Open } < countLimit
    }
    fun checkAccountBalanceLimits(bankAccountId:UUID, amount: Double) : Boolean {
        return (bankAccounts[bankAccountId]?.balance!! + amount) <= bankAccountBalanceLimit
                && (bankAccounts.values.sumOf { it.balance } + amount) <= accountBalanceLimit
    }
    fun checkBankAccountBalanceLimits(bankAccountId:UUID, amount: Double) : Boolean {
        return (bankAccounts[bankAccountId]?.balance!! + amount) <= bankAccountBalanceLimit
    }

    fun checkEnoughFunds(bankAccountId:UUID, amount: Double) : Boolean{
        return bankAccounts[bankAccountId]?.balance!! >= amount
    }

    fun checkEmptyBankAccount(bankAccountId: UUID) : Boolean {
        return bankAccounts[bankAccountId]?.balance!! == 0.0
    }

    // State transition functions which is represented by the class member function
    @StateTransitionFunc
    fun accountCreatedApply(event: AccountCreatedEvent) {
        accountId = event.accountId
    }
    @StateTransitionFunc
    fun bankAccountCreatedApply(event: BankAccountCreatedEvent) {
        bankAccounts[event.bankAccountId] = BankAccount(event.bankAccountId,
        BankAccountStatus.Open, 0.0)
    }

    @StateTransitionFunc
    fun accountDisabledApply(event: AccountDisabledEvent) {
        bankAccounts[event.bankAccountId]?.status = BankAccountStatus.Closed
    }

    @StateTransitionFunc
    fun accountMoneyWithdrawnApply(event: AccountMoneyWithdrawnEvent) {
        bankAccounts[event.bankAccountId]?.balance?.minus(event.amount)
    }

    @StateTransitionFunc
    fun accountMoneyEnrolledApply(event: AccountMoneyEnrolledEvent) {
        bankAccounts[event.bankAccountId]?.balance?.plus(event.amount)
    }
    @StateTransitionFunc
    fun accountMoneyTransferredApply(event: AccountMoneyTransferredEvent) {
        bankAccounts[event.senderId]?.balance?.minus(event.amount)
        bankAccounts[event.receiverId]?.balance?.plus(event.amount)
    }
}

data class BankAccount(
    val id: UUID,
    var status: BankAccountStatus,
    var balance: Double
)