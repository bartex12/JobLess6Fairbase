package com.bartex.jobless6_firestor.view

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.SyncStateContract
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.widget.EditText
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bartex.jobless6_firestor.R
import com.bartex.jobless6_firestor.Utils
import com.bartex.jobless6_firestor.model.Note
import com.bartex.jobless6_firestor.viewmodel.NoteViewModel
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*

class NoteActivity : AppCompatActivity() {

    private lateinit var viewModel: NoteViewModel
    private lateinit var hPress: TextInputEditText
    private lateinit var lPress: TextInputEditText
    private lateinit var puls: TextInputEditText
    private lateinit var toolbarNote: Toolbar

    private var note: Note? = null
    private val TAG = "33333"

    companion object {

        private val EXTRA_NOTE = NoteActivity::class.java.name + "extra.NOTE"
        private const val DATE_TIME_FORMAT = "dd.MM.yy HH:mm"
        private const val SAVE_DELAY = 2000L

        fun getStartIntent(context: Context, note: Note? = null): Intent {
            val intent = Intent(context, NoteActivity::class.java)
            intent.putExtra(EXTRA_NOTE, note)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)

        note = intent.getParcelableExtra(EXTRA_NOTE)
        Log.d(TAG, "NoteActivity onCreate note = " + note)

        initViews()

        setSupportActionBar(toolbarNote)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportActionBar?.title = if (note != null) {
            note!!.titleDate + "   "+ note!!.lastTime
        } else {
            getString(R.string.new_note_title)
        }

        viewModel = ViewModelProvider(this).get(NoteViewModel::class.java)
    }

    private fun initViews() {
        hPress = findViewById(R.id.highPressureEdit)
        lPress = findViewById(R.id.lowPressureEdit)
        puls = findViewById(R.id.pulseEdit)
        toolbarNote =  findViewById(R.id.toolbarNote)

        if (note != null) {
            hPress.setText(note!!.highPress)
            lPress.setText(note!!.lowPress)
            puls.setText(note!!.pulse)
        }
        hPress.addTextChangedListener(textChangeListener)
        lPress.addTextChangedListener(textChangeListener)
        puls.addTextChangedListener(textChangeListener)
    }

    private val textChangeListener = object : TextWatcher {
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            triggerSaveNote()
        }
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
    }

    fun  triggerSaveNote(){
        if (hPress.text == null || hPress.text!!.length <2||
            lPress.text == null || lPress.text!!.length <2||
            puls.text == null || puls.text!!.length <2)  return
        Handler(Looper.getMainLooper()).postDelayed({
            note = note?.copy(
                highPress = hPress.text.toString(),
                lowPress = lPress.text.toString(),
                pulse = puls.text.toString(),
            )?:createNewNote()
            note?. let{ viewModel.saveChanges(it)}
        }, SAVE_DELAY)
    }

    private fun createNewNote():Note =Note(
        id = UUID.randomUUID().toString(),
        titleDate = Utils.getDate(),
        highPress = hPress.text.toString(),
        lowPress = lPress.text.toString(),
        pulse = puls.text.toString(),
        lastTime = Utils.getTime()
    )

    override fun onOptionsItemSelected(item: MenuItem): Boolean  =
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

}