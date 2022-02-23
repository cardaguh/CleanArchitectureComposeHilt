package com.plcoding.cleanarchitecturenoteapp.feature_note.domain.use_case

/**
 * Created by Carlos Daniel Agudelo on 22/02/2022.
 */
data class NoteUseCases(
    val getNotes: GetNotes,
    val deleteNote: DeleteNote,
    val addNote: AddNote,
    val getNote: GetNote
)
