package uz.abduvali.pdpuz.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import uz.abduvali.pdpuz.entities.Course

@Dao
interface CourseDao {

    @Insert
    fun addCourse(course: Course)

    @Query("select * from course where id=:id")
    fun getCourseById(id: Int): Course

    @Query("select * from course")
    fun getCourseList(): List<Course>
}