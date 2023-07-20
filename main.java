/**
 * Waterways project
 *
 * @Brendan Shaw
 * @v29 - 21/7
 * 
 * Need to:
 * 2- CLEAN UP!
 * 1- Text box
 */

//Importing

//NEED TO CHECK TO ENSURE ALL OF THESE ARE USED

import java.util.Scanner;

import javax.swing.*;
import javax.swing.JFrame; 
import javax.swing.border.TitledBorder;  

import java.awt.Dimension;
import java.awt.event.*;
import java.awt.*;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
public class main extends JFrame implements ActionListener, MouseListener
{
    //Offset the grid is from the UI. I do not know any way of doing this automatically
    final int X_OFFSET=0;
    final int Y_OFFSET=60;
    //Number of sides on a square (No 'magic' numbers)
    final int SQUARE_SIDES=4;
    //Name of the window (Likely not used)
    final String WINDOW_TITLE="Brendan Shaw's Waterways Project";

    //Initalising varibles. Note these are not finals as I plan for the user to be able to modify these

    //Size of squares
    int squareSize=100;
    //Number of squares. 
    int xSize=100;
    int ySize=100;
    int x=xSize/2;
    int y=ySize/2;
    //Array of the pipes. Third dimension is the edges (Note pipesArray is now 2d, however, some for loops
    //still use the third dimension as the side, so ill leave the comment here)
    pipeNode [][] pipesArray;
    //To ensure it only runs once if clicked             
    boolean currentLeftClick=false;
    boolean currentRightClick=false;
    //Clicked node data. Needs to be initialised here
    int xClick;
    int yClick;
    //The mode types. Currently just flood but should be able to easily add more
    boolean floodMode=false;
    //Menu 1- Actions
    final String ACTION_MENU_NAME="Actions";
    final String[] ACTION_MENU_ITEMS={"Change water","Change Pipe Size"};
    final char[] ACTION_MENU_SHORTCUT = {'f','p'};
    //Menu 2- Movement
    //I tried to use keyboard listener but I couldn't get them working so I decided to use hotkeys
    final String MOVEMENT_MENU_NAME="Movement";
    final String[] MOVEMENT_MENU_ITEMS={"Up","Left","Right","Down"};
    final char[] MOVEMENT_MENU_SHORTCUT = {'w','a','d','s'};
    //Menu 3- Saveing
    //I am thinking about redoing this entire system as it is really bad, however until then it will be ugly
    final String SAVE_MENU_NAME="Save/load";
    final String[] SAVE_MENU_ITEMS={"Save","Load"};
    final char[] SAVE_MENU_SHORTCUT = {'k','l'};

    //Save info
    final String SAVE_DIRECTORY=System.getProperty("user.dir")+"\\Saves\\";//This gets the location of the save file
    final String SAVE_SEPERATOR=",";
    final String SAVE_FILE_TYPE=".txt";//I will be using a txt because time constraints
    final int[] PRIME_NUMBERS={2,3,5,7};//This is used for saving stuff I will explain later
    final int METADATA_SIZE=5;//This is the amount of data saved in save files that is not the pipe data
    final int PIPEDATA_SIZE=4;//This is the amount of data saved in save files that is the pipe data
    //These two are the values saved whether there is water or not
    final int NO_WATER_INT=0;
    final int YES_WATER_INT=1;

    //Frame and Panel init
    //JFrame frame = new JFrame(WINDOW_TITLE);
    JPanel panel = new JPanel();

    //Image initialising. Not finials because they have to be defined in a try catch due to files
    final int NUMBER_OF_PIPE_TYPES=3;
    Image[][] imageOfPipe= new Image[NUMBER_OF_PIPE_TYPES][SQUARE_SIDES];
    final String[] PIPE_TYPE_NAME = {"Regular","End","Flooded"};

