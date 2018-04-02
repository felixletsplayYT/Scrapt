package com.rfeoi.scrapt.API.objects;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Spirit {
    private int x = 0;
    private int y = 0;
    private int width = 0;
    private int height = 0;
    private ArrayList<Image> costumes;
    private int usedCostume;
    private boolean visibility = false;
    public Spirit(File image, int width, int height) throws IOException {
        costumes = new ArrayList<>();
        costumes.add(ImageIO.read(image));
        this.width = width;
        this.height = height;
    }
    public void step(int steps){
        setLocationX(getLocationX() + steps);
    }
    public void changeXby(int i){
        step(i);
    }
    public void changeYby(int i){
        setLocationY(getLocationY() + i);
    }
    public int getLocationX(){
       return x;
    }
    public int getLocationY(){
      return y;
    }

    public void setLocationX(int x) {
        this.x = x;
    }

    public void setLocationY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Image getCostumeImage(int id){
        return costumes.get(id);
    }
    public Image getUsedImage(){
        return getCostumeImage(getCostume());
    }

    public int getCostume() {
        return usedCostume;
    }

    public void setCostume(int id){
        if (costumes.size() > id){
            usedCostume = id;
        }
    }
    public void addCostumeImage(File image) throws IOException {
        costumes.add(ImageIO.read(image));
    }
    public void hide(){
        visibility = false;
    }
    public void show(){
        visibility = true;
    }

    public boolean isVisible() {
        return visibility;
    }
}
