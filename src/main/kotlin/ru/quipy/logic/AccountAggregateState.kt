package ru.quipy.logic

import ru.quipy.api.*
import ru.quipy.core.annotations.StateTransitionFunc
import ru.quipy.domain.AggregateState
import java.util.*

// Service's business logic
class AccountAggregateState : AggregateState<UUID, AccountAggregate> {
    private lateinit var accountId: UUID
    val bankAccounts = mutableMapOf<UUID, BankAccount>()
    private val bankAccountsCountLimit = 5
    private val bankAccountBalanceLimit = 10_000_000
    private val accountBalanceLimit = 25_000_000

    override fun getId() = accountId

    fun checkBankAccountsCount() : Boolean {
        return bankAccounts.count { it.value.status == BankAccountStatus.Open } < bankAccountsCountLimit
    }
    fun checkEnrollAccountBalanceLimits(bankAccountId: UUID, amount: Double) : Boolean {
        return checkBankAccountBalanceLimit(bankAccountId, amount) && checkAccountBalanceLimit(amount)
    }
    fun checkBankAccountBalanceLimit(bankAccountId:UUID, amount: Double) : Boolean {
        return bankAccounts[bankAccountId]?.balance!! + amount <= bankAccountBalanceLimit
    }

    fun checkAccountBalanceLimit(amount: Double) : Boolean {
        return bankAccounts.values.sumOf { it.balance } + amount <= accountBalanceLimit
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
        bankAccounts[event.bankAccountId] = BankAccount(event.bankAccountId)
    }

    @StateTransitionFunc
    fun accountDisabledApply(event: BankAccountDisabledEvent) {
        bankAccounts[event.bankAccountId]?.status = BankAccountStatus.Closed
    }

    @StateTransitionFunc
    fun accountMoneyWithdrawnApply(event: BankAccountMoneyWithdrawnEvent) {
        bankAccounts[event.bankAccountId]?.balance = bankAccounts[event.bankAccountId]?.balance?.minus(event.amount)!!
    }

    @StateTransitionFunc
    fun accountMoneyEnrolledApply(event: BankAccountMoneyEnrolledEvent) {
        bankAccounts[event.bankAccountId]?.balance = bankAccounts[event.bankAccountId]?.balance?.plus(event.amount)!!
    }
    @StateTransitionFunc
    fun accountMoneyTransferredApply(event: BankAccountMoneyTransferredEvent) {
        bankAccounts[event.senderId]?.balance = bankAccounts[event.senderId]?.balance?.minus(event.amount)!!
        bankAccounts[event.receiverId]?.balance = bankAccounts[event.receiverId]?.balance?.plus(event.amount)!!
    }
}

data class BankAccount(
    val id: UUID,
    var status: BankAccountStatus = BankAccountStatus.Open,
    var balance: Double = 0.0,
)