package uz.abduvali.pdpuz.dao

import androidx.room.*
import uz.abduvali.pdpuz.entities.Mentor
import uz.abduvali.pdpuz.models.MentorWithCourse

@Dao
interface MentorDao {

    @Insert
    fun addMentor(mentor: Mentor)

    @Delete
    fun deleteMentor(mentor: Mentor)

    @Update
    fun editMentor(mentor: Mentor)

    @Query("select * from mentor where id=:id")
    fun getMentorById(id: Int): Mentor

    @Query("select * from mentor")
    fun getMentorList(): List<Mentor>

    @Transaction
    @Query("select * from mentor")
    fun getMentorsWithCourse(): List<MentorWithCourse>

    @Transaction
    @Query("select * from mentor where mentor.course_id=:id")
    fun getMentorByCourseId(id: Int): List<Mentor>
}