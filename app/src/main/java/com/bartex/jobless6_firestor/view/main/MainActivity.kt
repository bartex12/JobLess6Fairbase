package com.bartex.jobless6_firestor.view.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bartex.jobless6_firestor.model.Note
import com.bartex.jobless6_firestor.R
import com.bartex.jobless6_firestor.Utils
import com.bartex.jobless6_firestor.adapter.MainAdapter
import com.bartex.jobless6_firestor.view.note.NoteActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }
    private lateinit var adapter: MainAdapter
    private lateinit var mainRecycler: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
        initAdapter()

        setSupportActionBar(findViewById(R.id.toolbar))
        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener{
            openNoteScreen(null)
        }

        viewModel.loadNotes()

        viewModel.getViewState().observe(this, object : Observer<MainViewState> {
            override fun onChanged(t: MainViewState?) {
                if (t==null) return
                if (t.error!=null)renderError(t.error)
                if (t.notes != null)renderData(t.notes)
            }
        })
    }

    private fun initView() {
        mainRecycler = findViewById(R.id.mainRecycler)
    }

    private fun initAdapter() {
        adapter = MainAdapter(object : MainAdapter.OniIemClickListener {
            override fun onItemClick(note: Note) {
                //вызываем NoteActivity
                openNoteScreen(note)
            }
        })
        mainRecycler.layoutManager = LinearLayoutManager(this)
        mainRecycler.adapter = adapter
    }

    //переходим на экран редактирования
    fun openNoteScreen(note: Note?){
        val intent:Intent = NoteActivity.getStartIntent(this, note?.id )
        startActivity(intent)
    }

    fun renderData(data: List<Note>?) {
        val dataSorted = Utils.sortListOfNoteDescending(data)
        dataSorted?. let {adapter.notes =  it}?:return
    }

    fun renderError(error: Throwable){
        if (error.message != null) showError(error.message!!)
    }
    private fun showError(error:String){
        val snackbar = Snackbar.make(mainRecycler, error, Snackbar.LENGTH_INDEFINITE)
        snackbar.setAction(R.string.ok_bth_title){ snackbar.dismiss() }
        snackbar.show()
    }
}