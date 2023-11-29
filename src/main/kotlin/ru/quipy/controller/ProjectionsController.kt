package ru.quipy.controller

import org.springframework.web.bind.annotation.*
import ru.quipy.projections.ProjectProjection
import ru.quipy.projections.TaskProjection
import ru.quipy.projections.UserProjection
import ru.quipy.services.ProjectionsService
import java.util.*

@RestController
@RequestMapping("/projections")
class ProjectionsController (val projectionsService: ProjectionsService) {
    @GetMapping("/userProjections")
    fun getUserProjections(): List<UserProjection> {
        return this.projectionsService.getAllUserProjection()
    }

    @GetMapping("/projectProjections/{id}/")
    fun getProjectProjection(@PathVariable id : UUID): ProjectProjection {
        return this.projectionsService.getProjectProjectionById(id)
    }

    @GetMapping("/projectProjections/{id}/participants")
    fun getProjectParticipants(@PathVariable id : UUID): Any {
        return this.projectionsService.getProjectParticipants(id)
    }

    @GetMapping("/projectProjections/{id}/tasks")
    fun getProjectTasks(@PathVariable id : UUID): Any {
        return this.projectionsService.getProjectTasks(id)
    }

    @GetMapping("/projectProjections/{id}/statuses")
    fun getProjectStatuses(@PathVariable id : UUID): Any {
        return this.projectionsService.getProjectStatuses(id)
    }

    @GetMapping("/taskProjections/{id}/")
    fun getTaskProjection(@PathVariable id : UUID): TaskProjection {
        return this.projectionsService.getTaskById(id)
    }

    @GetMapping("/taskProjections/{id}/executors")
    fun getTaskExecutors(@PathVariable id : UUID): Any {
        return this.projectionsService.getTaskExecutors(id)
    }
}