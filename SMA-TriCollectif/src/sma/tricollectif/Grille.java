package sma.tricollectif;

import java.util.ArrayList;
import util.Coordonnees;

/**
 *
 * @author Epulapp
 */
public class Grille {
    
    private Object[][] grille; // Contenant soit Objet, Agent ou null
    private ArrayList<Agent> listAgents;
    
    // Etat
    private boolean tousSatisait;

    public Grille(int taille, ArrayList<Agent> la){
        // Grille
        grille = new Agent[taille][taille];
        for(int i=0;i<taille;i++){
            for(int j=0;j<taille;j++){
                grille[i][j]=null;
            }
        }
        
        // Liste des agents
        if(la != null){
            listAgents = la;
            for(Agent a : la){
                if (grille[a.getPosition().getX()][a.getPosition().getY()] == null) {
                    grille[a.getPosition().getX()][a.getPosition().getY()] = a;
                }
            }
        }else{
            listAgents = new ArrayList<Agent>();
        }
        
        
    }//Grille()
    
    
    /**
     * Fait bouger un agent sur une case adjacente
     * @param a
     * @param cible
     * @return Agent a
     */
    public Agent moveAgent(Agent a, Coordonnees cible) {
        // Verifications
        if (isLibre(cible)) {
            // Modification de la position
            synchronized(grille) {
                grille[a.getPosition().getX()][a.getPosition().getY()] = null;
                grille[cible.getX()][cible.getY()] = a;
            }
            a.setPosition(cible);
        }
        
        return a;
    }//moveAgent()
    
    /**
     * Verifie que tous les agents soient satisfaits
     */
//    public boolean isTousSatisfait() {
//        tousSatisait = true;
//        for (Agent a : listAgents) {
//            if (! a.isSatisfait()) {
//                tousSatisait = false;
//                break;
//            }
//        }
//        return tousSatisait;
//    }
    
    public boolean isLibre(Coordonnees c){
        synchronized(grille) {
            return(grille[c.getX()][c.getY()] == null);
        }
    }
    
    public Object getCase(Coordonnees c){
        synchronized(grille) {
            if (c.getX() < 0 || c.getX() >= grille.length ||
                    c.getY() < 0 || c.getY() > grille[0].length) {
                return null;
            }
            return grille[c.getX()][c.getY()];
        }
    }
     
    public synchronized void print(){
        String ligne="";
        
        for(int i = 0; i < grille.length; i++){
            for(int j = 0; j < grille[i].length; j++){
                synchronized(grille) {
                    ligne += "[";
                    if(grille[i][j] != null){
                        if (grille[i][j].getClass().getName().equals("Agent")) {
                            ligne += ((Agent) grille[i][j]).getNom();
                        } else if (grille[i][j].getClass().getName().equals("Objet")) {
                            ligne += ((Objet) grille[i][j]).getType();
                        }
                    }
                    ligne+=" ]";
                }
            }
            System.out.println(ligne);
            ligne ="";
        }
        
        System.out.println("_____________________________________");
    }//print()
    
    public int getTaille(){
        return grille.length;
    }
    
    public ArrayList<Agent> getListAgents(){
        return listAgents;
    }
    
    
}
