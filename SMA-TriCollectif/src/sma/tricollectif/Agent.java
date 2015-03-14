
package sma.tricollectif;

import java.util.ArrayList;
import java.util.LinkedList;
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
    }
    
    
    public void run(){
        while (true) { //tant que le tri n'est pas satisfaisant on boucle 
            try {
                
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                System.out.println(ex.getMessage());
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
            if (o.getClass().getName().equals("Objet")) {
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
    
    public void actions() {
        if (prise == null) {// prise
            
        } else {// depot
            
        }
        
    }
    
    
    /**
     * Utils
     */
    
    public ArrayList<Coordonnees> getSurroundingCoords() {
        ArrayList<Coordonnees> coords = new ArrayList<Coordonnees>();
        coords.add(new Coordonnees(position.getX()-1,position.getY()));//Le haut
        coords.add(new Coordonnees(position.getX()+1,position.getY()));//Le bas
        coords.add(new Coordonnees(position.getX(),position.getY()-1));//La gauche
        coords.add(new Coordonnees(position.getX(),position.getY()+1));//La droite
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