    //Dialog messages. I am defining them here so I can easily change them later. They are arrays based on
    //each dialog, order being title>message.
    final String[] PIPE_SIZE_CHANGE_MESSAGE={"Pipe Size","What do you wish to change the pipe size to?"};
    //These are for the save 
    final String[] SAVE_MESSAGE={"Saves","What would you like to call your save?"};
    final String[] LOAD_MESSAGE={"Loading","What would you like to load? The avalible files are: \n"};
    final String[] FILE_ALREADY_MESSAGE={"File already exists","There is already a save file with this name,\nwould you like to override?"};
    //Genertic error for invaild input
    final String INVALID_INPUT_MESSAGE="Invaild input, please try again!";
    public main()
    {
        //Finish init
        System.out.println(WINDOW_TITLE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setPreferredSize(screenSize);

        //Menu

        //NEEED TO CLEAN

        JMenuBar menuBar;
        JMenuItem menuItem;
        JMenu actionMenu;
        JMenu movementMenu;
        JMenu saveMenu;
        menuBar=new JMenuBar();
        this.setJMenuBar(menuBar);
        actionMenu=new JMenu(ACTION_MENU_NAME);
        menuBar.add(actionMenu);
        movementMenu=new JMenu(MOVEMENT_MENU_NAME);
        menuBar.add(movementMenu);
        saveMenu=new JMenu(SAVE_MENU_NAME);
        menuBar.add(saveMenu);
        //Actions
        for(int i=0;i<ACTION_MENU_ITEMS.length;i++){
            menuItem=new JMenuItem(ACTION_MENU_ITEMS[i]);
            menuItem.addActionListener(this);
            menuItem.setAccelerator(KeyStroke.getKeyStroke(ACTION_MENU_SHORTCUT[i]));
            actionMenu.add(menuItem);
        }
        //Movements
        for(int i=0;i<MOVEMENT_MENU_ITEMS.length;i++){
            menuItem=new JMenuItem(MOVEMENT_MENU_ITEMS[i]);
            menuItem.addActionListener(this);
            menuItem.setAccelerator(KeyStroke.getKeyStroke(MOVEMENT_MENU_SHORTCUT[i]));
            movementMenu.add(menuItem);
        }
        //Saaaaaaaaaaaaaaaaves
        for(int i=0;i<SAVE_MENU_ITEMS.length;i++){
            menuItem=new JMenuItem(SAVE_MENU_ITEMS[i]);
            menuItem.addActionListener(this);
            menuItem.setAccelerator(KeyStroke.getKeyStroke(SAVE_MENU_SHORTCUT[i]));
            saveMenu.add(menuItem);
        }

        //Canvas init
        Canvas myGraphic=new Canvas();

        addMouseListener(this);
        this.setSize(screenSize.width,screenSize.height);
        panel.add(myGraphic);
        this.pack();
        this.toFront(); 
        this.setVisible(true);
        //this.add(panel);
        this.show();

        //Makes the actual pipe network
        remakeNetwork();

        //Gets the images of the pipes
        try{
            for(int i=0; i<NUMBER_OF_PIPE_TYPES; i++){
                for(int j=0; j<imageOfPipe[i].length; j++){
                    imageOfPipe[i][j]=ImageIO.read(new File("pipe"+j+PIPE_TYPE_NAME[i]+".png"));
                }
            }
        }catch (java.io.IOException ioe){
            ioe.printStackTrace();
            System.out.println("Error, file is missing");
        }
    }
    //Sets up a new network. Ported from old main function
    public void remakeNetwork(){
        pipesArray=new pipeNode[xSize][ySize];
        //Pipe init
        for(int i=0;i<pipesArray.length;i++){
            for(int j=0;j<pipesArray[i].length;j++){
                //Creates each pipeNode
                pipesArray[i][j]=new pipeNode();
                pipesArray[i][j].giveLocation(i,j);//This is used for both debugging and identifying the locations for the pipe dragging
            }
        }
        //Adjacent pipe locations
        for(int i=0;i<pipesArray.length;i++){
            for(int j=0;j<pipesArray[i].length;j++){
                int[] adjacentLocations=new int[SQUARE_SIDES];
                //Not using switch because they are bad. (They dont allow variables)
                //Edges loop around because it is much easier to code, and is a simple
                //way to resolve the edge issue. The user would probably not experience
                //it anyway, unless they are you. This is a bad way of doing this however
                //it is more time effecient for me
                if(i==0){
                    adjacentLocations[1]=xSize-1;
                    adjacentLocations[0]=i+1;
                }else if(i==xSize-1){
                    adjacentLocations[1]=i-1;
                    adjacentLocations[0]=0;
                }else{
                    //This is the line that is run regularly
                    adjacentLocations[1]=i-1;
                    adjacentLocations[0]=i+1;
                }
                //Same thing repeated for j,y
                if(j==0){
                    adjacentLocations[3]=ySize-1;
                    adjacentLocations[2]=j+1;
                }else if(j==ySize-1){
                    adjacentLocations[3]=j-1;
                    adjacentLocations[2]=0;
                }else{
                    //This is the line that is run regularly
                    adjacentLocations[3]=j-1;
                    adjacentLocations[2]=j+1;
                }
                pipeNode[] adjacentPipes={pipesArray[i][adjacentLocations[3]],pipesArray[adjacentLocations[0]][j],pipesArray[i][adjacentLocations[2]],pipesArray[adjacentLocations[1]][j]};
                pipesArray[i][j].setAdjacentPipeNode(adjacentPipes);
            }
        }

        repaint();
    }
    //Renders everything
    public void paint (Graphics g){
        super.paint(g);
        Graphics2D g2=(Graphics2D)g;
        int width = getWidth();
        int height = getHeight();
        //Grid
        for(int i=0;i<=getWidth()/squareSize;i++){
            if(!(i>pipesArray.length)){
                g.drawLine(i*(squareSize)+X_OFFSET, Y_OFFSET, i*(squareSize)+X_OFFSET, ySize*squareSize+Y_OFFSET);
            }else{
                i=getWidth()/squareSize+1;
            }
        }
        //Has to be done twice due to the chance of differing y and x sizes
        for(int i=0;i<=getHeight()/squareSize;i++){
            if(!(i>pipesArray[0].length)){
                g.drawLine(X_OFFSET, i*(squareSize)+Y_OFFSET, xSize*squareSize+X_OFFSET, i*(squareSize)+Y_OFFSET);
            }else{
                i=getHeight()/squareSize+1;
            }
        }
        //This renders the pipes. It is a bit laggy, I will define it in main
        for(int i=0;i<width/squareSize;i++){
            for(int j=0;j<height/squareSize;j++){
                boolean squareHere=false;
                for(int k=0;k<SQUARE_SIDES;k++){
                    if(squareHere){
                        //Requires try catch due to file selection of the draw image
                        //I cannot use icons as you cannot change their size, dispite the fact that it is laggier

                        if(pipesArray[i+x][j+y].pipeThere(k)){
                            Image pipeImage;
                            if(pipesArray[i+x][j+y].isWaterHere()){
                                pipeImage=imageOfPipe[2][k];
                            }else{
                                pipeImage=imageOfPipe[0][k];
                            }
                            g2.drawImage(pipeImage,(i*squareSize)+X_OFFSET,(j*squareSize)+Y_OFFSET,squareSize,squareSize,this);
                        }else{

                            Image pipeImage=imageOfPipe[1][k];
                            g2.drawImage(pipeImage,(i*squareSize)+X_OFFSET,(j*squareSize)+Y_OFFSET,squareSize,squareSize,this);
                        }
                    }else if (pipesArray[i+x][j+y].pipeThere(k)){
                        squareHere=true;
                        k=-1;
                    }

                }
            }
        }
    }
    //Finds what item is clicked in the menu
    public void actionPerformed(ActionEvent e){
        String itemClicked=e.getActionCommand();
        //Added this so it doesnt have to loop more times if it has alrady found it
        boolean actionFound=false;
        boolean movementFound=false;
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
                    movementFound=true;
                    i=MOVEMENT_MENU_ITEMS.length;
                }
            }
        }
        //Saves. This is a really bad system I need to redo this once I have finished the project
        if(!actionFound&&!movementFound){
            for(int i=0;i<SAVE_MENU_ITEMS.length;i++){
                if(SAVE_MENU_ITEMS[i]==itemClicked){
                    saveMenuInputs(i);
                    i=SAVE_MENU_ITEMS.length;
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
            if(!floodMode){
                int xMouse=e.getX()-X_OFFSET;
                int yMouse=e.getY()-Y_OFFSET;
                if(xMouse>0&&yMouse>0){
                    fillPipeDrag(xClick,yClick,xMouse,yMouse);
                }
            }
        }
        else if(e.getButton() == MouseEvent.BUTTON3){currentRightClick=false;}
        repaint();
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
                        //No longer deactivation floodmode, as per review 
                    }else{
                        //This section is outdated
                        //selectedNode.swapPipe(side);
                        //selectedNode.floodNodeIfShouldBe(side);
                        yClick=yMouse;
                        xClick=xMouse;
                    }
                }
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

    //This function may be obsolete, however I have found a use case which while it would be more effeint for the user if 
    //I write a specialised function for it, however that is not time effeicent for me, so I will be using this (for pipe
    //dragging if only 1 tile is dragged)

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
        //Gets the x and y of the location. I have to do this as I need to see if it is dragged across tiles, this is relitiv
        int xNode1Location=xNode1/squareSize;
        int xNode2Location=xNode2/squareSize;
        int yNode1Location=yNode1/squareSize;
        int yNode2Location=yNode2/squareSize;

        int numberOfTilesOnX=Math.abs(xNode1Location-xNode2Location);
        int numberOfTilesOnY=Math.abs(yNode1Location-yNode2Location);

        if(numberOfTilesOnX>numberOfTilesOnY){
            //'Drags' the x
            if(xNode1Location<xNode2Location){
                pipeLine(xNode1,xNode2,yNode1,yNode1,true);   
            }else{
                pipeLine(xNode2,xNode1,yNode2,yNode2,true);
            }

        }else if(numberOfTilesOnX==numberOfTilesOnY){
            //If there is only 1 tile
            pipeNode selectedNode=pipesArray[xNode1Location+x][yNode1Location+y];
            selectedNode.swapPipe(subSquares(yNode1%squareSize,xNode1%squareSize,squareSize));
        }else{
            //'Drags' the y
            if(yNode1Location<yNode2Location){
                pipeLine(xNode1,xNode1,yNode1,yNode2,false);
            }else{
                pipeLine(xNode2,xNode2,yNode2,yNode1,false);
            }

        }

    }
    //Puts a line of pipes down. Very ugly, need to clean.
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
            directions[0]=SQUARE_SIDES/2-1;
            directions[1]=SQUARE_SIDES/2+1;

            //y is the same as the first node to 'lock' in a line
            secondNode=pipesArray[x2/squareSize+x][y1/squareSize+y];

            if(x1%squareSize>squareSize/2){
                pipeSideDefultState=directions[0];
                //This needs to be done here, because this is after the defult state is found and before the first pipe is changed
                stateToConvertTo=!firstNode.pipeThere(pipeSideDefultState); 
                //Processes end node
                processEndNodes(false,directions[0],firstNode,(SQUARE_SIDES/2),stateToConvertTo);
            }else{
                pipeSideDefultState=directions[1];
                //This needs to be done here, because this is after the defult state is found and before the first pipe is changed
                stateToConvertTo=!firstNode.pipeThere(pipeSideDefultState); 
                //Processes end node
                processEndNodes(true,directions[0],firstNode,(SQUARE_SIDES/2),stateToConvertTo);
            }

            //Processes the far end node
            if(x2%squareSize>squareSize/2){
                processEndNodes(true,directions[1],secondNode,-(SQUARE_SIDES/2),stateToConvertTo);
            }else{
                processEndNodes(false,directions[1],secondNode,-(SQUARE_SIDES/2),stateToConvertTo);
            }

            endCoordenate=secondNode.xLocation();
            startCoordenate=xFirst+1;
        }else{
            //The directions of the y
            directions[0]=SQUARE_SIDES/2-2;
            directions[1]=SQUARE_SIDES/2;

            //x is the same as the first node to 'lock' in a line
            secondNode=pipesArray[x1/squareSize+x][y2/squareSize+y];

            //Processes the close node
            if(y1%squareSize>squareSize/2){
                pipeSideDefultState=directions[0];
                //This needs to be done here, because this is after the defult state is found and before the first pipe is changed
                stateToConvertTo=!firstNode.pipeThere(pipeSideDefultState); 
                //Processes end node
                processEndNodes(false,directions[1],firstNode,-(SQUARE_SIDES/2),stateToConvertTo);
            }else{
                pipeSideDefultState=directions[1];
                //This needs to be done here, because this is after the defult state is found and before the first pipe is changed
                stateToConvertTo=!firstNode.pipeThere(pipeSideDefultState); 
                //Processes end node
                processEndNodes(true,directions[1],firstNode,-(SQUARE_SIDES/2),stateToConvertTo);
            }
            //Processes the far end node
            if(y2%squareSize>squareSize/2){
                processEndNodes(true,directions[0],secondNode,(SQUARE_SIDES/2),stateToConvertTo);
            }else{
                processEndNodes(false,directions[0],secondNode,(SQUARE_SIDES/2),stateToConvertTo);
            }
            endCoordenate=secondNode.yLocation();
            startCoordenate=yFirst+1;
        }
        //I am going to handle the end pipes seperately, to ensure only particular sides get swapped
        for(int i=startCoordenate;i<endCoordenate;i++){
            //I dont see a better way of doing this currently, as there is no way to my knowledge to
            //input the order of array coordeinates using a variable
            pipeNode selectedNode;
            if(xDirection){
                selectedNode=pipesArray[i][yFirst];
            }else{
                selectedNode=pipesArray[xFirst][i];
            }
            //Dispite this only being 2 long, I decided to use a for loop because its 'cleaner'
            for(int j=0;j<SQUARE_SIDES/2;j++){
                selectedNode.forcePipe(directions[j],stateToConvertTo);
                //selectedNode.floodNodeIfShouldBe(directions[j]);
            }
        }
    }
    //Used only by the function above. 
    public void processEndNodes(boolean twoPipes, int smallSize, pipeNode nodeToSwap, int directionOfOtherSide, boolean stateToConvertTo){
        nodeToSwap.forcePipe(smallSize,stateToConvertTo);
        if(twoPipes){
            nodeToSwap.forcePipe(smallSize+(directionOfOtherSide),stateToConvertTo);
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
                //For the changing pipe size, I decided to do it here because it would be messier doing it elsewhere. If there were more actions
                //being processed here, then I would likely. I might move this, if oyu are reading this then I did not 

                //Asking this here, so I can add an error message to the dialog box
                String dialogInput=openDialog(PIPE_SIZE_CHANGE_MESSAGE[0], PIPE_SIZE_CHANGE_MESSAGE[1], squareSize+"");
                //Keeps looping to request the new pipe size if they input an invalid case 
                boolean keepLoop=true;
                while(keepLoop){
                    if(dialogInput.matches("\\d+")){//Efficent way to test to see if there is any non digit characters in a string, as try catch is really bad.
                        squareSize=Integer.parseInt(dialogInput);
                        keepLoop=false;
                    }else{
                        dialogInput=openDialog(PIPE_SIZE_CHANGE_MESSAGE[0], PIPE_SIZE_CHANGE_MESSAGE[1]+"\n"+INVALID_INPUT_MESSAGE, squareSize+"");
                    }
                }
                repaint();
                break;
            case 2:
                System.out.println("DEBUG");
                break;
            default:
                //This is kept in the terminal because it should be impossible. I will likely remove it.
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
    //Processes the save menu inputs
    public void saveMenuInputs(int saveInput){
        switch(saveInput){
                //Unlike actions, I have these do to functions because it would be too messy otherwise.
            case 0:
                save();
                break;
            case 1:
                load();
                break;
            default:
                System.out.println("Error, not a save action");
        }
        repaint();
    }
    //Opens a dialog box to get input. Returns a string rather than an int so I can use this to get text later
    public String openDialog(String windowTitle, String prompt, String defaultText){
        //I know this is 1 line, but it is signifcantly easier to use this function as it allows me to customise this box easily, 
        //and also makes the code cleaner as I only have 3 varible inputs
        return (String)JOptionPane.showInputDialog(this,prompt,windowTitle,JOptionPane.PLAIN_MESSAGE,null,null,defaultText);
    } 
    //Opens a dialog box which is two button, consisting of a yes or no.
    public boolean openBoolDialog(String windowTitle, String prompt){
        //Very similar to above funciton, however they are different so its better to have two seperate functions. It returns an int but I need it boolean though
        int input=JOptionPane.showConfirmDialog(this,prompt,windowTitle,JOptionPane.YES_NO_OPTION);
        if(input==1){
            return true;
        }
        return false;//By default it will return false
    }
    //Saves a network
    public void save(){
        boolean cancelSave=false;//This is the boolean that allows the user to cancel saving if it already exists or decides against saving
        String fileName=openDialog(SAVE_MESSAGE[0], SAVE_MESSAGE[1], "");
        if(fileName==null){//If the user cancels saving while giving it a name
            cancelSave=true;
        }
        try{
            File saveFile=new File (SAVE_DIRECTORY+fileName+SAVE_FILE_TYPE);
            //Check to ensure the user wishes to override the file
            if(!saveFile.createNewFile()){
                if(openBoolDialog(FILE_ALREADY_MESSAGE[0], FILE_ALREADY_MESSAGE[1])){//I need this to be seperate from the above if, or else it will ask if the user would like to override the file if it doesn't already exist
                    cancelSave=true;
                }
            }
            if(!cancelSave){
                FileWriter fileWriter=new FileWriter(saveFile);
                //I am creating a singular string to save. I know this could cause issue in complex systems but that is mostly negated with the pipeNode compression,
                //and I dont really have time to do osmething else. 
                //I am using multiple lines as well to make the code easier to read, however the metadata can all be one line
                String saveData=xSize+SAVE_SEPERATOR+ySize;//x and y size
                saveData+=SAVE_SEPERATOR+x+SAVE_SEPERATOR+y;//the location
                saveData+=SAVE_SEPERATOR+squareSize;//the size of pipes
                //Now for the pipe nodes
                for(int i=0;i<pipesArray.length;i++){
                    for(int j=0;j<pipesArray[i].length;j++){
                        if(pipesArray[i][j].pipeThere()){
                            saveData+=SAVE_SEPERATOR+i+SAVE_SEPERATOR+j;//Locations
                            //I am using prime numbers to save the states of each node of the pipe
                            int primePipeState=1;//Needs to be equal to 1 so it can be multiplied
                            for(int k=0;k<SQUARE_SIDES;k++){
                                if(pipesArray[i][j].pipeThere(k)){
                                    primePipeState*=PRIME_NUMBERS[k];
                                }
                            }
                            saveData+=SAVE_SEPERATOR+primePipeState;//If my code is bad, this should be a 1, however that should not happen
                            //I save whether there is water or not using 1 and 0, as booleans are annoying
                            int isWaterHereInt=NO_WATER_INT;
                            if(pipesArray[i][j].isWaterHere()){
                                isWaterHereInt=YES_WATER_INT;
                            }
                            saveData+=SAVE_SEPERATOR+isWaterHereInt;
                        }
                    }
                }
                fileWriter.write(saveData);
                fileWriter.flush();
                fileWriter.close();
            }
        }catch(IOException e){
            System.out.println(e);
            System.out.println(SAVE_DIRECTORY+fileName+SAVE_FILE_TYPE);
        }
    }
    //Loads a saved network
    public void load(){
        try{
            //First it finds all the saves avalible to ask the user
            File saveFilesFolder=new File(SAVE_DIRECTORY);
            File[] avalibleSaves=saveFilesFolder.listFiles();
            String avalibleSavesNames="";
            for(int i=0;i<avalibleSaves.length;i++){//Adds the avalible save files to a string to give to the user
                avalibleSavesNames+=(avalibleSaves[i].getName().replace(SAVE_FILE_TYPE,"")+"\n");//Also removes the .txt for cleanliness 
            }
            String fileName;
            if(avalibleSavesNames==""){
                //This runs if there is no files avalible
                fileName=openDialog(LOAD_MESSAGE[0], "This here is temp, \nI will add a thing soon that stops the loading", "");
            }else{
                fileName=openDialog(LOAD_MESSAGE[0], LOAD_MESSAGE[1]+avalibleSavesNames, "");            
            }
            File saveFile=new File(SAVE_DIRECTORY+fileName+SAVE_FILE_TYPE);
            Scanner fileReader=new Scanner(saveFile);
            String[] saveData=fileReader.nextLine().split(SAVE_SEPERATOR);
            
            //System.out.println(fileReader.nextLine());
            //Parses the string to an int, no need to check if it actually is an int as currently in try
            //I am using 'magic numbers' here, however these do not ever need to be changed, as more of these
            //vlaues can be added at the end, and I see no way to put the entirety of varibles into an array
            
            xSize=Integer.parseInt(saveData[0]);
            ySize=Integer.parseInt(saveData[1]);
            x=Integer.parseInt(saveData[2]);
            y=Integer.parseInt(saveData[3]);
            squareSize=Integer.parseInt(saveData[4]);
            remakeNetwork();
            //Pipe data
            for(int i=METADATA_SIZE;i<((saveData.length))-1;i=i+PIPEDATA_SIZE){
                pipeNode selectedNode=pipesArray[Integer.parseInt((saveData[i]))][Integer.parseInt(saveData[((i+1))])];
                //I am using prime numbers to save the states of each node of the pipe
                for(int k=0;k<SQUARE_SIDES;k++){
                    if((Integer.parseInt(saveData[i+2]))%PRIME_NUMBERS[k]==0){
                        //Here I am using force node so that it does have to process water as much
                        selectedNode.forcePipe(k,true);
                    }
                    if(Integer.parseInt((saveData[i+3]))==YES_WATER_INT){
                        selectedNode.flood(true);
                    }
                }
            }
        }catch(IOException e){
            System.out.println(e);
        }
    }
}

/*Each save needs-

 * Meta data-
 * x and y size
 * x and y location
 * pipe size 
 * //Note, Im not storing flood mode
 * //First flood mode has a very small impact on the user, and can be easily reactivated
 * //And pipe size will no longer be saved in a seperate file, and as such will now also be a part of the meta data due to time I have left
 * Pipenodes- //Will need location, because the user is likely to have majority of the file will be blank, thus this will compress the file more
 * Each of the 4 sides - //I will be using primes to effeicently save this, which while will be slower, it will be more space effecient
 * Whether it is flooded or not- //Will be a 1/0
 * //Note, Im not storing adjacent node or their locations
 * //I will be processing and assigning these on load
 */

