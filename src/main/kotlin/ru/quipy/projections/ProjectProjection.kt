package ru.quipy.projections

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository
import ru.quipy.api.*
import ru.quipy.streams.AggregateSubscriptionsManager
import javax.annotation.PostConstruct
import java.util.*

@Component
class ProjectRelation(
    private val projectProjectionRepository: ProjectProjectionRepo
) {

    val logger: Logger = LoggerFactory.getLogger(ProjectEventsSubscriber::class.java)

    @Autowired
    lateinit var subscriptionsManager: AggregateSubscriptionsManager

    @PostConstruct
    fun init() {
        subscriptionsManager.createSubscriber(ProjectAggregate::class, "ProjectAggregateSubscriberProjection") {
            `when`(ProjectCreatedEvent::class) { event ->
                val participants = mutableListOf<UUID>()
                val tasks = mutableListOf<UUID>()
                val statuses = mutableListOf<ProjectStatus>()
                participants.add(event.creatorId)
                projectProjectionRepository.save(
                    ProjectProjection(
                        event.projectId,
                        event.projectName,
                        event.creatorId,
                        event.description,
                        participants,
                        tasks,
                        statuses
                    )
                )
            }
        }
    }

    @PostConstruct
    fun addParticipant() {
        subscriptionsManager.createSubscriber(ProjectAggregate::class, "ProjectAggregateSubscriberProjection") {
            `when`(ParticipantAddedEvent::class) { event ->
                val projectProjection = projectProjectionRepository.findById(event.projectId).get()
                if (!projectProjection.participants.contains(event.userId)) {
                    projectProjection.participants.add(event.userId)
                    projectProjectionRepository.save(projectProjection)
                }
            }
        }
    }

    @PostConstruct
    fun delParticipant() {
        subscriptionsManager.createSubscriber(ProjectAggregate::class, "ProjectAggregateSubscriberProjection") {
            `when`(LeaveProjectEvent::class) { event ->
                val projectProjection = projectProjectionRepository.findById(event.projectId).get()
                if (projectProjection.participants.contains(event.userId)) {
                    projectProjection.participants.remove(event.userId)
                    projectProjectionRepository.save(projectProjection)
                }
            }
        }
    }

    @PostConstruct
    fun addStatus() {
        subscriptionsManager.createSubscriber(ProjectAggregate::class, "ProjectAggregateSubscriberProjection") {
            `when`(StatusCreatedEvent::class) { event ->
                val projectProjection = projectProjectionRepository.findById(event.projectId).get()
                val status = ProjectStatus(id = event.statusId, name = event.name, color = event.colour)
                projectProjection.statuses.add(status)
                projectProjectionRepository.save(projectProjection)
            }
        }
    }

    @PostConstruct
    fun delStatus() {
        subscriptionsManager.createSubscriber(ProjectAggregate::class, "ProjectAggregateSubscriberProjection") {
            `when`(StatusDeletedEvent::class) { event ->
                val projectProjection = projectProjectionRepository.findById(event.projectId).get()
                projectProjection.statuses.removeIf { it.id == event.statusId }
                projectProjectionRepository.save(projectProjection)
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
    val participants: MutableList<UUID>,
    val tasks: MutableList<UUID>,
    val statuses: MutableList<ProjectStatus>,
)


data class ProjectStatus(
    val id: UUID,
    val name: String,
    val color: String,
)
@Repository
interface ProjectProjectionRepo : MongoRepository<ProjectProjection, UUID>