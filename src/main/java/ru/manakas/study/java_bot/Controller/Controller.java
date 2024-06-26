package ru.manakas.study.java_bot.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.manakas.study.java_bot.Model.ModelJokes;
import ru.manakas.study.java_bot.Service.ServiceJokes;

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
    ResponseEntity<List<ModelJokes>> getAllJokes(){
        return ResponseEntity.ok(jokeService.getAllJokes());
    }

    @GetMapping("/{id}")
    ResponseEntity<ModelJokes> getJokeById(@PathVariable Long id){
        return jokeService.getJokeById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
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
}

