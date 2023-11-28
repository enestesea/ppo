package ru.quipy.projections

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository
import ru.quipy.api.ProjectAggregate
import ru.quipy.api.ProjectCreatedEvent
import ru.quipy.streams.AggregateSubscriptionsManager
import javax.annotation.PostConstruct
import ru.quipy.api.UserAggregate
import ru.quipy.api.UserCreatedEvent
import java.util.*

@Component
class ProjecRelation(
    private val projectProjectionRepository: ProjectProjectionRepo
) {

    val logger: Logger = LoggerFactory.getLogger(ProjectEventsSubscriber::class.java)

    @Autowired
    lateinit var subscriptionsManager: AggregateSubscriptionsManager

    @PostConstruct
    fun init() {
        subscriptionsManager.createSubscriber(ProjectAggregate::class, "ProjectAggregateSubscriberProjection") {
            `when`(ProjectCreatedEvent::class) { event ->
                projectProjectionRepository.save(ProjectProjection(event.projectId, event.projectName, event.creatorId, event.description))
            }
        }
    }
}

@Document("project-projection")
data class ProjectProjection(
    @Id
    val projectID: UUID,
    val projectName: String,
    val creatorID: UUID,
    val description: String,
)

@Repository
interface ProjectProjectionRepo : MongoRepository<ProjectProjection, UUID>