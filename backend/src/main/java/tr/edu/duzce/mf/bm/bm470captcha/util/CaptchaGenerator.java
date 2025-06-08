package tr.edu.duzce.mf.bm.bm470captcha.util;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Random;
import javax.imageio.ImageIO;

/**
 * Gelişmiş CAPTCHA üreticisi – harfleri döndürür, kaydırır, kaydırır ve
 * rastgele gürültü çizgileri ile noktalar ekleyerek çözülmesini zorlaştırır.
 */
public class CaptchaGenerator {

    private static final String CHAR_POOL = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    private static final Font[] FONTS = {
            new Font("Arial", Font.BOLD, 40),
            new Font("Courier", Font.BOLD, 40),
            new Font("Tahoma", Font.BOLD, 40),
            new Font("Verdana", Font.BOLD, 40)
    };

    private static final Random RANDOM = new Random();

    /**
     * İstenen uzunlukta rastgele metin üretir.
     */
    public static String generateRandomText(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(CHAR_POOL.charAt(RANDOM.nextInt(CHAR_POOL.length())));
        }
        return sb.toString();
    }

    /**
     * İstenen metin için gelişmiş CAPTCHA görseli üretir ve byte[] olarak döner.
     */
    public static byte[] generateCaptchaImageBytes(String text) {
        // Dinamik genişlik – her karakter için ~40px
        int width = 40 * text.length();
        int height = 70;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();

        // Pürüzsüz çizim
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Arkaplan
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);

        // Tüm resme hafif kesme (shear) uygulayarak "hareket" hissi kazandır
        double shearX = (RANDOM.nextDouble() - 0.5) * 0.3; // -0.15 .. 0.15
        double shearY = (RANDOM.nextDouble() - 0.5) * 0.3;
        g.shear(shearX, shearY);

        // Her karakteri rastgele döndür, renklendir ve biraz dikey kaydır
        int x = 10;
        for (char ch : text.toCharArray()) {
            int angle = RANDOM.nextInt(60) - 30; // -30 .. 30 derece
            AffineTransform old = g.getTransform();
            g.rotate(Math.toRadians(angle), x + 15, height / 2);

            g.setFont(FONTS[RANDOM.nextInt(FONTS.length)]);
            g.setColor(new Color(RANDOM.nextInt(150), RANDOM.nextInt(150), RANDOM.nextInt(150)));

            // Dikey konum +/-7px rastgele
            g.drawString(String.valueOf(ch), x, (height / 2) + (RANDOM.nextInt(15) - 7));

            g.setTransform(old);
            x += 35; // karakterler arası mesafe
        }

        // Rastgele gürültü çizgileri ekle
        for (int i = 0; i < 8; i++) {
            g.setColor(new Color(RANDOM.nextInt(255), RANDOM.nextInt(255), RANDOM.nextInt(255)));
            int x1 = RANDOM.nextInt(width);
            int y1 = RANDOM.nextInt(height);
            int x2 = RANDOM.nextInt(width);
            int y2 = RANDOM.nextInt(height);
            g.drawLine(x1, y1, x2, y2);
        }

        // Nokta/küçük daire gürültüsü ekle
        for (int i = 0; i < 80; i++) {
            g.setColor(new Color(RANDOM.nextInt(255), RANDOM.nextInt(255), RANDOM.nextInt(255), RANDOM.nextInt(255)));
            int cx = RANDOM.nextInt(width);
            int cy = RANDOM.nextInt(height);
            int r = RANDOM.nextInt(4) + 1;
            g.fillOval(cx, cy, r, r);
        }

        g.dispose();

        // Byte dizisine yaz
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(image, "png", baos);
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("CAPTCHA resmi oluşturulamadı", e);
        }
    }
}