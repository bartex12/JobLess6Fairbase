package com.bartex.jobless6_firestor.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bartex.jobless6_firestor.model.Note
import com.bartex.jobless6_firestor.R

class MainAdapter(private val oniIemClickListener: OniIemClickListener)
    : RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    private var currentNote:Note? = null
    var   isSameNote = false

    interface OniIemClickListener{
        fun onItemClick(note: Note)
    }

    //так сделано чтобы передавать список в адаптер без конструктора
    // - через присвоение полю значения
    var notes:List<Note> = listOf()
        set(value){
            field =  value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_control, parent, false)
        )


    override fun getItemCount(): Int = notes.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val arrNote = notes[position].titleDate.split("-")
        val arrCurrent:List<String>? =  currentNote?.titleDate?.split("-")
        isSameNote =  arrCurrent?. let{
                    arrNote[0].toInt() == arrCurrent[0].toInt()
                    &&arrNote[1].toInt() == arrCurrent[1].toInt()
                    &&arrNote[2].toInt() == arrCurrent[2].toInt()
        } ?:false
        holder.bind(notes[position])
        currentNote = notes[position]
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val  tvDate: TextView = itemView.findViewById(R.id.tvDate)
        private val  tvTime: TextView = itemView.findViewById(R.id.tvTime)
        private val  tvHPress: TextView = itemView.findViewById(R.id.tvHPress)
        private val  tvLPress: TextView = itemView.findViewById(R.id.tvLPress)
        private val  tvPulse: TextView = itemView.findViewById(R.id.tvPulse)
        private val  clGroup: ConstraintLayout = itemView.findViewById(R.id.clGroup)

        fun bind(note: Note){
            if (isSameNote){
                tvDate.visibility = View.GONE
            }else{
                tvDate.visibility = View.VISIBLE
                tvDate.text = note.titleDate
            }
            tvTime.text = note.lastTime
            tvHPress.text = note.highPress
            tvLPress.text = note.lowPress
            tvPulse.text = note.pulse
            if (note.highPress.toInt() in 100..130 && note.lowPress.toInt() in 70..85){
                clGroup.background = ContextCompat.getDrawable(itemView.context, R.drawable.gr2)
            }else{
                clGroup.background = ContextCompat.getDrawable(itemView.context, R.drawable.gr3)
            }

            //ставим слушатель щелчков на заметках и передаём заметку через интерфейс в активити
            itemView.setOnClickListener {
                oniIemClickListener.onItemClick(note)
            }
        }

    }
}