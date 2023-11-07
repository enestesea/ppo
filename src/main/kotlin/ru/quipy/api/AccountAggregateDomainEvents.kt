package ru.quipy.api

import ru.quipy.core.annotations.DomainEvent
import ru.quipy.domain.Event
import ru.quipy.logic.BankAccountStatus
import java.util.*

const val ACCOUNT_CREATED_EVENT = "ACCOUNT_CREATED_EVENT"
const val ACCOUNT_DISABLED_EVENT = "ACCOUNT_DISABLED_EVENT"
const val ACCOUNT_MONEY_TRANSFERRED_EVENT = "ACCOUNT_MONEY_TRANSFERRED_EVENT"
const val ACCOUNT_MONEY_WITHDRAWN_EVENT = "ACCOUNT_MONEY_WITHDRAWN_EVENT"
const val ACCOUNT_MONEY_ENROLLED_EVENT = "ACCOUNT_MONEY_ENROLLED_EVENT"
const val BANK_ACCOUNT_CREATED_EVENT = "BANK_ACCOUNT_CREATED_EVENT"

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

@DomainEvent(name = ACCOUNT_DISABLED_EVENT)
class AccountDisabledEvent(
    val bankAccountId : UUID,
    val accountId: UUID,
    createdAt: Long = System.currentTimeMillis(),
) : Event<AccountAggregate>(
    name = ACCOUNT_DISABLED_EVENT,
    createdAt = createdAt,
)



@DomainEvent(name = ACCOUNT_MONEY_TRANSFERRED_EVENT)
class AccountMoneyTransferredEvent(
    val accountId : UUID,
    val senderId: UUID,
    val receiverId: UUID,
    val amount: Double,
    createdAt: Long = System.currentTimeMillis(),
) : Event<AccountAggregate>(
    name = ACCOUNT_MONEY_TRANSFERRED_EVENT,
    createdAt = createdAt,
)

@DomainEvent(name = ACCOUNT_MONEY_WITHDRAWN_EVENT)
class AccountMoneyWithdrawnEvent(
    val bankAccountId : UUID,
    val accountId: UUID,
    val amount: Double,
    createdAt: Long = System.currentTimeMillis(),
) : Event<AccountAggregate>(
    name = ACCOUNT_MONEY_WITHDRAWN_EVENT,
    createdAt = createdAt,
)
@DomainEvent(name = ACCOUNT_MONEY_ENROLLED_EVENT)
class AccountMoneyEnrolledEvent(
    val bankAccountId : UUID,
    val accountId: UUID,
    val amount: Double,
    createdAt: Long = System.currentTimeMillis(),
) : Event<AccountAggregate>(
    name = ACCOUNT_MONEY_ENROLLED_EVENT,
    createdAt = createdAt,
)