package uz.abduvali.pdpuz.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.abduvali.pdpuz.databinding.ItemStudentBinding
import uz.abduvali.pdpuz.entities.Student

class StudentAdapter(
    var list: List<Student>,
    var listener: OnItemClickListener
) : RecyclerView.Adapter<StudentAdapter.MyViewHolder>() {

    inner class MyViewHolder(var binding: ItemStudentBinding) :
        RecyclerView.ViewHolder(binding.root)

    interface OnItemClickListener {
        fun itemEditClick(student: Student, position: Int)
        fun itemDeleteClick(student: Student, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        MyViewHolder(ItemStudentBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val student = list[position]
        holder.binding.apply {
            name.text = student.lastname + " " + student.firstname
            middleName.text = student.lastname
            date.text = student.date
            edit.setOnClickListener { listener.itemEditClick(student, position) }
            delete.setOnClickListener {
                listener.itemDeleteClick(student, position)
            }
        }
    }

    override fun getItemCount() = list.size
}