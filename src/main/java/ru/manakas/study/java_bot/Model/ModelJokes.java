package ru.manakas.study.java_bot.Model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity(name = "jokes")
@Table(name = "jokes")
public class ModelJokes {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "текст шутки")
    private String text;

    @Column(name = "дата создания")
    @CreationTimestamp
    private LocalDateTime createdDate;

    @Column(name = "дата обновления")
    @UpdateTimestamp
    private LocalDateTime updatedDate;
}

