package uz.abduvali.pdpuz.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.abduvali.pdpuz.databinding.ItemMentorBinding
import uz.abduvali.pdpuz.entities.Mentor

class MentorAdapter(
    var list: ArrayList<Mentor>,
    var listener: OnItemClickListener
) : RecyclerView.Adapter<MentorAdapter.MyViewHolder>() {

    inner class MyViewHolder(var binding: ItemMentorBinding) : RecyclerView.ViewHolder(binding.root)

    interface OnItemClickListener {
        fun itemEditClick(mentor: Mentor, position: Int)
        fun itemDeleteClick(mentor: Mentor, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        MyViewHolder(ItemMentorBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val mentor = list[position]
        holder.binding.apply {
            name.text = mentor.lastname + " " + mentor.firstname
            middleName.text = mentor.middleName
            edit.setOnClickListener { listener.itemEditClick(mentor, position) }
            delete.setOnClickListener { listener.itemDeleteClick(mentor, position) }
        }
    }

    override fun getItemCount() = list.size
}