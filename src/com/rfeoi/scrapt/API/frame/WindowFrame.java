package com.rfeoi.scrapt.API.frame;


import com.rfeoi.scrapt.API.Listener;
import com.rfeoi.scrapt.API.objects.Spirit;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class WindowFrame {
    private KeyListener keyListener;
    private MouseListener mouseListener;
    private MouseMotionListener mouseMotionListener;
    private Listener listener;
    private JFrame frame;
    private Image backgroundImage;
    public HashMap<String, Spirit> spirits;

    public WindowFrame(Dimension size, String title, Listener listener) {
        spirits = new HashMap<>();
        frame = new JFrame(title);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setSize(size);
        frame.repaint();
        this.listener = listener;
        setListener();
        new Thread(tick).start();
    }

    private void setListener() {
        this.keyListener = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {
                listener.keyTyped(keyEvent);
            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {
                listener.keyPressed(keyEvent);
            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {
                listener.keyReleased(keyEvent);
            }
        };
        frame.addKeyListener(this.keyListener);
        this.mouseListener = new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                listener.mouseClicked(mouseEvent);
            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                listener.mousePressed(mouseEvent);
            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {
                listener.mouseReleased(mouseEvent);
            }

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {
                listener.mouseEntered(mouseEvent);
            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {
                listener.mouseExited(mouseEvent);
            }
        };
        frame.addMouseListener(this.mouseListener);
        this.mouseMotionListener = new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent mouseEvent) {
                listener.mouseDragged(mouseEvent);
            }

            @Override
            public void mouseMoved(MouseEvent mouseEvent) {
                listener.mouseMoved(mouseEvent);
            }
        };
        frame.addMouseMotionListener(mouseMotionListener);
    }

    private Runnable tick = () -> {
        while(true){
            refresh();
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };

    //METHODS (for user)

    public void setTitle(String name){
        frame.setTitle(name);
    }
    public String getTitle(){
        return frame.getTitle();
    }
    public boolean touches(String firstSpirit, String secondSpirit){
        for(int x=0-spirits.get(firstSpirit).getWidth()/2; x<=spirits.get(firstSpirit).getWidth()/2; x++){
            for(int y= 0-spirits.get(firstSpirit).getHeight()/2; y<=spirits.get(firstSpirit).getHeight()/2; y++){
                if((spirits.get(firstSpirit).getLocationX() + x) == spirits.get(secondSpirit).getLocationX() && (spirits.get(firstSpirit).getLocationY() + y) == spirits.get(secondSpirit).getLocationY()){
                    return true;
                }
            }
        }
        return false;
    }

    public void setBackgroundImage(File file) throws IOException {
        backgroundImage = ImageIO.read(file);
    }

    public int getMouseX() {
        try {
            return (int) frame.getMousePosition().getX();
        } catch (NullPointerException ignored) {

        }
        return 0;
    }

    public int getMouseY() {
        try {
            return (int) frame.getMousePosition().getY();
        } catch (NullPointerException ingnored) {
        }
        return 0;
    }

    public void stopAtWall(String spiritname) {
        if (spirits.get(spiritname).getLocationX() <= 0) {
            spirits.get(spiritname).setLocationX(0);
        } else if (spirits.get(spiritname).getLocationX() > (frame.getWidth() - spirits.get(spiritname).getWidth())) {
            spirits.get(spiritname).setLocationX(frame.getWidth() - spirits.get(spiritname).getWidth());
        }
        if (spirits.get(spiritname).getLocationY() < spirits.get(spiritname).getHeight() / 2) {
            spirits.get(spiritname).setLocationY(spirits.get(spiritname).getHeight() / 2);
        } else if (spirits.get(spiritname).getLocationY() > frame.getHeight() - spirits.get(spiritname).getHeight()) {
            spirits.get(spiritname).setLocationY(frame.getHeight() - spirits.get(spiritname).getHeight());
        }
    }

    private void refresh() {
        new Thread(refreshRunnable).start();
    }

    private Runnable refreshRunnable = () -> {
        frame.getGraphics().clearRect(0, 0, (int) frame.getSize().getWidth(), (int) frame.getSize().getHeight());
        if (backgroundImage != null) {
            frame.getGraphics().drawImage(backgroundImage, 0, 0, frame.getWidth(), frame.getHeight(), null);
        }
        if (!spirits.isEmpty()) {
            for (Spirit spirit : spirits.values()) {
                if (spirit.isVisible()){
                    frame.getGraphics().drawImage(spirit.getUsedImage(), spirit.getLocationX(), spirit.getLocationY(), spirit.getWidth(), spirit.getHeight(), null);
                }
            }
        }
    };
}
