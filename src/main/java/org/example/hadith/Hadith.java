package org.example.hadith;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "hadiths")
public class Hadith {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name = "id")
    int id;

    @Column(name = "arabic_text")
    String arabic_text;

    @Column(name = "kazakh_text")
    String kazakh_text;

    @Column(name = "source")
    String source;
}
