package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AnimalAdapter(private val animals: List<Animal>, private val clickListener: AnimalListener) :
        RecyclerView.Adapter<AnimalAdapter.MyViewHolder>(){
            class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
                var itemImg: ImageView = itemView.findViewById(R.id.animal_picture)
                var itemName: TextView = itemView.findViewById(R.id.animal_name)
                var itemRegNum: TextView = itemView.findViewById(R.id.animal_reg_num)
            }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var cardView = LayoutInflater.from(parent.context).inflate(R.layout.animal_item, parent, false)
        return MyViewHolder(cardView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        //항목에 표시될 구성요소 설정
        //저장된 이미지가 없는 경우에는 기본 이미지로 설정
        if(animals[position].animal_img.toString() ==""){
            holder.itemImg.setImageResource(R.mipmap.ic_launcher_default_animal)
        }
        else{
            holder.itemImg.setImageURI(animals[position].animal_img)
        }

        holder.itemName.text =  animals[position].animal_name
        holder.itemRegNum.text = animals[position].animal_reg_num

        //항목 선택 시 프로필로 이동될 리스너 설정
        holder.itemView.setOnClickListener {
            clickListener.onClick(animals[position])
        }
    }

    override fun getItemCount(): Int {
        return animals.size
    }

    interface AnimalListener{
        fun onClick(animal: Animal)
    }
}