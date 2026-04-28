package Vue;

import java.awt.*;
import java.util.Objects;

import Controleur.ControleurSouris;
import Modele.Armes.Arme;
import Modele.Armes.Baton;
import Modele.Modele;

import static Modele.Constantes.IMAGE_TOP_JOUEUR;
import static Modele.Constantes.LARGEUR_TOP_JOUEUR_SOURCE;

/**
 * Gère l'affichage dynamique de l'arme du joueur.
 * Cette vue calcule l'orientation vers le curseur, affiche éventuellement
 * la zone d'attaque et applique les offsets produits par l'animation d'attaque.
 */
public class VueArme {

    /** ---------- [Constantes de rendu] ---------- **/

    public static final int TAILLE = 10;

    private static final int OFFSET_START_ARME = 10;
    private static final int OFFSET_Y_ARME = 12;
    private static final double RATIO_LARGEUR = 0.9;

    /** ---------- [Propriétés - Dépendances] ---------- **/

    private final ControleurSouris controleurSouris;
    private Modele modele;
    private Vue vue;

    /** ---------- [Propriétés - État d'affichage] ---------- **/

    private double angleOffsetAnimation = 0;
    private int translationOffsetAnimation = 0;
    private boolean enAnimation = false;
    private boolean affPortee = false;

    /** ---------- [Propriétés - Dimensions source] ---------- **/

    private int imgWidth;
    private int imgHeight;

    /** ---------- [Constructeurs] ---------- **/

    /**
     * Initialise la vue de l'arme et ses dépendances de rendu.
     *
     * @param controleurSouris - Contrôleur fournissant la position instantanée du curseur
     * @param vue - Vue principale utilisée comme repère écran
     * @param modele - Modèle donnant accès au joueur et à l'arme équipée
     */
    public VueArme(ControleurSouris controleurSouris, Vue vue, Modele modele) {
        this.controleurSouris = controleurSouris;
        this.modele = modele;
        this.vue = vue;
    }

    /** ---------- [Getters & Setters - État visuel] ---------- **/

    public boolean getAffPortee() {
        return affPortee;
    }

    public void setAffPortee(boolean b) {
        affPortee = b;
    }

    /**
     * Injecte l'offset angulaire calculé par l'animation d'attaque.
     *
     * @param offset - Rotation supplémentaire à appliquer localement à l'arme
     */
    public void setAngleOffsetAnimation(double offset) {
        this.angleOffsetAnimation = offset;
    }

    /**
     * Injecte la translation locale utilisée notamment pour la lance.
     *
     * @param offset - Déplacement supplémentaire sur l'axe local de l'arme
     */
    public void setTranslationOffsetAnimation(int offset) {
        this.translationOffsetAnimation = offset;
    }

    /**
     * Verrouille ou libère l'état d'animation de l'arme.
     *
     * @param b - true si une animation d'attaque est en cours
     */
    public void setEnAnimation(boolean b) {
        this.enAnimation = b;
    }

    public boolean getEnAnimation() {
        return enAnimation;
    }

    /** ---------- [Méthodes Publiques - Rendu] ---------- **/

