package ru.quipy.controller

import org.springframework.web.bind.annotation.*
import ru.quipy.projections.ProjectProjection
import ru.quipy.projections.UserProjection
import ru.quipy.services.ProjectionsService

@RestController
@RequestMapping("/projections")
class ProjectionsController (val projectionsService: ProjectionsService) {
    @GetMapping("/userProjections")
    fun getUserProjections(): List<UserProjection> {
        return this.projectionsService.getAllUserProjection()
    }

    @GetMapping("/projectProjections")
    fun getProjectProjections(): List<ProjectProjection> {
        return this.projectionsService.getAllProjectProjection()
    }
}