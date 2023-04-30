
/**
 * Waterways project
 *
 * @Brendan Shaw
 * @v4 - 1/5
 * 
 * Need to:
 * 1- GUI,
 * CLEAN UP!
 * 2- Clean changing system,
 * 3- Water,
 * 4- File stuff
 */

//Importing
import java.util.Scanner;
import java.awt.*;
import javax.swing.*;
import javax.swing.JFrame;   
import java.awt.Dimension;
import java.awt.event.*;
public class main extends JFrame implements ActionListener, MouseListener
{
    //Sets up text scanner               TEMP
    Scanner scanner = new Scanner(System.in);
    //Offset the grid is from the UI
    int xOffset=0;
    int yOffset=10;
    //Number of squares
    int xSize=10;
    int ySize=10;
    //Size of squares
    int squareSize=10;
    //Array of the pipes. Third dimension is the edges
    boolean [][][] pipesArray=new boolean[xSize][ySize][4];
    //Keeps program running             TEMP
    boolean keepRunning=true;
    public main()
    {
        while (keepRunning){
            System.out.println("Where do you want change?");
            String[] data=scanner.nextLine().split(" ");
            int[] pipeLocation=
                {Integer.valueOf(data[0]),
                    Integer.valueOf(data[1]),
                    Integer.valueOf(data[2])};
            pipesArray[pipeLocation[0]][pipeLocation[1]][pipeLocation[2]]=
            !(pipesArray[pipeLocation[0]][pipeLocation[1]][pipeLocation[2]]);
            JPanel panel = new JPanel();
            panel.setPreferredSize(new Dimension(400,400));
            Canvas myGraphic=new Canvas();
            panel.add(myGraphic);
            this.pack();
            this.toFront(); 
            this.setVisible(true);
            for(int i=0;i<pipesArray.length;i++){
                for(int j=0;j<pipesArray[i].length;j++){
                    for(int k=0;k<pipesArray[i][j].length;k++){
                        if(pipesArray[i][j][k]){
                            System.out.print("i");
                        }else{
                            System.out.print(" ");
                        }
                    }
                    System.out.print("|");
                }
                System.out.println("");
            }
        }
    }

    public void actionPerformed(ActionEvent e){
        String itemClicked=e.getActionCommand();

    }

    public void mouseExited(MouseEvent e){}

    public void mouseEntered(MouseEvent e){}

    public void mouseReleased(MouseEvent e){}

    public void mousePressed(MouseEvent e){}

    public void mouseClicked(MouseEvent e){
        System.out.println(e);
        int xMouse=e.getX()-xOffset;
        int yMouse=e.getY()-yOffset;
        if(xMouse>0&&yMouse>0){
            //Need to clean
            pipesArray[xMouse/squareSize][yMouse/squareSize]
            [subSquares(yMouse%squareSize,xMouse%squareSize,squareSize)]=
            !(pipesArray[xMouse/squareSize][yMouse/squareSize]
            [subSquares(yMouse%squareSize,xMouse%squareSize,squareSize)]);
        }
    }

    /*    !
    boolean swap(boolean swapper){
    if(swapper){
    return false;
    }
    return true;
    }*/
    
    //Returns the side of the square that is clicked, 0 being top, going clockwise, left being 3
    int subSquares(int yClicked, int xClicked, int squareSize){
        //It is possible to make this slightly faster by splitting the left
        //and right up and only checking 1 and 3 once

        //TODO- Clean this

        //Top right
        if(squareSize/2>=xClicked&&squareSize/2>=yClicked){
            if(triangleHitBoxMethod(xClicked-(squareSize/2),-(yClicked))){
                return 1;
            }else{
                return 0;
            }
        }
        //Top left
        if(squareSize/2<xClicked&&squareSize/2>yClicked){
            if(triangleHitBoxMethod(xClicked,yClicked)){
                return 0;
            }else{
                return 3;
            }
        }
        //Bottom right
        if(squareSize/2>xClicked&&squareSize/2<yClicked){
            if(triangleHitBoxMethod(xClicked-(squareSize/2),yClicked-(squareSize/2))){
                return 1;
            }else{
                return 2;
            }
        }
        //Bottom left
        if(squareSize/2<=xClicked&&squareSize/2<=yClicked){
            if(triangleHitBoxMethod(xClicked,-(yClicked-(squareSize/2)))){
                return 2;
            }else{
                return 3;
            }
        }

        //Should not run, but is required for java. Will be removed after cleaning
        return -1;
    }
    //Buttons are squares not triangles, so I have to make my own
    boolean triangleHitBoxMethod(int yClicked, int xClicked){
        //Method to find if top or bottom was clicked
        //I don't care if it is clicked on the line because
        //user likely won't notice unless they go out of their 
        //way, does not break, just returns false, which is 1
        //pixel off of the area that is meant to return false
        return xClicked>yClicked;
    }
}
