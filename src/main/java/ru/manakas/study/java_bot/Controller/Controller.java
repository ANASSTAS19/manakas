package ru.manakas.study.java_bot.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.manakas.study.java_bot.Service.ServiceJokes;
import ru.manakas.study.java_bot.Model.ModelJokes;
import  ru.manakas.study.java_bot.Model.JokesCount;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/jokes")
@RequiredArgsConstructor
public class Controller {

    private final ServiceJokes jokeService;

    @PostMapping
    ResponseEntity<Void> createJoke(@RequestBody ModelJokes text){
        jokeService.createJoke(text);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Page<ModelJokes>> getAllJokes(@RequestParam int page){
        return ResponseEntity.ok(jokeService.getAllJokes(page));
    }

    @GetMapping("/{id}")
    ResponseEntity<ModelJokes> getJokeById(@PathVariable Long id, @RequestParam("userId") Long userId){
        return jokeService.getJokeById(id, userId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    ResponseEntity<ModelJokes> changeJoke(@PathVariable Long id, @RequestBody ModelJokes textJoke){
        Optional<ModelJokes> changedJoke = jokeService.changeJokeById(id, textJoke);
        return changedJoke.map(ResponseEntity::ok).orElseGet(()->ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteJoke(@PathVariable Long id){
        jokeService.deleteJokeById(id);
        return ResponseEntity.ok().build();
    }

    //шутки топ-5
    @GetMapping("/top5")
    ResponseEntity<List<JokesCount>> getTop5Jokes() {
        return ResponseEntity.ok(jokeService.getTop5Jokes());
    }

    //рандомная шутка ха ха
    @GetMapping("/random")
    public ResponseEntity<ModelJokes> getRandomJoke() {
        return jokeService.getRandomJoke()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}