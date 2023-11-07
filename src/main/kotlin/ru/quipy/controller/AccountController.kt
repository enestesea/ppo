package ru.quipy.controller

import org.springframework.web.bind.annotation.*
import ru.quipy.api.*
import ru.quipy.core.EventSourcingService
import ru.quipy.logic.*
import java.util.*

@RestController
@RequestMapping("/accounts")
class AccountController(
    val accountEsService: EventSourcingService<UUID, AccountAggregate, AccountAggregateState>
) {
    @PostMapping("/")
    fun createAccount(): AccountCreatedEvent {
        return accountEsService.create { it.create(UUID.randomUUID()) }
    }

    @GetMapping("/{accountId}")
    fun getAccount(@PathVariable accountId: UUID): AccountAggregateState? {
        return accountEsService.getState(accountId)
    }

    @PostMapping("/{accountId}/bank-accounts")
    fun createBankAccount(@PathVariable accountId: UUID): BankAccountCreatedEvent {
        return accountEsService.update(accountId) { it.createBankAccount(UUID.randomUUID()) }
    }

    @PostMapping("/{accountId}/bank-accounts/{bankAccountId}/disable")
    fun disableBankAccount(@PathVariable accountId: UUID, @PathVariable bankAccountId: UUID): BankAccountDisabledEvent {
        return accountEsService.update(accountId) { it.disable(bankAccountId) }
    }

    @PostMapping("/{accountId}/bank-accounts/{bankAccountId}/withdraw")
    fun withdrawBankAccount(@PathVariable accountId: UUID, @PathVariable bankAccountId: UUID, @RequestBody body: WithdrawalRequest): BankAccountMoneyWithdrawnEvent {
        return accountEsService.update(accountId) { it.withdraw(bankAccountId, body.amount) }
    }

    @PostMapping("/{accountId}/bank-accounts/{bankAccountId}/enroll")
    fun withdrawBankAccount(@PathVariable accountId: UUID, @PathVariable bankAccountId: UUID, @RequestBody body: EnrollRequest): BankAccountMoneyEnrolledEvent {
        return accountEsService.update(accountId) { it.enroll(bankAccountId, body.amount) }
    }

    @PostMapping("/{accountId}/bank-accounts/transfer")
    fun transferBankAccount(@PathVariable accountId: UUID, @RequestBody body: TransferRequest): BankAccountMoneyTransferredEvent {
        return accountEsService.update(accountId) { it.transferBetweenInternal(body.senderId, body.receiverId, body.amount) }
    }
}


data class WithdrawalRequest(val amount: Double)
data class EnrollRequest(val amount: Double)
data class TransferRequest(val senderId: UUID, val receiverId: UUID, val amount: Double)