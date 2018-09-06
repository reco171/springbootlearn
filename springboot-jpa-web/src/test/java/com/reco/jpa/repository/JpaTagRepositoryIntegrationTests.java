package com.reco.jpa.repository;

import com.reco.jpa.domain.Tag;
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
public class JpaTagRepositoryIntegrationTests {

    @Autowired
    JpaTagRepository repository;

    @Test
    public void findsAllTags() {
        List<Tag> tags = this.repository.findAll();
        assertThat(tags).hasSize(3);
        for (Tag tag : tags) {
            assertThat(tag.getNotes().size()).isGreaterThan(0);
        }
    }

}