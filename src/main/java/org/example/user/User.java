package org.example.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Data
@Builder
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    @Id
    private Long telegramId;

    private String username;
    private LocalDate registeredAt;

    @PrePersist
    public void onCreate() {
        this.registeredAt = LocalDate.now(); // автоматически устанавливается при первом сохранении
    }
}
