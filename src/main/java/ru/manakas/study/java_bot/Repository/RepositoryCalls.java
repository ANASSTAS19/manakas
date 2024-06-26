package ru.manakas.study.java_bot.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.manakas.study.java_bot.Model.JokesCalls;
import ru.manakas.study.java_bot.Model.JokesCount;

import java.util.List;

public interface RepositoryCalls extends JpaRepository<JokesCalls, Long> {
    @Query("SELECT new ru.manakas.study.java_bot.Model.JokesCount(j.joke.id, j.joke.text, j.joke.createdDate, j.joke.updatedDate, COUNT(j)) " +
            "FROM JokesCalls j " +
            "GROUP BY j.joke.id, j.joke.text, j.joke.createdDate, j.joke.updatedDate " +
            "ORDER BY COUNT(j) DESC " +
            "LIMIT 5")
    List<JokesCount> findTop5JokesByCalls();
}