    /**
     * Dessine l'arme équipée dans l'espace monde en fonction du curseur et de l'état courant.
     * Le rendu se fait dans un repère local centré sur le joueur puis orienté vers la souris,
     * ce qui simplifie à la fois la visée, l'animation et l'affichage des portées.
     *
     * @param g - Contexte graphique principal
     */
    public void dessiner(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();

        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        Arme armeEquipee = modele.getJoueur().getArmeEquipee();
        Image image = armeEquipee.getImage();
        int portee = armeEquipee.getPortee();
        double ouvertureCone = armeEquipee.getAngle();

        int posJoueurX = (int) modele.getJoueur().getX();
        int posJoueurY = (int) modele.getJoueur().getY();

        int centerX = vue.getWidth() / 2;
        int centerY = vue.getHeight() / 2;

        int mouseX = controleurSouris.getMX();
        int mouseY = controleurSouris.getMY();

        // L'angle est calculé depuis le centre écran, car le joueur y reste visuellement centré par la caméra.
        double angle = Math.atan2(mouseY - centerY, mouseX - centerX);
        double angleOffset = 0;

        int rayon = 20;

        g2d.translate(posJoueurX, posJoueurY);
        g2d.rotate(angle);

        if (affPortee) {
            // La lance utilise une zone rectiligne, les autres armes une zone conique.
            if (Objects.equals(armeEquipee.getNom(), "Lance")) {
                g2d.setColor(new Color(0, 150, 255, 60));
                g2d.fillRect(0, -portee / 8, (int) (portee * 1.2), portee / 4);

                g2d.setColor(new Color(0, 100, 255, 150));
                g2d.drawRect(0, -portee / 8, (int) (portee * 1.2), portee / 4);

            } else {
                g2d.setColor(new Color(0, 150, 255, 60));
                int arcAngle = (int) Math.toDegrees(ouvertureCone);

                // L'arc est recentré autour de la direction de visée pour que la souris pointe l'axe médian.
                int startAngle = -arcAngle / 2;

                g2d.fillArc(-portee, -portee, portee * 2, portee * 2, startAngle, arcAngle);

                g2d.setColor(new Color(0, 100, 255, 150));
                g2d.drawArc(-portee, -portee, portee * 2, portee * 2, startAngle, arcAngle);

                // Les deux rayons ferment visuellement le cône et rendent sa lecture plus nette.
                g2d.drawLine(0, 0, (int) (portee * Math.cos(Math.toRadians(startAngle))), (int) (portee * Math.sin(Math.toRadians(startAngle))));
                g2d.drawLine(0, 0, (int) (portee * Math.cos(Math.toRadians(startAngle + arcAngle))), (int) (portee * Math.sin(Math.toRadians(startAngle + arcAngle))));
            }
        }

        // Les offsets d'animation sont appliqués dans le repère local déjà orienté vers la cible.
        g2d.rotate(angleOffsetAnimation);
        g2d.translate(translationOffsetAnimation, 0);

        g2d.setColor(Color.GRAY);

        if (image == null) {
            // Rendu de secours si l'image d'arme n'est pas disponible.
            g2d.setColor(Color.BLACK);
            g2d.fillRect(rayon, -TAILLE / 2, portee - rayon, TAILLE);
        } else {
            // Le sprite est redimensionné à partir de la portée pour que la sensation de longueur reste cohérente.
            int scaledWidth = (int) (portee * RATIO_LARGEUR);
            int scaledHeight = (int) ((scaledWidth * image.getHeight(null)) / (double) image.getWidth(null));

            int posX = OFFSET_START_ARME;
            int posY = getPosY(armeEquipee);

            g2d.drawImage(image, posX, posY, scaledWidth, scaledHeight, null);
        }

        g2d.translate(-translationOffsetAnimation, 0);

        // Le haut du joueur est redessiné après l'arme pour préserver la superposition voulue de certains sprites.
        int offsetJoueur = -LARGEUR_TOP_JOUEUR_SOURCE / 2;
        g2d.drawImage(IMAGE_TOP_JOUEUR, offsetJoueur, offsetJoueur, 50, 50, null);

        g2d.dispose();
    }

    /** ---------- [Méthodes Privées - Ajustements de sprites] ---------- **/

    /**
     * Retourne l'offset vertical propre à l'arme équipée.
     * Ces ajustements compensent les différences de cadrage entre sprites afin
     * d'obtenir un point de tenue visuellement cohérent dans la main du joueur.
     *
     * @param armeEquipee - Arme actuellement affichée
     * @return position Y locale du sprite dans le repère de l'arme
     */
    private int getPosY(Arme armeEquipee) {
        int posY = -OFFSET_Y_ARME;

        if (Objects.equals(armeEquipee.getNom(), "Baton")) {
            posY = -5;
        }

        if (Objects.equals(armeEquipee.getNom(), "Epee")) {
            posY = -15;
        }

        if (Objects.equals(armeEquipee.getNom(), "Epee Lourde")) {
            posY = -25;
        }

        if (Objects.equals(armeEquipee.getNom(), "Lance")) {
            posY = -17;
        }

        return posY;
    }
}