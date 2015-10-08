/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zebra.view;

import java.util.ArrayList;
import jpa.entities.*;
import zebra.Zebra;

/**
 *
 * @author aeugster
 */
public final class Model {
    
    private boolean zebraB; //True: List of Zebras; False: List of Ratings
    private ArrayList<Zebracrossing> zebras;
    private ArrayList<Rating> ratings;

    public Model() {
        zebraB = true;
        zebras = new ArrayList<>();
        ratings = new ArrayList<>();
        
        fetchData(null);
        
    }
    
    public void fetchData(Zebracrossing zebra){
        if(isZebraB()){
            setZebras(Zebra.getZebracrossings());
        }else{
            setRatings(Zebra.getRatingsOfZebra(zebra));
        }
    }

    /**
     * @return the zebras
     */
    public ArrayList<Zebracrossing> getZebras() {
        return zebras;
    }

    /**
     * @param zebras the zebras to set
     */
    public void setZebras(ArrayList<Zebracrossing> zebras) {
        this.zebras = zebras;
    }

    /**
     * @return the ratings
     */
    public ArrayList<Rating> getRatings() {
        return ratings;
    }

    /**
     * @param ratings the ratings to set
     */
    public void setRatings(ArrayList<Rating> ratings) {
        this.ratings = ratings;
    }

    /**
     * @return the zebraB
     */
    public boolean isZebraB() {
        return zebraB;
    }

    /**
     * @param zebraB the zebraB to set
     */
    public void setZebraB(boolean zebraB) {
        this.zebraB = zebraB;
    }   
}
