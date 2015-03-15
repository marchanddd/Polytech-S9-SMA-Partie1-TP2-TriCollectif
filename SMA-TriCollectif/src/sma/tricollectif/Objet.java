package sma.tricollectif;

/**
 * Objets à trier
 */
public class Objet {
    
    private Character type;
    
    public Objet(Character type) {
        this.type = type;
    }
    
    /**
     * Utils
     */
    
    public boolean equals(Objet o) {
        return (o.getType().equals(type));
    }
    
    /**
     * Getter / Setter
     */

    public Character getType() {
        return type;
    }
}
