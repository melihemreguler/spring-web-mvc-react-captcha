package tr.edu.duzce.mf.bm.bm470captcha.entity;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "captcha")
public class Captcha {

    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    @Lob
    @Column(name = "image", nullable = false)
    private byte[] image;

    @Getter
    @Setter
    @Column(name = "text_value", nullable = false, length = 100)
    private String textValue;

}
