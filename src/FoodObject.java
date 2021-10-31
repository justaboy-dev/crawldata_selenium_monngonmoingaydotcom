/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Admin
 */
public class FoodObject {
    public String name;
    public String id;
    public String description;
    public String ingredients;
    public String preprocessing;
    public String perform;
    public String eating;
    public String tip;
    public String levelOfDifficult;
    public String imageUrl;
    
    public FoodObject()
    {
        
    }
    public FoodObject(String name, String description,
            String ingredients, String preprocessing,
            String perform, String eating, String tip, String levelOfDifficult, String imageUrl,String id)
    {
        this.name = name;
        this.description = description;
        this.ingredients = ingredients;
        this.preprocessing = preprocessing;
        this.perform = perform;
        this.eating = eating;
        this.tip = tip;
        this.levelOfDifficult = levelOfDifficult;
        this.imageUrl = imageUrl;
        this.id = id;
    }
}
