package com.bartex.jobless6_firestor.view.note

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bartex.jobless6_firestor.R
import com.bartex.jobless6_firestor.Utils
import com.bartex.jobless6_firestor.model.Note
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import java.util.*

class NoteActivity : AppCompatActivity() {
    private val viewModel: NoteViewModel by lazy {
        ViewModelProvider(this).get(NoteViewModel::class.java)
    }
    private lateinit var hPress: TextInputEditText
    private lateinit var lPress: TextInputEditText
    private lateinit var puls: TextInputEditText
    private lateinit var toolbarNote: Toolbar
    private lateinit var buttonSave: Button
    private var note: Note? = null
    private val TAG = "33333"

    companion object {
        private val EXTRA_NOTE = NoteActivity::class.java.name + "extra.NOTE"

        fun getStartIntent(context: Context, noteId: String? = null): Intent {
            val intent = Intent(context, NoteActivity::class.java)
            intent.putExtra(EXTRA_NOTE, noteId)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)

        initViews()
        setSupportActionBar(toolbarNote)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val noteId  = intent.getStringExtra(EXTRA_NOTE)
        if (noteId != null){
            viewModel.loadNote(noteId)
        }else{
            supportActionBar?.title = getString(R.string.new_note_title)
        }

        viewModel.getViewState().observe(this, object : Observer<NoteViewState> {
            override fun onChanged(t: NoteViewState?) {
                Log.d(TAG, "***3*** BaseActivity onChanged")
                if (t==null) return
                if (t.error!=null)renderError(t.error)
                if (t.note != null)renderData(t.note)
            }
        })
        buttonSave.setOnClickListener { saveNoteInFirebase() }
    }

    private fun saveNoteInFirebase() {
        if (hPress.text == null || hPress.text!!.length < 2||hPress.text?.isEmpty() == true||
            lPress.text == null || lPress.text!!.length <2||lPress.text?.isEmpty() == true||
            puls.text == null || puls.text!!.length <2 ||puls.text?.isEmpty() == true ){
            Snackbar.make(puls, getString(R.string.press_pulse), Snackbar.LENGTH_SHORT)
                .setAnchorView(puls)
                .show()
            return
        }
        note = note?.copy(
            highPress = hPress.text.toString(),
            lowPress = lPress.text.toString(),
            pulse = puls.text.toString(),
        )?:createNewNote()

        note?. let{ viewModel.saveChanges(it)}
        finish()
    }

    // присваиваем полученную из базы данных заметку свойству note.
    fun renderData(data: Note?) {
        this.note = data
        supportActionBar?.title = this.note?.let {note!!.titleDate +"   "+ note?.lastTime}

      note?. let {
            hPress.setText(it.highPress)
            lPress.setText(it.lowPress)
            puls.setText(it.pulse)
        }
    }

    fun renderError(error: Throwable){
        if (error.message != null) showError(error.message!!)
    }

    private fun showError(error:String){
        val snackbar = Snackbar.make(toolbarNote, error, Snackbar.LENGTH_INDEFINITE)
        snackbar.setAction(R.string.ok_bth_title){ snackbar.dismiss() }
        snackbar.show()
    }

    private fun initViews() {
        hPress = findViewById(R.id.highPressureEdit)
        lPress = findViewById(R.id.lowPressureEdit)
        puls = findViewById(R.id.pulseEdit)
        toolbarNote =  findViewById(R.id.toolbarNote)
        buttonSave =  findViewById(R.id.buttonSave)
    }

    private fun createNewNote():Note{
        return Note(
            id = UUID.randomUUID().toString(),
            titleDate = Utils.getDate(),
            highPress = hPress.text.toString(),
            lowPress = lPress.text.toString(),
            pulse = puls.text.toString(),
            lastTime = Utils.getTime()
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean  =
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
}