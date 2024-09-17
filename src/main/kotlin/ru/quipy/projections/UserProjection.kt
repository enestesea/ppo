package ru.quipy.projections

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository
import ru.quipy.streams.AggregateSubscriptionsManager
import javax.annotation.PostConstruct
import ru.quipy.api.UserAggregate
import ru.quipy.api.UserCreatedEvent
import java.util.*

@Component
class UserRelation(
    private val userProjectionRepository: UserProjectionRepo
) {

    val logger: Logger = LoggerFactory.getLogger(ProjectEventsSubscriber::class.java)

    @Autowired
    lateinit var subscriptionsManager: AggregateSubscriptionsManager

    @PostConstruct
    fun init() {
        subscriptionsManager.createSubscriber(UserAggregate::class, "UserAggregateSubscriberProjection") {
            `when`(UserCreatedEvent::class) { event ->
                userProjectionRepository.save(UserProjection(event.userId, event.nickname, event.userName))
            }
        }
    }
}

@Document("user-projection")
data class UserProjection(
    @Id
    val userId: UUID,
    val nickname: String,
    val firstName: String,
)

@Repository
interface UserProjectionRepo : MongoRepository<UserProjection, UUID>