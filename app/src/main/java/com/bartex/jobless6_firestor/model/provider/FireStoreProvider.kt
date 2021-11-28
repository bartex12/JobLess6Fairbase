package com.bartex.jobless6_firestor.model.provider

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bartex.jobless6_firestor.model.Note
import com.bartex.jobless6_firestor.model.NoteResult
import com.google.firebase.firestore.*

class FireStoreProvider: RemoteDataProvider {

companion object{
    private const val NOTES_COLLECTION = "notesPressureAndPulse"
}
    //Чтобы создать новую коллекцию, достаточно получить экземпляр базы данных для приложения:
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    //И вызвать у него метод collection() с именем нужной коллекции:
    private val notesReference: CollectionReference = db.collection(NOTES_COLLECTION)

    override fun subscribeToAllNotes(): LiveData<NoteResult> {
        val result = MutableLiveData<NoteResult>()
        notesReference.addSnapshotListener(object : EventListener<QuerySnapshot> {
            override fun onEvent(snapshot: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error!=null){
                    result.value = NoteResult.Error(error)
                }
                if (snapshot!=null){
                    val notes = mutableListOf<Note>()
                    for (doc: QueryDocumentSnapshot in snapshot){
                        notes.add(doc.toObject(Note::class.java))
                    }
                    result.value = NoteResult.Success(notes)
                }
            }
        })
        return result
    }

    override fun getNoteById(id: String): LiveData<NoteResult> {
        val result = MutableLiveData<NoteResult>()
        notesReference.document(id)
            .get()
            .addOnSuccessListener { snapshot -> //не сразу запись а ее снимок
                result.value = NoteResult.Success(snapshot.toObject(Note::class.java))
            }.addOnFailureListener {
                result.value = NoteResult.Error(it)
            }
        return result
    }

    override fun saveNote(note: Note): LiveData<NoteResult> {
        val result = MutableLiveData<NoteResult>()
       notesReference.document(note.id)
            .set(note) //возвращает объект типа Task
            .addOnSuccessListener {
                result.value = NoteResult.Success(note)
            }.addOnFailureListener {p0 ->
               result.value = NoteResult.Error(p0)
            }
        return result
    }
}