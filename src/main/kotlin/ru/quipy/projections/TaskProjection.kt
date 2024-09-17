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
class TaskRelation(
    private val taskProjectionRepository: TaskProjectionRepo,
    private val projectProjectionRepository: ProjectRelation.ProjectProjectionRepo
) {

    val logger: Logger = LoggerFactory.getLogger(ProjectEventsSubscriber::class.java)

    @Autowired
    lateinit var subscriptionsManager: AggregateSubscriptionsManager

    @PostConstruct
    fun init() {
        subscriptionsManager.createSubscriber(TaskAggregate::class, "TaskAggregateSubscriberProjection") {
            `when`(TaskCreatedEvent::class) { event ->
                val executors = mutableListOf<UUID>()
                taskProjectionRepository.save(TaskProjection(event.taskId, event.taskName, event.description, event.deadline, executors))

                val projectProjection = projectProjectionRepository.findById(event.projectId).get()
                projectProjection.tasks.add(event.taskId)
                projectProjectionRepository.save(projectProjection)
            }
        }
    }

    @PostConstruct
    fun update() {
        subscriptionsManager.createSubscriber(TaskAggregate::class, "TaskAggregateSubscriberProjection") {
            `when`(TaskUpdatedEvent::class) { event ->
                val taskProjection = taskProjectionRepository.findById(event.taskId).get()
                taskProjection.name = event.taskName
                taskProjection.description = event.description
                taskProjection.deadline = event.deadline
                taskProjectionRepository.save(taskProjection)
            }
        }
    }

    @PostConstruct
    fun addExecutor() {
        subscriptionsManager.createSubscriber(TaskAggregate::class, "TaskAggregateSubscriberProjection") {
            `when`(ExecutorAddedEvent::class) { event ->
                val taskProjection = taskProjectionRepository.findById(event.taskId).get()
                taskProjection.executors.add(event.userId)
                taskProjectionRepository.save(taskProjection)
            }
        }
    }
}


@Document("status-projection")
data class TaskProjection(
    @Id
    val taskId: UUID,
    var name: String,
    var description: String,
    var deadline: Date,
    var executors: MutableList<UUID>,
)

@Repository
interface TaskProjectionRepo : MongoRepository<TaskProjection, UUID>