package ru.quipy.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.quipy.api.UserAggregate
import ru.quipy.api.UserCreatedEvent
import ru.quipy.core.EventSourcingService
import ru.quipy.logic.UserAggregateState
import ru.quipy.logic.createUser
import java.util.*


@RestController
@RequestMapping("/users")
class UserController(
    val usersEsService: EventSourcingService<UUID, UserAggregate, UserAggregateState>
) {

    @PostMapping("")
    fun createUser(@RequestBody body: CreateUserRequest): UserCreatedEvent {
        return usersEsService.create { it.createUser(UUID.randomUUID(), body.userName, body.nickname, body.password) }
    }
}

data class CreateUserRequest (
    val userName: String,
    val nickname: String,
    val password: String
)