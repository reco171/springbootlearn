package com.reco.jpa.repository;

import com.reco.jpa.domain.Tag;

import java.util.List;

/**
 * Created by root on 9/6/18.
 */
public interface TagRepository {
    List<Tag> findAll();
}
