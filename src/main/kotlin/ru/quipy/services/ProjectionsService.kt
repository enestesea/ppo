package ru.quipy.services

import org.springframework.stereotype.Service
import ru.quipy.projections.UserProjection
import ru.quipy.projections.UserProjectionRepo

@Service
class ProjectionsService constructor(val userProjectionRepo: UserProjectionRepo){
    fun getAllUserProjection(): List<UserProjection> {
        return userProjectionRepo.findAll()
    }

}
