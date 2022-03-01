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
            entity = Mentor::class,
            parentColumns = ["id"],
            childColumns = ["mentor_id"],
            onDelete = CASCADE
        )
    ]
)
data class Group(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var name: String,
    @ColumnInfo(name = "mentor_id")
    var mentorId: Int,
    var date: String,
    var days: String,
    var isCreated: Boolean
) : Serializable
