package uz.abduvali.pdpuz.models

import androidx.room.Embedded
import androidx.room.Relation
import uz.abduvali.pdpuz.entities.Group
import uz.abduvali.pdpuz.entities.Mentor

data class GroupWithMentor(
    @Embedded val group: Group,
    @Relation(parentColumn = "mentor_id", entityColumn = "id")
    val mentor: Mentor
)
