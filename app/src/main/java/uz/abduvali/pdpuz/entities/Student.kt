package uz.abduvali.pdpuz.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Group::class,
            parentColumns = ["id"],
            childColumns = ["group_id"],
            onDelete = CASCADE
        )
    ]
)
data class Student(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0, var lastname: String, var firstname: String,
    var middleName: String, var date: String,
    @ColumnInfo(name = "group_id")
    var groupId: Int
) : Serializable
