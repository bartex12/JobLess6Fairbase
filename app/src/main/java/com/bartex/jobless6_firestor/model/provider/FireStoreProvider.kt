package com.bartex.jobless6_firestor.model.provider

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bartex.jobless6_firestor.model.Note
import com.bartex.jobless6_firestor.model.NoteResult
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.*

private const val NOTES_COLLECTION = "notesPressureAndPulse"

class FireStoreProvider: RemoteDataProvider {

    private val TAG = FireStoreProvider::class.java.simpleName

    //Чтобы создать новую коллекцию, достаточно получить экземпляр базы данных для приложения:
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    //И вызвать у него метод collection() с именем нужной коллекции:
    private val notesReference: CollectionReference = db.collection(NOTES_COLLECTION)

//    Реализация метода subscribeToAllNotes() будет отличаться от двух других,
//     так как мы хотим получать не только все заметки, но и обновления при изменении
//     коллекции notes. Для этого добавим к коллекции EventListener, метод которого будет
//     вызываться при первом добавлении его к коллекции и каждом изменении документов в ней,
//     а также получать снимок обновленной коллекции. При изменении или добавлении документа
//     сразу получим обновленную коллекцию.

    //метод onEvent() вызывается и при изменении в коллекции, и при возникновении
    // ошибок обращения к ней. И тип ошибки, и тип данных, получаемых в методе, объявлены
    // с вопросительным знаком. Это означает, что данные типы поддерживают значение null.
    // Поэтому в теле метода проверяем на null пришедшую в метод ошибку.
    // Если ошибки нет, проверяем на null данные и проходим по документам в коллекции,
    // преобразуем их в экземпляры Note.

    //ни в одном из методов не задаем generic-тип для NoteResult.Success.
    // В этом нет необходимости, потому что компилятор может вывести тип из переданного значения.

    //Еще одно важное замечание. Метод doc.toObject(Note::class.java) занимается тем,
    // что трансформирует данные (маппинг), полученные с сервера FireBase, в данные типа Note.
    // Это значит, что в классе Note должен быть пустой конструктор —
    // это обязательный контракт для маппинга данных через FireBase
    //это значит , что у каждого поля в классе Note должно быть значение по умолчанию
    override fun subscribeToAllNotes(): LiveData<NoteResult> {
        Log.d(TAG, "***0*** FireStoreProvider subscribeToAllNotes")
        val result = MutableLiveData<NoteResult>()
        notesReference.addSnapshotListener(object : EventListener<QuerySnapshot> {
            override fun onEvent(snapshot: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error!=null){
                    Log.d(TAG, "***0-1*** FireStoreProvider subscribeToAllNotes error!=null")
                    result.value = NoteResult.Error(error)
                }
                if (snapshot!=null){
                    Log.d(TAG, "***0-2*** FireStoreProvider subscribeToAllNotes snapshot!=null")
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

    //метод для получения заметки по id
    //вызываем у документа метод get() и в методе onSuccess()
    // получаем экземпляр DocumentSnapshot, который представляет собой снимок
    // интересующего нас документа. Чтобы преобразовать его в экземпляр Note,
    // вызовем у него метод toObject(), в который передадим класс интересующего нас объекта
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

    // метод для сохранения записи  в облаке
    //Чтобы получить ссылку на документ, надо вызвать метод document(), передав в него имя документа.
    //Это будет id записи, так как оно уникально и хранится в записи
    //Чтобы сохранить новый документ или заменить данные в существующем, есть метод set(),
    // в который можем передать экземпляр записи. Метод set() возвращает объект типа Task
    // — задание для базы данных на проведение транзакции.
    //
    //Чтобы получить callback при успешном сохранении документа, вызовем у него метод
    // addOnSuccessListener и передадим анонимный экземпляр OnSuccessListener.
    // При успешном сохранении документа не возвращается значение,
    // только вызывается метод onSuccess() у переданного OnSuccessListener.
    // Чтобы получить callback в классах, инициирующих сохранение,
    // будем возвращать NoteResult.Success с сохраненной записью в поле data.
    // Для callback’а при неудачном сохранении документа добавим OnFailureListener
    // и вернем экземпляр NoteResult.Error с полученной ошибкой
    //
    //И в случае успеха, и при ошибке у нас будет экземпляр NoteResult.
    // Можем использовать для возврата значения LiveData,
    // параметризованную типом NotesResult. В месте подписки на эту LiveData
    // проверим результат на тип и сможем понять, удачно ли прошло сохранение
    override fun saveNote(note: Note): LiveData<NoteResult> {
        val result = MutableLiveData<NoteResult>()
       notesReference.document(note.id)
            .set(note) //возвращает объект типа Task
            .addOnSuccessListener {
                Log.d(TAG, "Note $note is saved")
                result.value = NoteResult.Success(note)
            }.addOnFailureListener {p0 ->
               Log.d(TAG, "Error saving note $note, message:${p0.message}")
               result.value = NoteResult.Error(p0)
            }
        return result
    }
}