
package sma.tricollectif;

import java.util.ArrayList;
import java.util.Iterator;
import util.Coordonnees;

/**
 *
 * @author Epulapp
 */
public class Agent extends Thread {
    
    private String nom;
    
    private Coordonnees position; // X ordonnée / Y abcisse en commencant par 0 en haut à gauche
    private Grille grille; //grille
    
    private ArrayList<Agent> listAgents;
   
    
    public Agent(Coordonnees p,Grille g, Coordonnees pf, String n){
        nom = n;
        position = p;
        grille = g;
    }
    
    // TODO gestion de gains et de coûts
    // 
    // Nb couts mini des agents ayant atteint la position 
   
    public void run(){
        while (! grille.isTousSatisfait()) { //tant que le tri n'est pas satisfaisant on boucle 
            
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }//run()

    
    /**
     * Etats
     */
    
    public boolean isSatisfait() {
        return false;
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

    public ArrayList<Agent> getListAgents() {
        return listAgents;
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

    public void setListAgents(ArrayList<Agent> listAgents) {
        this.listAgents = listAgents;
    }

    
}
