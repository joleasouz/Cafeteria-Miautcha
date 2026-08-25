package view.gerenciarFontes;

import java.awt.Font;

public class Fontes {
    private static Font aclonica;

    public static Font get(float tamanho) {
        if (aclonica == null) {
            try {
                aclonica = Font.createFont(Font.TRUETYPE_FONT, 
                    Fontes.class.getResourceAsStream("/Aclonica.ttf"));
            } catch (Exception e) {
                aclonica = new Font("SansSerif", Font.PLAIN, 12);
            }
        }
        return aclonica.deriveFont(tamanho);
    }
}