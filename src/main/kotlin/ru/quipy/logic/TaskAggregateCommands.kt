package ru.quipy.logic

import ru.quipy.api.*
import java.util.*

fun TaskAggregateState.createTask(id: UUID, projectId: UUID, name: String, description: String, deadline: Date): TaskCreatedEvent {
    if (name.length > 255) {
        throw IllegalArgumentException("Task name should be less than 255 characters!")
    }

    return TaskCreatedEvent(id, projectId, name, description, deadline)
}

fun TaskAggregateState.updateTask(name: String, description: String, deadline: Date): TaskUpdatedEvent {
    return TaskUpdatedEvent(this.getId(), name, description, deadline)
}

fun TaskAggregateState.addExecutor(userId: UUID): ExecutorAddedEvent {
    if (executors.contains(userId)) {
        throw IllegalArgumentException("User is already assigned as an executor for this task.")
    }

    return ExecutorAddedEvent(this.getId(), userId)
}

fun TaskAggregateState.assignStatus(statusId: UUID): StatusAssignedToTaskEvent {
    return StatusAssignedToTaskEvent(this.getId(), statusId)
}

fun TaskAggregateState.removeStatus(): StatusRemovedFromTaskEvent {
    return StatusRemovedFromTaskEvent(this.getId())
}