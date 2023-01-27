package com.example.myapplication

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class InoculationAdapter(private val inoculations: ArrayList<Inoculation>, var animalID: Int, private val clickListener: InoculationHistoryListener) : RecyclerView.Adapter<InoculationAdapter.InoculationViewHolder>(){
    class InoculationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var myHelper: MyDBHelper = MyDBHelper(itemView.context)
        var itemModify: ImageButton = itemView.findViewById(R.id.modify_btn)
        var itemDelete: ImageButton = itemView.findViewById(R.id.delete_btn)
        var itemDate: TextView = itemView.findViewById(R.id.inoculation_date)
        var itemContent: TextView = itemView.findViewById(R.id.inoculation_content)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InoculationViewHolder {
        var cardView = LayoutInflater.from(parent.context).inflate(R.layout.inoculation_item, parent, false)
        return InoculationViewHolder(cardView)
    }

    override fun onBindViewHolder(holder: InoculationViewHolder, position: Int) {
        holder.itemDate.text = inoculations[position].inoculation_date
        holder.itemContent.text = inoculations[position].inoculation_content

        holder.itemView.setOnClickListener {
            clickListener.onClick(inoculations[position])
        }
        holder.itemModify.setOnClickListener {
            val intent = Intent(it.context, AddInoculationActivity::class.java)
            intent.putExtra("animal_name", inoculations[position].animal_name)
            intent.putExtra(ANIMAL_ID_EXTRA, animalID)
            intent.putExtra(INOCULATION_ID_EXTRA, inoculations[position].id)
            intent.putExtra("regNum", inoculations[position].animal_reg_num)
            it.context.startActivity(intent)
        }
        holder.itemDelete.setOnClickListener {
            //데이터 삭제
            holder.myHelper.deleteInoculationData(inoculations[position].id.toString())
            inoculations.removeAt(position)
            this.notifyItemRemoved(position)
            this.notifyItemRangeRemoved(position, getItemCount())
        }
    }

    override fun getItemCount(): Int {
        return inoculations.size
    }

    interface InoculationHistoryListener {
        fun onClick(inoculation: Inoculation)
    }
}