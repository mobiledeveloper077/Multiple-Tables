package uz.abduvali.pdpuz.models

import androidx.room.Embedded
import androidx.room.Relation
import uz.abduvali.pdpuz.entities.Course
import uz.abduvali.pdpuz.entities.Mentor

data class MentorWithCourse(
    @Embedded val mentor: Mentor,
    @Relation(parentColumn = "course_id", entityColumn = "id")
    val course: Course
)
