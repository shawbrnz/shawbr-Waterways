
/**
 * Hitbox code for tiles
 *
 * @Brendan Shaw
 * @v3 28/4
 */
import java.awt.event.*;
import javax.swing.JFrame;  
public class hitbox4Corner extends JFrame// implements ActionListener
{
    public hitbox4Corner(int x,int y)
    {
        // initialise instance variables
        
    }
    public void mouseExited(MouseEvent e){}
    public void mouseEntered(MouseEvent e){}
    public void mouseReleased(MouseEvent e){}
    public void mousePressed(MouseEvent e){}
    public void mouseClicked(MouseEvent e){
        int xMouse=e.getX();
        int yMouse=e.getY();
        //if(((xMouse>xBox)&&(xMouse<(xBox+xSizeBox)))&&((yMouse>yBox)&&(yMouse<(yBox+ySizeBox)))){
        //    createDialog("something witty","Better", 50,50);
        //}
    }/*
    public void actionPerformed(ActionEvent e){
        if (e.getSource()==myButton1){
            System.out.println("First button");
        }else{
            System.out.println("Unknown button clicked");
        }
    }
    //Returns the side of the square that is clicked, 0 being top, going clockwise, left being 3
    int subSquares(int yClicked, int xClicked, int squareSize){
        //Top right
        if(squareSize/2>xClicked&&squareSize/2>yClicked){}
        //Top left
        if(squareSize/2<xClicked&&squareSize/2>yClicked){}
        //Bottom right
        if(squareSize/2>xClicked&&squareSize/2<yClicked){}
        //Bottom left
        if(squareSize/2<xClicked&&squareSize/2<yClicked){}
    }*/
    //Buttons are squares not triangles, so I have to make my own
    //There are two because of the two differnent types of triangle
    boolean triangleHitBoxMethodAcending(int yClicked, int xClicked){
        //Method to find if top left or bottom right was clicked
        return (yClicked/xClicked)>1;
    }
    boolean triangleHitBoxMethodDecending(int yClicked, int xClicked, int triangleSize){
        //Method to find if bottom left or top right was clicked
        return ((triangleSize-yClicked)/xClicked)>1;
    }
}
