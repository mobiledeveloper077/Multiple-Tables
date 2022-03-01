package uz.abduvali.pdpuz.dao

import androidx.room.*
import uz.abduvali.pdpuz.entities.Group
import uz.abduvali.pdpuz.models.GroupWithMentor

@Dao
interface GroupDao {

    @Insert
    fun addGroup(group: Group)

    @Delete
    fun deleteGroup(group: Group)

    @Update
    fun editGroup(group: Group)

    @Query("select * from `group` where id=:id")
    fun getGroupById(id: Int): Group

    @Query("select * from `group`")
    fun getGroupList(): List<Group>

    @Transaction
    @Query("select * from `group`")
    fun getGroupsWithMentor(): List<GroupWithMentor>

    @Transaction
    @Query("select * from `group` where `group`.mentor_id=:id")
    fun getGroupsByMentorId(id: Int): List<Group>
}