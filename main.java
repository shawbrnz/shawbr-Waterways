/**
 * Waterways project
 *
 * @Brendan Shaw
 * @v12 - 25/5
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
import javax.swing.*;
import javax.swing.JFrame;   
import java.awt.Dimension;
import java.awt.event.*;
import java.awt.*;
public class main extends JFrame implements ActionListener, MouseListener
{
    //Sets up text scanner               TEMP
    Scanner scanner = new Scanner(System.in);
    //Offset the grid is from the UI
    int xOffset=0;
    int yOffset=50;
    //Number of squares
    int xSize=10;
    int ySize=10;
    //Number of sides on a square
    int squareSides=4;
    //Size of squares
    int squareSize=100;
    //Array of the pipes. Third dimension is the edges
    pipeNode [][] pipesArray=new pipeNode[xSize][ySize];
    //Keeps program running             TEMP
    boolean keepRunning=true;
    //To ensure it only runs once if clicked             TEMP
    boolean currentLeftClick=false;
    boolean currentRightClick=false;
    public main()
    {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(xSize*squareSize+xOffset,ySize*squareSize+yOffset));
        Canvas myGraphic=new Canvas();
        panel.add(myGraphic);
        addMouseListener(this);
        this.setSize((xSize*squareSize)+xOffset,(ySize*squareSize)+yOffset);
        this.toFront(); 
        this.setVisible(true);
        repaint();
        for(int i=0;i<pipesArray.length;i++){
            for(int j=0;j<pipesArray[i].length;j++){
                pipesArray[i][j]=new pipeNode();
            }
        }
        for(int i=0;i<pipesArray.length;i++){
            for(int j=0;j<pipesArray[i].length;j++){
                System.out.println("Annoyed");
                int[] adjacentLocations=new int[squareSides];
                //Not using switch because they are bad. (They dont allow variables)
                pipeNode[] adjacentPipes={pipesArray[i-1][j],pipesArray[i][j+1],pipesArray[i+1][j],pipesArray[i][j-1]};
                pipesArray[i][j].setAdjacentPipeNode(adjacentPipes);
            }
        }
        while (keepRunning){
            //Old code for text menu
            /*System.out.println("Where do you want change?");
            String[] data=scanner.nextLine().split(" ");
            int[] pipeLocation=
            {Integer.valueOf(data[0]),
            Integer.valueOf(data[1]),
            Integer.valueOf(data[2])};
            pipesArray[pipeLocation[0]][pipeLocation[1]].swapPipe(pipeLocation[2]);
            for(int i=0;i<pipesArray.length;i++){
            for(int j=0;j<pipesArray[i].length;j++){
            for(int k=0;k<squareSides;k++){
            if(pipesArray[i][j].pipeThere(k)){
            System.out.print("i");
            }else{
            System.out.print(" ");
            }
            }
            System.out.print("|");
            }
            System.out.println("");
            }*/

        }
    }

    public void paint (Graphics g){
        super.paint(g);
        Graphics2D g2=(Graphics2D)g;
        int width = getWidth();
        int height = getHeight();
        //System.out.println("Updated");
        for(int i=0;i<pipesArray.length;i++){
            for(int j=0;j<pipesArray[i].length;j++){
                boolean squareHere=false;
                for(int k=0;k<squareSides;k++){
                    if(pipesArray[i][j].isWaterHere()){
                        ImageIcon pipeImage=new ImageIcon("DebugWater.png");
                        pipeImage.paintIcon(this,g,(i*squareSize)+xOffset,(j*squareSize)+yOffset);
                    }
                    if(squareHere){
                        if(pipesArray[i][j].pipeThere(k)){
                            ImageIcon pipeImage=new ImageIcon("pipe"+k+".png");
                            //System.out.println(k+","+i+","+j+",true");
                            pipeImage.paintIcon(this,g,(i*squareSize)+xOffset,(j*squareSize)+yOffset);
                        }else{
                            ImageIcon pipeImage=new ImageIcon("nopipe"+k+".png");
                            //System.out.println(k+","+i+","+j+",false");
                            pipeImage.paintIcon(this,g,(i*squareSize)+xOffset,(j*squareSize)+yOffset);
                        }
                    }else if (pipesArray[i][j].pipeThere(k)){
                        squareHere=true;
                        k=-1;
                    }
                }
            }
        }
        //Grid
        for(int i=0;i<=xSize;i++){
            g.drawLine(i*(squareSize)+xOffset, 0, i*(squareSize)+xOffset, ySize*squareSize+yOffset);
        }
        for(int i=0;i<=ySize;i++){
            g.drawLine(0, i*(squareSize)+yOffset, xSize*squareSize+xOffset, i*(squareSize)+yOffset);
        }
    }
    //These are required, dispite the fact that they are not used.
    public void actionPerformed(ActionEvent e){}

    public void mouseExited(MouseEvent e){}

    public void mouseEntered(MouseEvent e){}

    public void mouseReleased(MouseEvent e){
        if(e.getButton() == MouseEvent.BUTTON1){currentLeftClick=false;}
        else if(e.getButton() == MouseEvent.BUTTON3){currentRightClick=false;}
    }

    public void mousePressed(MouseEvent e){
        //Left click
        if(e.getButton() == MouseEvent.BUTTON1){
            System.out.println(e.getButton());
            if(!currentLeftClick){
                int xMouse=e.getX()-xOffset;
                int yMouse=e.getY()-yOffset;
                if(xMouse>0&&yMouse>0){
                    //System.out.println("True");
                    pipesArray[xMouse/squareSize][yMouse/squareSize].swapPipe(subSquares(yMouse%squareSize,xMouse%squareSize,squareSize));
                    //System.out.println(subSquares(yMouse%squareSize,xMouse%squareSize,squareSize));
                }
                JPanel panel = new JPanel();
                panel.setPreferredSize(new Dimension(1000,1000));
                Canvas myGraphic=new Canvas();
                panel.add(myGraphic);
                addMouseListener(this);
                this.setSize((xSize*squareSize)+xOffset,(ySize*squareSize)+yOffset);
                this.toFront(); 
                this.setVisible(true);
                repaint();
                currentLeftClick=true;
            }
        }
        //Right click
        else if (e.getButton() == MouseEvent.BUTTON3){
            System.out.println(e.getButton());
            if(!currentRightClick){
                int xMouse=e.getX()-xOffset;
                int yMouse=e.getY()-yOffset;
                if(xMouse>0&&yMouse>0&&pipesArray[xMouse/squareSize][yMouse/squareSize].pipeThere()){
                    pipesArray[xMouse/squareSize][yMouse/squareSize].flood();
                }
                JPanel panel = new JPanel();
                panel.setPreferredSize(new Dimension(1000,1000));
                Canvas myGraphic=new Canvas();
                panel.add(myGraphic);
                addMouseListener(this);
                this.setSize((xSize*squareSize)+xOffset,(ySize*squareSize)+yOffset);
                this.toFront(); 
                this.setVisible(true);
                repaint();
                currentRightClick=true;
            }
        }
    }

    //Not using this function as it is bad

    //Ok, why it is bad is because it runs mutliple times
    public void mouseClicked(MouseEvent e){}

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

        //Top left
        if(squareSize/2>=xClicked&&squareSize/2>=yClicked){
            if((xClicked>(yClicked))){
                return 0;
            }else{
                return 3;
            }
        }
        //Top right
        if(squareSize/2<xClicked&&squareSize/2>yClicked){
            if((xClicked-(squareSize/2)<(squareSize/2)-yClicked)){
                return 0;
            }else{
                return 1;
            }
        }
        //Bottom left
        if(squareSize/2>xClicked&&squareSize/2<yClicked){
            if((xClicked>-yClicked+2*(squareSize/2))){
                return 2;
            }else{
                return 3;
            }
        }
        //Bottom right
        if(squareSize/2<=xClicked&&squareSize/2<=yClicked){
            if((xClicked-(squareSize/2)>(yClicked-(squareSize/2)))){
                return 1;
            }else{
                return 2;
            }
        }
        //Should not run, but is required for java. Will be removed after cleaning
        return -1;
    }
    //Buttons are squares not triangles, so I have to make my own
    //Outdated
    boolean triangleHitBoxMethod(int yClicked, int xClicked){
        //Method to find if top or bottom was clicked
        //I don't care if it is clicked on the line because
        //user likely won't notice unless they go out of their 
        //way, does not break, just returns false, which is 1
        //pixel off of the area that is meant to return false
        return xClicked>yClicked;
    }
}
