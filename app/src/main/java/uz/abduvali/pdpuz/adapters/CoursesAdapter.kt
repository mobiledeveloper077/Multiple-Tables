package uz.abduvali.pdpuz.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.abduvali.pdpuz.databinding.ItemCourseBinding
import uz.abduvali.pdpuz.entities.Course

class CoursesAdapter(var list: ArrayList<Course>, var listener: onItemClickListener) :
    RecyclerView.Adapter<CoursesAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemCourseBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.apply {
            val course = list[position]
            name.text = course.name
            root.setOnClickListener { listener.onClick(course) }
        }
    }

    override fun getItemCount() = list.size

    inner class MyViewHolder(var binding: ItemCourseBinding) :
        RecyclerView.ViewHolder(binding.root)

    interface onItemClickListener {
        fun onClick(course: Course)
    }
}