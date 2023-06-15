/**
 * Waterways project
 *
 * @Brendan Shaw
 * @v20 - 16/6
 * 
 * Need to:
 * 1- Finish the pipe dragging,
 * 2- CLEAN UP!
 * 3- File stuff
 */

//Importing

//NEED TO CHECK TO ENSURE ALL OF THESE ARE USED

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
    
    //NEEED TO MAKE SOME OF THESE CONSTANT, I DO NOT WANT THE USER TO MODIFY THESE, BUT I DO OTHERS
    
    //Size of squares
    int squareSize=100;
    //Number of squares. 
    int xSize=100;
    int ySize=100;
    int x=xSize/2;
    int y=ySize/2;
    //Array of the pipes. Third dimension is the edges (Note pipesArray is now 2d, however, some for loops
    //still use the third dimension as the side)
    pipeNode [][] pipesArray=new pipeNode[xSize][ySize];
    //To ensure it only runs once if clicked             
    boolean currentLeftClick=false;
    boolean currentRightClick=false;
    //Clicked node data
    int xClick;
    int yClick;
    //The mode types'
    boolean floodMode=false;
    //Menu 1
    final String ACTION_MENU_NAME="Actions";
    final String[] ACTION_MENU_ITEMS={"Change water","Does nothing"};
    final char[] ACTION_MENU_SHORTCUT = {'f','n'};
    //Menu 2
    //I tried to use keyboard listener but I couldn't get them working so I decided to use hotkeys
    final String MOVEMENT_MENU_NAME="Movement";
    final String[] MOVEMENT_MENU_ITEMS={"Up","Left","Right","Down"};
    final char[] MOVEMENT_MENU_SHORTCUT = {'w','a','d','s'};
    public main()
    {
        //Panel init
        JPanel panel = new JPanel();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        panel.setPreferredSize(screenSize);

        //Menu
        
        //NEEED TO CLEAN
        
        JMenuBar menuBar;
        JMenuItem menuItem;
        JMenu actionMenu;
        JMenu movementMenu;
        menuBar=new JMenuBar();
        this.setJMenuBar(menuBar);
        actionMenu=new JMenu(ACTION_MENU_NAME);
        menuBar.add(actionMenu);
        movementMenu=new JMenu(MOVEMENT_MENU_NAME);
        menuBar.add(movementMenu);
        for(int i=0;i<ACTION_MENU_ITEMS.length;i++){
            menuItem=new JMenuItem(ACTION_MENU_ITEMS[i]);
            menuItem.addActionListener(this);
            menuItem.setAccelerator(KeyStroke.getKeyStroke(ACTION_MENU_SHORTCUT[i]));
            actionMenu.add(menuItem);
        }
        for(int i=0;i<MOVEMENT_MENU_ITEMS.length;i++){
            menuItem=new JMenuItem(MOVEMENT_MENU_ITEMS[i]);
            menuItem.addActionListener(this);
            menuItem.setAccelerator(KeyStroke.getKeyStroke(MOVEMENT_MENU_SHORTCUT[i]));
            movementMenu.add(menuItem);
        }

        //Canvas init
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
                pipesArray[i][j].giveLocation(i,j);
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
    //This renders the grid
    public void paint (Graphics g){
        super.paint(g);
        Graphics2D g2=(Graphics2D)g;
        int width = getWidth();
        int height = getHeight();
        for(int i=0;i<width/squareSize;i++){
            for(int j=0;j<height/squareSize;j++){
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
    //Finds what item is clicked in the menu
    public void actionPerformed(ActionEvent e){
        String itemClicked=e.getActionCommand();
        //Added this so it doesnt have to loop more times if it has alrady found it
        boolean actionFound=false;
        //Actions
        for(int i=0;i<ACTION_MENU_ITEMS.length;i++){
            if(ACTION_MENU_ITEMS[i]==itemClicked){
                action(i);
                actionFound=true;
                i=ACTION_MENU_ITEMS.length;
            }
        }
        //Movements
        if(!actionFound){
            for(int i=0;i<MOVEMENT_MENU_ITEMS.length;i++){
                if(MOVEMENT_MENU_ITEMS[i]==itemClicked){
                    movement(i);
                    actionFound=true;
                    i=MOVEMENT_MENU_ITEMS.length;
                }
            }
        }
    }

    //These are required, dispite the fact that they are not used.
    public void mouseExited(MouseEvent e){}

    public void mouseEntered(MouseEvent e){}
    //Activates the 'dragging' when hte mouse is let go and allows for another click to occur
    public void mouseReleased(MouseEvent e){
        if(e.getButton() == MouseEvent.BUTTON1){
            currentLeftClick=false;
            int xMouse=e.getX()-X_OFFSET;
            int yMouse=e.getY()-Y_OFFSET;
            if(xMouse>0&&yMouse>0){
                fillPipeDrag(xClick,yClick,xMouse,yMouse);
            }
        }
        else if(e.getButton() == MouseEvent.BUTTON3){currentRightClick=false;}
    }
    //Processes the stuff for when there is a click
    public void mousePressed(MouseEvent e){
        //This ensures that the gui does not interfare with the grid
        int xMouse=e.getX()-X_OFFSET;
        int yMouse=e.getY()-Y_OFFSET;

        //Left click
        if(e.getButton() == MouseEvent.BUTTON1){
            //This is to resolve issues of multiple clicks
            if(!currentLeftClick){
                //This is to resolve issues if the user clicks off the screen
                if(xMouse>0&&yMouse>0){
                    pipeNode selectedNode=pipesArray[xMouse/squareSize+x][yMouse/squareSize+y];
                    int side=subSquares(yMouse%squareSize,xMouse%squareSize,squareSize);
                    if(floodMode){
                        selectedNode.flood(!selectedNode.isWaterHere());
                        //No longer deactivation floodmode 
                    }else{
                        //selectedNode.swapPipe(side);
                        //selectedNode.floodNodeIfShouldBe(side);
                        yClick=yMouse;
                        xClick=xMouse;
                    }
                }
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
    //per click which is bad if it only should go once
    public void mouseClicked(MouseEvent e){}

    //This function may be obsolete
    
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
    //Swaps the pipes if they are dragged over
    public void fillPipeDrag(int xNode1,int yNode1,int xNode2,int yNode2){
        System.out.println(xNode1+" "+yNode1+" "+xNode2+" "+yNode2);
        if(Math.abs(xNode1-xNode2)>Math.abs(yNode1-yNode2)){
            System.out.println("If ");
            //'Drags' the x
            if(xNode1<xNode2){
                pipeLine(xNode1,xNode2,yNode1,yNode1,true);
            }else{
                pipeLine(xNode2,xNode1,yNode2,yNode2,true);
            }
        }else{
            System.out.println("Else");
            //'Drags' the y
            if(yNode1<yNode2){
                pipeLine(xNode1,xNode1,yNode1,yNode2,false);
            }else{
                pipeLine(xNode2,xNode2,yNode2,yNode1,false);
            }
        }
    }
    //Puts a line of pipes down
    public void pipeLine(int x1,int x2,int y1,int y2, boolean xDirection){
        //Finds the first node
        pipeNode firstNode=pipesArray[x1/squareSize+x][y1/squareSize+y];
        pipeNode secondNode;

        int xFirst=firstNode.xLocation();
        int yFirst=firstNode.yLocation();
        int pipeSideDefultState;

        //Initialises the directions
        int [] directions=new int[2];

        
        boolean stateToConvertTo; 
        
        int endCoordenate;
        int startCoordenate;
        //This initalises all the varible that depend on whether the 'drag' is on the x direction or the y direction
        //There is a lot of stuff which is specific to each of the sides I might create more varibles to clean this
        if(xDirection){
            //The directions of the x
            directions[0]=1;
            directions[1]=3;

            //y is the same as the first node to 'lock' in a line
            secondNode=pipesArray[x2/squareSize+x][y1/squareSize+y];

            if(x1%squareSize>squareSize/2){
                pipeSideDefultState=directions[0];
                //This needs to be done here, because this is after the defult state is found and before the first pipe is changed
                stateToConvertTo=firstNode.pipeThere(pipeSideDefultState); 
                //Processes end node
                processEndNodes(false,directions[0],firstNode,(SQUARE_SIDES/2));
            }else{
                pipeSideDefultState=directions[1];
                //This needs to be done here, because this is after the defult state is found and before the first pipe is changed
                stateToConvertTo=firstNode.pipeThere(pipeSideDefultState); 
                //Processes end node
                processEndNodes(true,directions[0],firstNode,(SQUARE_SIDES/2));
            }

            //Processes the far end node
            if(x2%squareSize>squareSize/2){
                processEndNodes(true,directions[1],secondNode,-(SQUARE_SIDES/2));
            }else{
                processEndNodes(true,directions[1],secondNode,-(SQUARE_SIDES/2));
            }
            
            endCoordenate=secondNode.xLocation();
            startCoordenate=xFirst+1;
        }else{
            //The directions of the y
            directions[0]=0;
            directions[1]=2;

            //x is the same as the first node to 'lock' in a line
            secondNode=pipesArray[x1/squareSize+x][y2/squareSize+y];

            //Processes the close node
            if(y1%squareSize>squareSize/2){
                pipeSideDefultState=directions[0];
                //This needs to be done here, because this is after the defult state is found and before the first pipe is changed
                stateToConvertTo=firstNode.pipeThere(pipeSideDefultState); 
                //Processes end node
                processEndNodes(false,directions[0],firstNode,(squareSize/2));
            }else{
                pipeSideDefultState=directions[1];
                //This needs to be done here, because this is after the defult state is found and before the first pipe is changed
                stateToConvertTo=firstNode.pipeThere(pipeSideDefultState); 
                //Processes end node
                processEndNodes(true,directions[0],firstNode,(squareSize/2));
            }

            //Processes the far end node
            if(y2%squareSize>squareSize/2){
                processEndNodes(true,directions[1],secondNode,-(squareSize/2));
            }else{
                processEndNodes(true,directions[1],secondNode,-(squareSize/2));
            }

            endCoordenate=secondNode.yLocation();
            startCoordenate=yFirst+1;
        }
        //I am going to handle the end pipes seperately, to ensure only particular sides get swapped
        System.out.println("Before the for loop. Start is "+startCoordenate+", end is "+endCoordenate+". XY is "+x+","+y+". The direction is "+xDirection);
        for(int i=startCoordenate;i<endCoordenate;i++){
            //I dont see a better way of doing this currently, as there is no way to my knowledge to
            //input the order of array coordeinates using a variable
            pipeNode selectedNode;
            if(xDirection){
                System.out.println("1- Locked y to "+yFirst+". Current y is "+y+". i is"+i+". X"+x);
                selectedNode=pipesArray[i][yFirst];
                System.out.println("2- Locked y to "+yFirst+". Current y is "+y);
            }else{
                System.out.println("1-Locked x to "+xFirst+". Current x is "+x);
                selectedNode=pipesArray[xFirst][i];
                System.out.println("2-Locked x to "+xFirst+". Current x is "+x);
            }
            System.out.println("i is "+i);
            //Dispite this only being 2 long, I decided to use a for loop because its 'cleaner'
            for(int j=0;j<SQUARE_SIDES/2;j++){
                System.out.println("The state to conver to is "+stateToConvertTo+". The direction is "+directions[j]+". ij "+i+","+j+". Max should be "+SQUARE_SIDES/2+". xy "+selectedNode.xLocation()+","+selectedNode.yLocation());
                selectedNode.forcePipe(directions[j],stateToConvertTo);
                //selectedNode.floodNodeIfShouldBe(directions[j]);
            }
        }
        System.out.println("After the for loop. Start is "+startCoordenate+", end is "+endCoordenate+". XY is "+x+","+y+". The direction is "+xDirection);
    }
    //Used only by the function above. 
    public void processEndNodes(boolean twoPipes, int smallSize, pipeNode nodeToSwap, int directionOfOtherSide){
        nodeToSwap.swapPipe(smallSize);
        if(twoPipes){
            nodeToSwap.swapPipe(smallSize+(directionOfOtherSide));
        }
    }
    //Processes the action 
    public void action(int actionInput){
        switch(actionInput){
                //I think I found an actual use for switchs
            case 0:
                floodMode=!floodMode;
                break;
            case 1:
                break;
            default:
                System.out.println("Error, not an action");
        }
    }
    //Processes the movement
    public void movement(int movementInput){
        switch(movementInput){
                //I think I found an actual use for switchs
            case 0:
                y-=1;
                break;
            case 1:
                x-=1;
                break;
            case 2:
                x+=1;
                break;
            case 3:
                y+=1;
                break;
            default:
                System.out.println("Error, not a movement");
        }
        repaint();
    }
}
