package com.example.myapplication

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class ScheduleAdapter(var schedules: ArrayList<Schedule>, var animalID: Int, private val clickListener: ScheduleListener) :
    RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder>() {
    class ScheduleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var myHelper: MyDBHelper = MyDBHelper(itemView.context)
        var itemModify: ImageButton = itemView.findViewById(R.id.modify_btn)
        var itemDelete: ImageButton = itemView.findViewById(R.id.delete_btn)
        var itemDate: TextView = itemView.findViewById(R.id.schedule_date)
        var itemContent: TextView = itemView.findViewById(R.id.schedule_content)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        var cardView = LayoutInflater.from(parent.context).inflate(R.layout.schedule_item, parent, false)
        return ScheduleViewHolder(cardView)
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        holder.itemDate.text = schedules[position].schedule_date
        holder.itemContent.text = schedules[position].schedule_content

        //수정 버튼
        holder.itemModify.setOnClickListener {
            //일정 추가 액티비티로 이동해서 수정
            val intent = Intent(it.context, AddScheduleActivity::class.java)
            intent.putExtra("animal_name", schedules[position].animal_name)
            intent.putExtra(ANIMAL_ID_EXTRA, animalID)
            intent.putExtra(SCHEDULE_ID_EXTRA, schedules[position].id)
            intent.putExtra("regNum", schedules[position].animal_reg_num)
            it.context.startActivity(intent)
        }
        //삭제 버튼
        holder.itemDelete.setOnClickListener {
            //데이터 삭제
            holder.myHelper.deleteScheduleData(schedules[position].id.toString())
            //화면에 반영
            schedules.removeAt(position)
            this.notifyItemRemoved(position)
            this.notifyItemRangeRemoved(position, getItemCount())
        }

        holder.itemView.setOnClickListener {
            clickListener.onClick(schedules[position])
        }
    }

    override fun getItemCount(): Int {
        return schedules.size
    }

    interface ScheduleListener {
        fun onClick(schedule: Schedule)
    }
}