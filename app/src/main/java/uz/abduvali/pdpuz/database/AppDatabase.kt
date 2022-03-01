package uz.abduvali.pdpuz.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import uz.abduvali.pdpuz.dao.CourseDao
import uz.abduvali.pdpuz.dao.GroupDao
import uz.abduvali.pdpuz.dao.MentorDao
import uz.abduvali.pdpuz.dao.StudentDao
import uz.abduvali.pdpuz.entities.Course
import uz.abduvali.pdpuz.entities.Group
import uz.abduvali.pdpuz.entities.Mentor
import uz.abduvali.pdpuz.entities.Student

@Database(
    entities = [Course::class, Mentor::class, Group::class, Student::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun courseDao(): CourseDao
    abstract fun mentorDao(): MentorDao
    abstract fun groupDao(): GroupDao
    abstract fun studentDao(): StudentDao

    companion object {
        private var instance: AppDatabase? = null

        @Synchronized
        fun getInstance(context: Context): AppDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(context, AppDatabase::class.java, "my_db")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
            }
            return instance!!
        }
    }
}