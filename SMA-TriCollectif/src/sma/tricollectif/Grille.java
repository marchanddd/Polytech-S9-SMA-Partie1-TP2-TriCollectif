package sma.tricollectif;

import java.util.ArrayList;
import util.Coordonnees;

/**
 *
 * @author Epulapp
 */
public class Grille {
    
    private int taille;
    private Agent[][] grille;
    private ArrayList<Agent> listAgents;
    
    // Etat
    private boolean tousSatisait;

    public Grille(int t,ArrayList<Agent> la){
        taille = t;
        listAgents = la;
        grille = new Agent[t][t];
        for(int i=0;i<t;i++){
            for(int j=0;j<t;j++){
                grille[i][j]=null;
            }
        }
        
        if(la != null){
            for(Agent a : la){
                if (grille[a.getPosition().getX()][a.getPosition().getY()] == null) {
                    grille[a.getPosition().getX()][a.getPosition().getY()] = a;
                }
            }
            
            isTousSatisfait();
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
        // TODO AMELIORATION verifications
        if (isLibre(cible)) {
            // Modification de la position
            synchronized(grille) {
                grille[a.getPosition().getX()][a.getPosition().getY()] = null;
                grille[cible.getX()][cible.getY()] = a;
            }
            a.setPosition(cible);
        }
        
        return a;
    }
    
    /**
     * Verifie que tous les agents soient satisfaits
     */
    public boolean isTousSatisfait() {
        tousSatisait = true;
        for (Agent a : listAgents) {
            if (! a.isSatisfait()) {
                tousSatisait = false;
                break;
            }
        }
        return tousSatisait;
    }
    
    public boolean isLibre(Coordonnees c){
        synchronized(grille) {
            return(grille[c.getX()][c.getY()] == null);
        }
    }
    
    public Agent getCase(Coordonnees c){
        synchronized(grille) {
            return grille[c.getX()][c.getY()];
        }
    }
     
    public synchronized void print(){
        System.out.println("_____________________________________");
        String ligne="";
        
        for(int i=0;i<taille;i++){
            for(int j=0;j<taille;j++){
                synchronized(grille) {
                    if(grille[i][j] !=null){
                        ligne+="["+grille[i][j].getNom()+"]";
                    }else{
                       ligne+="[ ]";
                    }
//                    if(grille[i][j] !=null){
//                        ligne+=grille[i][j].getNom();
//                    }else{
//                       ligne+="-";
//                    }
                }
            }
            System.out.println(ligne);
            ligne ="";
        }
        
        System.out.println("_____________________________________");
        
    }
    
    public int getTaille(){
        return taille;
    }
    
    public ArrayList<Agent> getListAgents(){
        return listAgents;
    }
    
    
}
