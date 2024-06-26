package ru.manakas.study.java_bot.Service;

import org.springframework.data.domain.Page;
import ru.manakas.study.java_bot.Model.ModelJokes;
import ru.manakas.study.java_bot.Model.JokesCount;

import java.util.List;
import java.util.Optional;

public interface ServiceJokes {
    Optional<ModelJokes> createJoke(ModelJokes text);

    Page<ModelJokes> getAllJokes(int page);

    Optional<ModelJokes> getJokeById(Long id, Long userId);//

    void deleteJokeById(Long id);//

    Optional<ModelJokes> changeJokeById(Long id, ModelJokes text);///

    List<JokesCount> getTop5Jokes();

    Optional<ModelJokes> getRandomJoke();
}
