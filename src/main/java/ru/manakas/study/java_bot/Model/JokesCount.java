package ru.manakas.study.java_bot.Model;

import java.time.LocalDateTime;

public class JokesCount extends ModelJokes{
    public JokesCount(Long id, String text, LocalDateTime createdDate, LocalDateTime updatedDate, Long count){
        super(id, text, createdDate, updatedDate);
        this.count = count;
    }
    public long count;
}
