package ru.manakas.study.java_bot;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.manakas.study.java_bot.Model.ModelJokes;
import ru.manakas.study.java_bot.Service.ServiceJokes;
import ru.manakas.study.java_bot.Repository.RepositoryJokes;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JokeServiceImplTest {

    @Mock
    private RepositoryJokes jokesJokeRepository;

    @InjectMocks
    private ServiceJokes jokeService;

    @Test
    void createJoke() {
        ModelJokes joke = new ModelJokes(null, "Новый анекдот", null, null);
        when(jokesJokeRepository.save(joke)).thenReturn(joke);

        Optional<ModelJokes> result = jokeService.createJoke(joke);

        assertTrue(result.isPresent());
        assertEquals(result.get(), joke);
        verify(jokesJokeRepository, times(1)).save(joke);
    }

    @Test
    void getJokeByIdNotFound() {
        when(jokesJokeRepository.findById(2L)).thenReturn(Optional.empty());

        Optional<ModelJokes> result = jokeService.getJokeById(2L, 3L);

        assertFalse(result.isPresent());
        verify(jokesJokeRepository, times(1)).findById(2L);
    }

    @Test
    void getJokeById() {
        ModelJokes joke = new ModelJokes(1L, "Анекдот 3", null, null);
        when(jokesJokeRepository.findById(1L)).thenReturn(Optional.of(joke));

        Optional<ModelJokes> result = jokeService.getJokeById(1L, 1L);

        assertTrue(result.isPresent());
        assertEquals(result.get(), joke);
        verify(jokesJokeRepository, times(1)).findById(1L);
    }

    @Test
    void deleteJokeById() {
        jokeService.deleteJokeById(1L);

        verify(jokesJokeRepository, times(1)).deleteById(1L);
    }

    @Test
    void changeJokeById() {
        ModelJokes existingJoke = new ModelJokes(1L, "Старый анекдот", null, null);
        ModelJokes updatedJoke = new ModelJokes(1L, "Изменённый анекдот", null, null);
        when(jokesJokeRepository.findById(1L)).thenReturn(Optional.of(existingJoke));
        when(jokesJokeRepository.save(existingJoke)).thenReturn(existingJoke);

        Optional<ModelJokes> result = jokeService.changeJokeById(1L, updatedJoke);

        assertTrue(result.isPresent());
        assertEquals(result.get(), existingJoke);
        assertEquals(result.get().getText(), updatedJoke.getText());
        verify(jokesJokeRepository, times(1)).findById(1L);
        verify(jokesJokeRepository, times(1)).save(existingJoke);
    }

    @Test
    void changeJokeByIdNotFound() {
        when(jokesJokeRepository.findById(2L)).thenReturn(Optional.empty());

        Optional<ModelJokes> result = jokeService.changeJokeById(2L, new ModelJokes(2L, "Новый анекдот", null, null));

        assertFalse(result.isPresent());
        verify(jokesJokeRepository, times(1)).findById(2L);
        verify(jokesJokeRepository, never()).save(any());
    }
}
