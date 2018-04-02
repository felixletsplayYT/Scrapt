package com.rfeoi.scrapt.API;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public interface Listener {
    void mouseDragged(MouseEvent mouseEvent);
    void mouseMoved(MouseEvent mouseEvent);
    void mouseClicked(MouseEvent mouseEvent);
    void mousePressed(MouseEvent mouseEvent);
    void mouseReleased(MouseEvent mouseEvent);
    void mouseEntered(MouseEvent mouseEvent);
    void mouseExited(MouseEvent mouseEvent);
    void keyTyped(KeyEvent keyEvent);
    void keyPressed(KeyEvent keyEvent);
    void keyReleased(KeyEvent keyEvent);
}
