package ru.quipy.api

import ru.quipy.core.annotations.DomainEvent
import ru.quipy.domain.Event
import ru.quipy.logic.BankAccountStatus
import java.util.*

const val ACCOUNT_CREATED_EVENT = "ACCOUNT_CREATED_EVENT"
const val BANK_ACCOUNT_CREATED_EVENT = "BANK_ACCOUNT_CREATED_EVENT"
const val BANK_ACCOUNT_DISABLED_EVENT = "BANK_ACCOUNT_DISABLED_EVENT"
const val BANK_ACCOUNT_MONEY_TRANSFERRED_EVENT = "BANK_ACCOUNT_MONEY_TRANSFERRED_EVENT"
const val BANK_ACCOUNT_MONEY_WITHDRAWN_EVENT = "BANK_ACCOUNT_MONEY_WITHDRAWN_EVENT"
const val BANK_ACCOUNT_MONEY_ENROLLED_EVENT = "BANK_ACCOUNT_MONEY_ENROLLED_EVENT"

// API
@DomainEvent(name = ACCOUNT_CREATED_EVENT)
class AccountCreatedEvent(
    val accountId: UUID,
    createdAt: Long = System.currentTimeMillis(),
) : Event<AccountAggregate>(
    name = ACCOUNT_CREATED_EVENT,
    createdAt = createdAt,
)

@DomainEvent(name = BANK_ACCOUNT_CREATED_EVENT)
class BankAccountCreatedEvent(
    val bankAccountId : UUID,
    val accountId: UUID,
    createdAt: Long = System.currentTimeMillis(),
) : Event<AccountAggregate>(
    name = BANK_ACCOUNT_CREATED_EVENT,
    createdAt = createdAt,
)

@DomainEvent(name = BANK_ACCOUNT_DISABLED_EVENT)
class BankAccountDisabledEvent(
    val bankAccountId : UUID,
    val accountId: UUID,
    createdAt: Long = System.currentTimeMillis(),
) : Event<AccountAggregate>(
    name = BANK_ACCOUNT_DISABLED_EVENT,
    createdAt = createdAt,
)



@DomainEvent(name = BANK_ACCOUNT_MONEY_TRANSFERRED_EVENT)
class BankAccountMoneyTransferredEvent(
    val accountId : UUID,
    val senderId: UUID,
    val receiverId: UUID,
    val amount: Double,
    createdAt: Long = System.currentTimeMillis(),
) : Event<AccountAggregate>(
    name = BANK_ACCOUNT_MONEY_TRANSFERRED_EVENT,
    createdAt = createdAt,
)

@DomainEvent(name = BANK_ACCOUNT_MONEY_WITHDRAWN_EVENT)
class BankAccountMoneyWithdrawnEvent(
    val bankAccountId : UUID,
    val accountId: UUID,
    val amount: Double,
    createdAt: Long = System.currentTimeMillis(),
) : Event<AccountAggregate>(
    name = BANK_ACCOUNT_MONEY_WITHDRAWN_EVENT,
    createdAt = createdAt,
)
@DomainEvent(name = BANK_ACCOUNT_MONEY_ENROLLED_EVENT)
class BankAccountMoneyEnrolledEvent(
    val bankAccountId : UUID,
    val accountId: UUID,
    val amount: Double,
    createdAt: Long = System.currentTimeMillis(),
) : Event<AccountAggregate>(
    name = BANK_ACCOUNT_MONEY_ENROLLED_EVENT,
    createdAt = createdAt,
)