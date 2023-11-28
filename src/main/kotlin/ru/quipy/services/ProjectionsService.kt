package ru.quipy.services

import org.springframework.stereotype.Service
import ru.quipy.projections.ProjectProjection
import ru.quipy.projections.ProjectProjectionRepo
import ru.quipy.projections.UserProjection
import ru.quipy.projections.UserProjectionRepo

@Service
class ProjectionsService constructor(val userProjectionRepo: UserProjectionRepo, val projectProjectionRepo: ProjectProjectionRepo){
    fun getAllUserProjection(): List<UserProjection> {
        return userProjectionRepo.findAll()
    }

    fun getAllProjectProjection(): List<ProjectProjection> {
        return projectProjectionRepo.findAll()
    }

}
