package tr.edu.duzce.mf.bm.bm470captcha.entity;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "captcha")
public class Captcha {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(name = "image", nullable = false)
    private byte[] image;  // BLOB olarak saklanacak resim

    @Column(name = "text_value", nullable = false, length = 100)
    private String textValue;
}
