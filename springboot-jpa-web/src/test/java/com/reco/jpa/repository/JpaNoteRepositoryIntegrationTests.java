package com.reco.jpa.repository;

import com.reco.jpa.domain.Note;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by root on 9/6/18.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class JpaNoteRepositoryIntegrationTests {

    @Autowired
    JpaNoteRepository repository;

    @Test
    public void findsAllNotes() {
        List<Note> notes = this.repository.findAll();
        assertThat(notes).hasSize(4);
        for (Note note : notes) {
            assertThat(note.getTags().size()).isGreaterThan(0);
        }
    }

}
