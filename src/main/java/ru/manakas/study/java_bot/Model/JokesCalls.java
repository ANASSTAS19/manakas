package ru.manakas.study.java_bot.Model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "calls")
public class JokesCalls {

    @Id
    @GeneratedValue(generator = "call_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "call_id_seq", sequenceName = "call_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "time_call")
    @CreationTimestamp
    private LocalTime timeCall;

    @JoinColumn(name = "joke_id")
    @ManyToOne
    private ModelJokes joke;
}
