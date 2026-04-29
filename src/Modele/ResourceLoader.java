package Modele;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class ResourceLoader {
    // On utilise cette classe comme point de référence pour le JAR
    public static Image load(String path) throws IOException {
        // Le "/" au début est vital : il repart de la racine du JAR
        URL url = ResourceLoader.class.getResource(path);
        if (url == null) {
            System.err.println("ERREUR : Impossible de trouver : " + path);
            return null;
        }
        return ImageIO.read(Objects.requireNonNull(ResourceLoader.class.getResource(path)));
    }

    public static BufferedImage load(String path, int a) throws IOException {
        // Le "/" au début est vital : il repart de la racine du JAR
        URL url = ResourceLoader.class.getResource(path);
        if (url == null) {
            System.err.println("ERREUR : Impossible de trouver : " + path);
            return null;
        }
        return ImageIO.read(Objects.requireNonNull(ResourceLoader.class.getResource(path)));
    }
}