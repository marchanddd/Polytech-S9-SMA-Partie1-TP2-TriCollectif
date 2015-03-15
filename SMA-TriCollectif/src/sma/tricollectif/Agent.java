
package sma.tricollectif;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.Coordonnees;

/**
 *
 * @author Epulapp
 */
public class Agent extends Thread {
    
    private String nom;
    private Coordonnees position; // X ordonnée / Y abcisse en commencant par 0 en haut à gauche
    private Grille grille; //grille
    
    // Parametres
    private final int i; // nb mouvements aleatoires
    private final int t; // taille memoire
    private final float kp;
    private final float km;
    
    // Etats
    private int iCurrent; // nb de mouvement courrent
    private Objet prise;
    
    // Perception
    private LinkedList<Character> memoire;
    
    public Agent(String n, Grille g, Coordonnees p, int i, int t, float kp, float km){
        nom = n;
        position = p;
        grille = g;
        this.i = i;
        this.t = t;
        this.kp = kp;
        this.km = km;
        this.memoire = new LinkedList<Character>();
        prise = null;
        this.iCurrent = 0;
    }
    
    
    public void run(){
        while (true) { //tant que le tri n'est pas satisfaisant on boucle 
            try {
                perception();
                action();
            } catch (Exception ex) {
                Logger.getLogger(Agent.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//run()
    
    /**
     * Perceptions
     */

    public void perception() {
        viewSurrounding();
    }
    
    /**
     * Enregistre en memoire les Objets alentours
     */
    private void viewSurrounding() {
        ArrayList<Coordonnees> coords = getSurroundingCoords();
        for (Coordonnees c : coords) {
            Object o = grille.getCase(c);
            if (o != null && o.getClass().getName().equals("sma.tricollectif.Objet")) {
                memoire.addFirst(((Objet) o).getType());
            } else {
                memoire.addFirst(null);
            }
        }
        // suppression des donnees en trops
        while (memoire.size() > t) {
            memoire.removeLast();
        }
    }//viewSurrounding()
    
    /**
     * Etats
     */
    
    public boolean isSatisfait() {
        return false;
    }
    
    /**
     * Actions
     */
    
    public void action() throws Exception {
        if (prise == null) {// recherche
            if (iCurrent < i) {
                bougerAleatoirement();
                iCurrent++;
            } else {
                prise();
                if (prise == null) { // pas pris
                    bougerAleatoirement();
                }
            }
        } else {// depot
            pose();
            if (prise != null) { // pas pose
                bougerAleatoirement();
            }
        }
        
    }//action()
    
    public void prise() throws Exception {
        if (prise != null) throw new Exception("L'agent à déjà une prise");
        // count
        HashMap<Character, Integer> countersObjet = new HashMap<Character, Integer>();
        for (Character type : memoire) {
            if (type != null) {
                if (countersObjet.containsKey(type)) {
                    countersObjet.put(type, countersObjet.get(type) + 1);
                } else {
                    countersObjet.put(type, 1);
                }
            }
        }
        // prise
        ArrayList<Coordonnees> surrounding = getSurroundingCoords();
        synchronized(grille) {
            for (Coordonnees c : surrounding) {
                Object o = grille.getCase(c);
                if (o != null && o.getClass().getName().equals("sma.tricollectif.Objet")) {
                    Objet obj = (Objet) o;
                    if (countersObjet.containsKey(obj.getType())) {
                        //proba
                        double PPrise = Math.pow(kp/(kp+(countersObjet.get(obj.getType())/(float)t)), 2); 
                        if (Math.random() <= PPrise) {
                            // prise
                            grille.removeObjet(c);
                            prise = obj;
                            return;
                        }
                    }
                }
            }
        }
    }//prise()
    
    public void pose() throws Exception {
        // count
        int counterObjet = 0;
        ArrayList<Coordonnees> surrounding = getSurroundingCoords();
        ArrayList<Coordonnees> casesPossible = new ArrayList<Coordonnees>();
        synchronized(grille) {
            for (Coordonnees c : surrounding) {
                Object o = grille.getCase(c);
                if (o == null) {
                    casesPossible.add(c);
                } else if (o.getClass().getName().equals("sma.tricollectif.Objet") &&
                        prise.equals((Objet) o)) {
                    counterObjet++;
                }
            }
            // pose
            if (counterObjet > 0) {
                for (Coordonnees c : casesPossible) {
                    //proba
                    double PPose = Math.pow(km/(km+((float) counterObjet)/t), 2); 
                    if (Math.random() <= PPose) {
                        // prise
                        grille.putObjet(c, prise);
                        prise = null;
                        return;
                    }
                }
            }
        }
    }//pose()
    
    /**
     * Utils
     */
    
    public ArrayList<Coordonnees> getSurroundingCoords() {
        ArrayList<Coordonnees> coords = new ArrayList<Coordonnees>();
        coords.add(new Coordonnees(position.getX()-1,position.getY()));//Le haut
        coords.add(new Coordonnees(position.getX()+1,position.getY()));//Le bas
        coords.add(new Coordonnees(position.getX(),position.getY()-1));//La gauche
        coords.add(new Coordonnees(position.getX(),position.getY()+1));//La droite
        Iterator<Coordonnees> itC = coords.iterator();
        while (itC.hasNext()) {
            if (! grille.isInBounds(itC.next())) {
                itC.remove();
            }
        }
        return coords;
    }
    
    public void bougerAleatoirement(){
        Coordonnees newPosition = new Coordonnees(position.getX(),position.getY());
        ArrayList<Coordonnees> casesPossible = new ArrayList<Coordonnees>();
        Coordonnees haut = new Coordonnees(position.getX()-1,position.getY());//Le haut
        Coordonnees bas = new Coordonnees(position.getX()+1,position.getY());//Le bas
        Coordonnees gauche = new Coordonnees(position.getX(),position.getY()-1);//La gauche
        Coordonnees droite = new Coordonnees(position.getX(),position.getY()+1);//La droite
        //Vérifier haut
        if(position.getX() > 0){
            if(grille.isLibre(haut)){
                casesPossible.add(haut);
            }
        }
        //Vérifier bas
        if(position.getX() < grille.getTaille()-1){
            if(grille.isLibre(bas)){
                casesPossible.add(bas);
            }
        }
         //Vérifier gauche
        if(position.getY() > 0){
            if(grille.isLibre(gauche)){
                casesPossible.add(gauche);
            }
        }
        //Vérifier droite
        if(position.getY() < grille.getTaille()-1){
            if(grille.isLibre(droite)){
                casesPossible.add(droite);
            }
        }
        if (! casesPossible.isEmpty()) {
            if(casesPossible.size() == 1) {
                newPosition = casesPossible.get(0);
            } else {
                newPosition = casesPossible.get((int)(Math.random()*casesPossible.size()));
            }
            grille.moveAgent(this, newPosition);
        }
    }//bougerAleatoirement()
    
    
    /**
     * Getters / Setters
     */
    
    public Coordonnees getPosition() {
        return position;
    }

    public Grille getGrille() {
        return grille;
    }

    public String getNom(){
        return nom;
    }
    
    public void setPosition(Coordonnees position) {
        this.position = position;
    }

    public void setGrille(Grille grille) {
        this.grille = grille;
    }
    
    public void setNom(String n){
        this.nom = n;
    }
    
}
