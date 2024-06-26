package ru.manakas.study.java_bot.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.manakas.study.java_bot.Model.JokesCalls;
import ru.manakas.study.java_bot.Model.JokesCount;
import ru.manakas.study.java_bot.Model.ModelJokes;
import ru.manakas.study.java_bot.Repository.RepositoryJokes;
import ru.manakas.study.java_bot.Repository.RepositoryCalls;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ServiceJokesImpl implements ServiceJokes {
    private final RepositoryJokes jokesJokeRepository;
    private final RepositoryCalls callsRepository;

    @Override
    public Optional<ModelJokes> createJoke(ModelJokes text){
        return Optional.of(jokesJokeRepository.save(text));
    }

    @Override
    public Page<ModelJokes> getAllJokes(int page){
        int size = 2;
        return jokesJokeRepository.findAll(PageRequest.of(page, size));
    }


    @Override
    public Optional<ModelJokes> getJokeById(Long id, Long userId) {
        Optional<ModelJokes> jokeOptional = jokesJokeRepository.findById(id);

        if (jokeOptional.isPresent()) {
            // Шутка найдена, создаем запись в JokeCalls
            JokesCalls call = new JokesCalls();
            call.setJoke(jokeOptional.get());
            call.setUserId(userId);
            callsRepository.save(call);
        }

        return jokeOptional;
    }

    @Override
    public void deleteJokeById(Long id) {
        jokesJokeRepository.deleteById(id);
    }

    @Override
    public Optional<ModelJokes> changeJokeById(Long id, ModelJokes text) {
        Optional<ModelJokes> jokeOptional = jokesJokeRepository.findById(id);
        jokeOptional.ifPresent(joke -> {
            joke.setText(text.getText());
            joke.setUpdatedDate(LocalDateTime.now());
            jokesJokeRepository.save(joke);
        });
        return jokeOptional;
    }


    @Override
    public List<JokesCount> getTop5Jokes() {
        return callsRepository.findTop5JokesByCalls();

    }

    @Override
    public Optional<ModelJokes> getRandomJoke() {
        return jokesJokeRepository.findRandomJoke();
    }
}