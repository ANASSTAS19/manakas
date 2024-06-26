package ru.manakas.study.java_bot.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.manakas.study.java_bot.Model.ModelJokes;

public interface Repository extends JpaRepository<ModelJokes, Long> {

}