package com.reco.jpa.repository;

import com.reco.jpa.domain.Note;

import java.util.List;

/**
 * Created by root on 9/6/18.
 */
public interface NoteRepository {
    List<Note> findAll();

}
