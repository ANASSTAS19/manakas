package ru.manakas.study.java_bot.Service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.manakas.study.java_bot.Model.ModelJokes;
import ru.manakas.study.java_bot.Repository.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class ServiceJokesImpl implements ServiceJokes {
    private final Repository jokesRepository;

    @Override
    public Optional<ModelJokes> createJoke(ModelJokes text){
        return Optional.of(jokesRepository.save(text));
    }

    @Override
    public List<ModelJokes> getAllJokes(){
        return jokesRepository.findAll();
    }

    @Override
    public Optional<ModelJokes> getJokeById(Long id) {
        return jokesRepository.findById(id);
    }

    @Override
    public void deleteJokeById(Long id) {
        jokesRepository.deleteById(id);
    }

    @Override
    public Optional<ModelJokes> changeJokeById(Long id, ModelJokes text) {
        Optional<ModelJokes> jokeOptional = jokesRepository.findById(id);
        jokeOptional.ifPresent(joke -> {
            joke.setText(text.getText());
            joke.setUpdatedDate(LocalDateTime.now());
            jokesRepository.save(joke);
        });
        return jokeOptional;
    }
}
