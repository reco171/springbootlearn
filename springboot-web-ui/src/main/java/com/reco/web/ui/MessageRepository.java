package com.reco.web.ui;

/**
 * Created by root on 9/6/18.
 */
public interface MessageRepository {

    Iterable<Message> findAll();

    Message save(Message message);

    Message findMessage(Long id);

    void deleteMessage(Long id);

}
