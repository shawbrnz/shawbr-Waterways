/**
 * Waterways project
 *
 * @Brendan Shaw
 * @v14 - 26/5
 * 
 * Need to:
 * 2- GUI,
 * 1- CLEAN UP!
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
    //Offset the grid is from the UI
    final int X_OFFSET=0;
    final int Y_OFFSET=50;
    //Number of sides on a square (No magic numbers)
    final int SQUARE_SIDES=4;
    
    //Initalising varibles. Note these are not finals as I plan for the user to be able to modify these
    //Size of squares
    int squareSize=100;
    //Number of squares
    int xSize=10;
    int ySize=10;
    //Array of the pipes. Third dimension is the edges
    pipeNode [][] pipesArray=new pipeNode[xSize][ySize];
    //To ensure it only runs once if clicked             TEMP
    boolean currentLeftClick=false;
    boolean currentRightClick=false;
    public main()
    {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(xSize*squareSize+X_OFFSET,ySize*squareSize+Y_OFFSET));
        Canvas myGraphic=new Canvas();
        panel.add(myGraphic);
        addMouseListener(this);
        this.setSize((xSize*squareSize)+X_OFFSET,(ySize*squareSize)+Y_OFFSET);
        this.toFront(); 
        this.setVisible(true);
        repaint();
        for(int i=0;i<pipesArray.length;i++){
            for(int j=0;j<pipesArray[i].length;j++){
                pipesArray[i][j]=new pipeNode();
                pipesArray[i][j].giveName((j*10)+i);
            }
        }
        for(int i=0;i<pipesArray.length;i++){
            for(int j=0;j<pipesArray[i].length;j++){
                System.out.println("Annoyed");
                int[] adjacentLocations=new int[SQUARE_SIDES];
                //Not using switch because they are bad. (They dont allow variables)
                //Edges loop around because it is much easier to code 
                if(i==0){
                    adjacentLocations[1]=xSize-1;
                    adjacentLocations[0]=i+1;
                }else if(i==xSize-1){
                    adjacentLocations[1]=i-1;
                    adjacentLocations[0]=0;
                }else{
                    adjacentLocations[1]=i-1;
                    adjacentLocations[0]=i+1;
                }
                //Same thing repeated for j
                if(j==0){
                    adjacentLocations[3]=ySize-1;
                    adjacentLocations[2]=j+1;
                }else if(j==ySize-1){
                    adjacentLocations[3]=j-1;
                    adjacentLocations[2]=0;
                }else{
                    adjacentLocations[3]=j-1;
                    adjacentLocations[2]=j+1;
                }
                pipeNode[] adjacentPipes={pipesArray[i][adjacentLocations[3]],pipesArray[adjacentLocations[0]][j],pipesArray[i][adjacentLocations[2]],pipesArray[adjacentLocations[1]][j]};
                pipesArray[i][j].setAdjacentPipeNode(adjacentPipes);
            }
        }
    }

    public void paint (Graphics g){
        super.paint(g);
        Graphics2D g2=(Graphics2D)g;
        int width = getWidth();
        int height = getHeight();
        for(int i=0;i<pipesArray.length;i++){
            for(int j=0;j<pipesArray[i].length;j++){
                boolean squareHere=false;
                for(int k=0;k<SQUARE_SIDES;k++){
                    if(pipesArray[i][j].isWaterHere()){
                        ImageIcon pipeImage=new ImageIcon("DebugWater.png");
                        pipeImage.paintIcon(this,g,(i*squareSize)+X_OFFSET,(j*squareSize)+Y_OFFSET);
                    }
                    if(squareHere){
                        if(pipesArray[i][j].pipeThere(k)){
                            ImageIcon pipeImage=new ImageIcon("pipe"+k+".png");
                            pipeImage.paintIcon(this,g,(i*squareSize)+X_OFFSET,(j*squareSize)+Y_OFFSET);
                        }else{
                            ImageIcon pipeImage=new ImageIcon("nopipe"+k+".png");
                            pipeImage.paintIcon(this,g,(i*squareSize)+X_OFFSET,(j*squareSize)+Y_OFFSET);
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
            g.drawLine(i*(squareSize)+X_OFFSET, 0, i*(squareSize)+X_OFFSET, ySize*squareSize+Y_OFFSET);
        }
        //Has to be done twice due to the chance of differing y and x sizes
        for(int i=0;i<=ySize;i++){
            g.drawLine(0, i*(squareSize)+Y_OFFSET, xSize*squareSize+X_OFFSET, i*(squareSize)+Y_OFFSET);
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
        int xMouse=e.getX()-X_OFFSET;
        int yMouse=e.getY()-Y_OFFSET;
        pipeNode selectedNode=pipesArray[xMouse/squareSize][yMouse/squareSize];
        //Left click
        if(e.getButton() == MouseEvent.BUTTON1){
            if(!currentLeftClick){
                if(xMouse>0&&yMouse>0){
                    int side=subSquares(yMouse%squareSize,xMouse%squareSize,squareSize);
                    selectedNode.swapPipe(side);
                    selectedNode.floodNodeIfShouldBe(side);
                }
                JPanel panel = new JPanel();
                panel.setPreferredSize(new Dimension(1000,1000));
                Canvas myGraphic=new Canvas();
                panel.add(myGraphic);
                addMouseListener(this);
                this.setSize((xSize*squareSize)+X_OFFSET,(ySize*squareSize)+Y_OFFSET);
                this.toFront(); 
                this.setVisible(true);
                repaint();
                currentLeftClick=true;
            }
        }
        //Right click
        else if (e.getButton() == MouseEvent.BUTTON3){
            if(!currentRightClick){
                if(xMouse>0&&yMouse>0&&selectedNode.pipeThere()){
                    selectedNode.flood(!selectedNode.isWaterHere());
                }
                JPanel panel = new JPanel();
                panel.setPreferredSize(new Dimension(1000,1000));
                Canvas myGraphic=new Canvas();
                panel.add(myGraphic);
                addMouseListener(this);
                this.setSize((xSize*squareSize)+X_OFFSET,(ySize*squareSize)+Y_OFFSET);
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
}
