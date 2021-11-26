package com.bartex.jobless6_firestor.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bartex.jobless6_firestor.viewmodel.MainViewModel
import com.bartex.jobless6_firestor.MainViewState
import com.bartex.jobless6_firestor.model.Note
import com.bartex.jobless6_firestor.R
import com.bartex.jobless6_firestor.adapter.MainAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: MainAdapter
    private lateinit var mainRecycler: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()

        setSupportActionBar(findViewById(R.id.toolbar))
        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener{
            openNoteScreen(null)
        }

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        adapter = MainAdapter(object : MainAdapter.OniIemClickListener{
            override fun onItemClick(note: Note) {
                //вызываем NoteActivity
                openNoteScreen(note)
            }
        })
        mainRecycler.layoutManager = LinearLayoutManager(this)
        mainRecycler.adapter = adapter

        //чтобы сразу показывались заметки на экране - во viewModel использован блок init
        viewModel.viewState().observe(this, {
            //передаём список в адаптер путём присваивания полю значения
            it?. let{adapter.notes =  it.notes}
        })
    }

    private fun initView() {
        mainRecycler = findViewById(R.id.mainRecycler)
    }

    fun openNoteScreen(note: Note?){
        val intent: Intent = NoteActivity.getStartIntent(this, note)
        startActivity(intent)
    }
}