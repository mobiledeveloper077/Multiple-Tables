package uz.abduvali.pdpuz.dao

import androidx.room.*
import uz.abduvali.pdpuz.entities.Student
import uz.abduvali.pdpuz.models.StudentWithGroup

@Dao
interface StudentDao {

    @Insert
    fun addStudent(student: Student)

    @Delete
    fun deleteStudent(student: Student)

    @Update
    fun editStudent(student: Student)

    @Query("select * from student where id=:id")
    fun getStudentById(id: Int): Student

    @Query("select * from student")
    fun getStudentList(): List<Student>

    @Transaction
    @Query("select * from student")
    fun getStudentsWithGroup(): List<StudentWithGroup>

    @Transaction
    @Query("select * from student where student.group_id =:id")
    fun getStudentByGroupId(id: Int): List<Student>
}