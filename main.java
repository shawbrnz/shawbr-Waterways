/**
 * Waterways project
 *
 * @Brendan Shaw
 * @v16 - 6/6
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
    int xSize=100;
    int ySize=100;
    int x=xSize/2;
    int y=ySize/2;
    //Array of the pipes. Third dimension is the edges
    pipeNode [][] pipesArray=new pipeNode[xSize][ySize];
    //To ensure it only runs once if clicked             TEMP
    boolean currentLeftClick=false;
    boolean currentRightClick=false;
    //The mode types'
    boolean floodMode=false;
    //Menu 1
    final String ACTION_MENU_NAME="Actions";
    final String[] ACTION_MENU_ITEMS={"Change water", "Does nothing"};
    final char[] ACTION_MENU_SHORTCUT = {'f','n'};
    public main()
    {
        //Panel init
        JPanel panel = new JPanel();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        panel.setPreferredSize(screenSize);

        //Menu
        JMenuBar menuBar;
        JMenuItem menuItem;
        JMenu menu;
        menuBar=new JMenuBar();
        this.setJMenuBar(menuBar);
        menu=new JMenu(ACTION_MENU_NAME);
        menuBar.add(menu);
        for(int i=0;i<ACTION_MENU_ITEMS.length;i++){
            menuItem=new JMenuItem(ACTION_MENU_ITEMS[i]);
            menuItem.addActionListener(this);
            menuItem.setAccelerator(KeyStroke.getKeyStroke(ACTION_MENU_SHORTCUT[i]));
            menu.add(menuItem);
        }

        //Canvas
        Canvas myGraphic=new Canvas();
        panel.add(myGraphic);
        addMouseListener(this);
        this.setSize(screenSize.width,screenSize.height);
        this.toFront(); 
        this.setVisible(true);
        repaint();

        //Pipe init
        for(int i=0;i<pipesArray.length;i++){
            for(int j=0;j<pipesArray[i].length;j++){
                pipesArray[i][j]=new pipeNode();
                pipesArray[i][j].giveName((j*pipesArray.length)+i);
            }
        }
        for(int i=0;i<pipesArray.length;i++){
            for(int j=0;j<pipesArray[i].length;j++){
                int[] adjacentLocations=new int[SQUARE_SIDES];
                //Not using switch because they are bad. (They dont allow variables)
                //Edges loop around because it is much easier to code, and is a simple
                //way to resolve the edge issue. The user would probably not experience
                //it anyway
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
        for(int i=0;i<height/squareSize;i++){
            for(int j=0;j<width/squareSize;j++){
                boolean squareHere=false;
                for(int k=0;k<SQUARE_SIDES;k++){
                    if(squareHere){
                        if(pipesArray[i+x][j+y].pipeThere(k)){
                            ImageIcon pipeImage;
                            if(pipesArray[i+x][j+y].isWaterHere()){
                                pipeImage=new ImageIcon("pipe"+k+"flooded.png");
                            }else{
                                pipeImage=new ImageIcon("pipe"+k+".png");
                            }
                            pipeImage.paintIcon(this,g,(i*squareSize)+X_OFFSET,(j*squareSize)+Y_OFFSET);
                        }else{
                            ImageIcon pipeImage=new ImageIcon("nopipe"+k+".png");
                            pipeImage.paintIcon(this,g,(i*squareSize)+X_OFFSET,(j*squareSize)+Y_OFFSET);
                        }
                    }else if (pipesArray[i+x][j+y].pipeThere(k)){
                        squareHere=true;
                        k=-1;
                    }
                }
            }
        }
        //Grid
        for(int i=0;i<=getWidth()/squareSize;i++){
            if(!(i>pipesArray.length)){
                g.drawLine(i*(squareSize)+X_OFFSET, 0, i*(squareSize)+X_OFFSET, ySize*squareSize+Y_OFFSET);
            }else{
                i=getWidth()/squareSize+1;
            }
        }
        //Has to be done twice due to the chance of differing y and x sizes
        for(int i=0;i<=getHeight()/squareSize;i++){
            if(!(i>pipesArray[0].length)){
                g.drawLine(0, i*(squareSize)+Y_OFFSET, xSize*squareSize+X_OFFSET, i*(squareSize)+Y_OFFSET);
            }else{
                i=getHeight()/squareSize+1;
            }
        }
    }

    public void actionPerformed(ActionEvent e){
        String itemClicked=e.getActionCommand();
        for(int i=0;i<ACTION_MENU_ITEMS.length;i++){
            if(ACTION_MENU_ITEMS[i]==itemClicked){
                action(i);
            }
        }
    }

    //These are required, dispite the fact that they are not used.
    public void mouseExited(MouseEvent e){}

    public void mouseEntered(MouseEvent e){}

    public void mouseReleased(MouseEvent e){
        if(e.getButton() == MouseEvent.BUTTON1){currentLeftClick=false;}
        else if(e.getButton() == MouseEvent.BUTTON3){currentRightClick=false;}
    }

    public void mousePressed(MouseEvent e){
        int xMouse=e.getX()-X_OFFSET;
        int yMouse=e.getY()-Y_OFFSET;
        pipeNode selectedNode=pipesArray[xMouse/squareSize+x][yMouse/squareSize+y];
        System.out.println(xMouse/squareSize+x);
        System.out.println(yMouse/squareSize+y);
        System.out.println("AHHH");
        //Left click
        if(e.getButton() == MouseEvent.BUTTON1){
            if(!currentLeftClick){
                if(xMouse>0&&yMouse>0){
                    int side=subSquares(yMouse%squareSize,xMouse%squareSize,squareSize);
                    if(floodMode){
                        selectedNode.flood(!selectedNode.isWaterHere());
                        floodMode=false;
                    }else{
                        selectedNode.swapPipe(side);
                        selectedNode.floodNodeIfShouldBe(side);
                    }
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
    //Processes the action 
    public void action(int actionInput){
        switch(actionInput){
            case 0:
                floodMode=!floodMode;
                break;
            case 1:
                break;
            default:
                System.out.println("Error, not an action");
        }
    }

}
