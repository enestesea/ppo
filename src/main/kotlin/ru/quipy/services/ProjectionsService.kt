package ru.quipy.services

import org.springframework.stereotype.Service
import ru.quipy.projections.*
import java.util.*

@Service
class ProjectionsService constructor(val userProjectionRepo: UserProjectionRepo,
                                     val projectProjectionRepo: ProjectProjectionRepo,
                                     val taskProjectionRepo: TaskProjectionRepo,
                                    ){
    fun getAllUserProjection(): List<UserProjection> {
        return userProjectionRepo.findAll()
    }

    fun getProjectProjectionById(id: UUID): ProjectProjection {
        return projectProjectionRepo.findById(id).get()
    }

    fun getProjectTasks(id: UUID): List<TaskProjection> {
        val taskIds = projectProjectionRepo.findById(id).get().tasks
        val tasks = mutableListOf<TaskProjection>()
        for (taskId in taskIds) {
            val task = taskProjectionRepo.findById(taskId).get()
            tasks.add(task)
        }
        return tasks
    }

    fun getProjectParticipants(id: UUID): List<UserProjection> {
        val userIds = projectProjectionRepo.findById(id).get().participants
        val participants= mutableListOf<UserProjection>()
        for (userId in userIds) {
            val user = userProjectionRepo.findById(userId).get()
            participants.add(user)
        }
        return participants
    }

    fun getProjectStatuses(id: UUID): List<ProjectStatus> {
        return projectProjectionRepo.findById(id).get().statuses
    }

    fun getTaskById(id:UUID): TaskProjection {
        return taskProjectionRepo.findById(id).get()
    }

    fun getTaskExecutors(id:UUID): List<UserProjection> {
        val executorIds = taskProjectionRepo.findById(id).get().executors
        val executors = mutableListOf<UserProjection>()
        for (userId in executorIds) {
            val user = userProjectionRepo.findById(userId).get()
            executors.add(user)
        }
        return executors
    }

}
