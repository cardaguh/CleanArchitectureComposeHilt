package com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.notes

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.cleanarchitecturenoteapp.feature_note.domain.model.Note
import com.plcoding.cleanarchitecturenoteapp.feature_note.domain.use_case.NoteUseCases
import com.plcoding.cleanarchitecturenoteapp.feature_note.domain.util.NoteOrder
import com.plcoding.cleanarchitecturenoteapp.feature_note.domain.util.OrderType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Carlos Daniel Agudelo on 22/02/2022.
 */
@HiltViewModel
class NotesViewModel @Inject constructor(
    private val noteUsesCases: NoteUseCases
): ViewModel() {

    private val _state = mutableStateOf(NotesState())
    val state: State<NotesState> = _state

    private var recentlyDeletedNote: Note? = null

    private var getNotesJob: Job? = null

    init {
        getNotes(NoteOrder.Date(OrderType.Descending))
    }

    fun onEvent(event: NotesEvent) {
       when(event) {
           is NotesEvent.Order -> {
               if (state.value.noteOrder::class == event.noteOrder::class &&
                       state.value.noteOrder.orderType == event.noteOrder.orderType) {
                   return
               }
               getNotes(event.noteOrder)
           }
           is NotesEvent.DeleteNote -> {
               viewModelScope.launch {
                   noteUsesCases.deleteNote(event.note)
                   recentlyDeletedNote = event.note
               }
           }
           is NotesEvent.RestoreNote -> {
               viewModelScope.launch {
                   noteUsesCases.addNote(recentlyDeletedNote ?: return@launch)
                   recentlyDeletedNote = null
               }

           }
           is NotesEvent.ToggleOrderSection -> {
               _state.value = state.value.copy(
                   isOrderSectionVisible = !state.value.isOrderSectionVisible
               )

           }

       }
    }

    private fun getNotes(noteOrder: NoteOrder) {
        getNotesJob?.cancel()
        getNotesJob = noteUsesCases.getNotes(noteOrder)
            .onEach { notes->
                _state.value = state.value.copy(
                    notes = notes,
                    noteOrder = noteOrder
                )
            }
            .launchIn(viewModelScope)
    }
}