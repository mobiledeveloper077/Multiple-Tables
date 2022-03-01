package uz.abduvali.pdpuz.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.abduvali.pdpuz.databinding.ItemGroupBinding
import uz.abduvali.pdpuz.entities.Group

class GroupAdapter(
    var list: List<Group>,
    var studentsCount: ArrayList<Int>,
    var listener: OnItemClickListener
) : RecyclerView.Adapter<GroupAdapter.MyViewHolder>() {

    inner class MyViewHolder(var binding: ItemGroupBinding) : RecyclerView.ViewHolder(binding.root)

    interface OnItemClickListener {
        fun itemViewClick(group: Group)
        fun itemEditClick(group: Group, position: Int)
        fun itemDeleteClick(group: Group, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        MyViewHolder(ItemGroupBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val group = list[position]
        holder.binding.apply {
            name.text = group.name
            if (studentsCount[position] == 0) {
                count.text = "O'quvchi qo'shilmagan"
            } else {
                count.text = "O'quvchilar soni: ${studentsCount[position]} ta"
            }
            view.setOnClickListener { listener.itemViewClick(group) }
            edit.setOnClickListener { listener.itemEditClick(group, position) }
            delete.setOnClickListener { listener.itemDeleteClick(group, position) }
        }
    }

    override fun getItemCount() = list.size
}