package uz.abduvali.pdpuz.models

import androidx.room.Embedded
import androidx.room.Relation
import uz.abduvali.pdpuz.entities.Group
import uz.abduvali.pdpuz.entities.Student

data class StudentWithGroup(
    @Embedded val student: Student,
    @Relation(parentColumn = "group_id", entityColumn = "id")
    val group: Group
)
