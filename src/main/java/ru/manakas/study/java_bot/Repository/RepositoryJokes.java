package ru.manakas.study.java_bot.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.manakas.study.java_bot.Model.ModelJokes;

import java.util.Optional;

public interface RepositoryJokes extends PagingAndSortingRepository<ModelJokes, Long>, JpaRepository<ModelJokes, Long> {
    @Query("SELECT j FROM ModelJokes j ORDER BY RANDOM() LIMIT 1")
    Optional<ModelJokes> findRandomJoke();
}