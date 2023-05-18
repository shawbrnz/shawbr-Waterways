/**
 * Waterways project
 *
 * @Brendan Shaw
 * @v9 - 18/5
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
public class main extends JFrame implements ActionListener, MouseListener, KeyListener
{
    //Sets up text scanner               TEMP
    Scanner scanner = new Scanner(System.in);
    //Offset the grid is from the UI
    int xOffset=0;
    int yOffset=10;
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
    boolean currentClick=false;
    public main()
    {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(1000,1000));
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
        System.out.println("Updated");
        for(int i=0;i<pipesArray.length;i++){
            for(int j=0;j<pipesArray[i].length;j++){
                boolean squareHere=false;
                for(int k=0;k<squareSides;k++){
                    if(squareHere){
                        if(pipesArray[i][j].pipeThere(k)){
                            ImageIcon pipeImage=new ImageIcon("pipe"+k+".png");
                            System.out.println(k+","+i+","+j+",true");
                            pipeImage.paintIcon(this,g,(i*squareSize)+xOffset,(j*squareSize)+yOffset);
                        }else{
                            ImageIcon pipeImage=new ImageIcon("nopipe"+k+".png");
                            System.out.println(k+","+i+","+j+",false");
                            pipeImage.paintIcon(this,g,(i*squareSize)+xOffset,(j*squareSize)+yOffset);
                        }
                    }else if (pipesArray[i][j].pipeThere(k)){
                        squareHere=true;
                        k=-1;
                    }
                }
            }
        }
        //Debug lines
        int width = getWidth();
        int height = getHeight();
        for(int i=1;i<=20;i++){
            //Not my debug code
            g.drawLine(i*(50)+xOffset, 0, i*(50)+xOffset, height);
        }
        for(int i=1;i<=20;i++){
            //Not my debug code
            g.drawLine(0, i*(50)+yOffset, width, i*(50)+yOffset);
        }
    }

    public void keyPressed(KeyEvent e){    
        System.out.println(e.getKeyCode());
    } 
    //These are required, dispite the fact that they are not used.
    public void actionPerformed(ActionEvent e){}

    public void keyReleased(KeyEvent e){System.out.println(e.getKeyCode());}

    public void keyTyped(KeyEvent e){System.out.println(e.getKeyCode());}

    public void mouseExited(MouseEvent e){}

    public void mouseEntered(MouseEvent e){}

    public void mouseReleased(MouseEvent e){
        currentClick=false;
        System.out.println("Falsify");
    }

    public void mousePressed(MouseEvent e){
        if(!currentClick){
            int xMouse=e.getX()-xOffset;
            int yMouse=e.getY()-yOffset;
            if(xMouse>0&&yMouse>0){
                //System.out.println("True");
                pipesArray[xMouse/squareSize][yMouse/squareSize].swapPipe(subSquares(yMouse%squareSize,xMouse%squareSize,squareSize));
                System.out.println(subSquares(yMouse%squareSize,xMouse%squareSize,squareSize));

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
            currentClick=true;
        }
        else{System.out.println("Truifty");}
    }
    //Not using this function as it is bad
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
        System.out.println("subSquare");
        System.out.println(squareSize);
        //Top left
        if(squareSize/2>=xClicked&&squareSize/2>=yClicked){
            System.out.println("tl");
            System.out.println(xClicked);
            System.out.println(yClicked);
            if((xClicked>(yClicked))){
                System.out.println("0");
                return 0;
            }else{
                System.out.println("1");
                return 3;
            }
        }
        //Top right
        if(squareSize/2<xClicked&&squareSize/2>yClicked){
            System.out.println("tr");
            System.out.println(xClicked);
            System.out.println(yClicked);
            if((xClicked-(squareSize/2)<(squareSize/2)-yClicked)){
                System.out.println("0");
                return 0;
            }else{
                System.out.println("1");
                return 1;
            }
        }
        //Bottom left
        if(squareSize/2>xClicked&&squareSize/2<yClicked){
            System.out.println("bl");
            System.out.println(xClicked);
            System.out.println(yClicked);
            if((xClicked>-yClicked+2*(squareSize/2))){
                System.out.println("2");
                return 2;
            }else{
                System.out.println("3");
                return 3;
            }
        }
        //Bottom right
        if(squareSize/2<=xClicked&&squareSize/2<=yClicked){
            System.out.println("br");
            System.out.println(xClicked);
            System.out.println(yClicked);
            if((xClicked-(squareSize/2)>(yClicked-(squareSize/2)))){
                System.out.println("1");
                return 1;
            }else{
                System.out.println("2");
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
