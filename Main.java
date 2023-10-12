/**
 * Waterways project
 *
 * @Brendan Shaw
 * @v39 - 13/10
 * 
 * This is the main function, where the frame is. 
 * 
 * Note, there are some extra comments which while dont seem very helpful, greatly increases readability in some cases.
 * 
 * What I need to do to fix my issues:
 */

//Importing

//Note some of these are commented out, as I am no longer using them I should delete them however I will leave them here until I am finished

import java.util.Scanner;

import javax.swing.*;

import java.awt.event.*;
import java.awt.*;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
public class Main extends JFrame implements ActionListener, MouseListener
{
    //Offset the grid is from the UI. I do not know any way of doing this automatically
    final int X_OFFSET=0;
    final int Y_OFFSET=60;
    //Number of sides on a square (No 'magic' numbers)
    final int SQUARE_SIDES=4;

    //Initalising varibles. Note these are not finals as I plan for the user to be able to modify these

    //Size of squares
    int squareSize=100;
    //Defult number of squares. 
    int xSize=100;
    int ySize=100;
    int x=xSize/2;
    int y=ySize/2;
    //Resolutions. Given values later
    int width;
    int height;
    //Array of the pipes. Third dimension is the edges (Note pipesArray is now 2d, however, some for loops
    //still use the third dimension as the side, so ill leave the comment here)
    PipeNode [][] pipesArray;
    //To ensure it only runs once if clicked             
    boolean currentLeftClick=false;
    boolean currentRightClick=false;
    //Clicked node data. Needs to be initialised here
    int xClick;
    int yClick;
    //The mode types. Currently just flood but should be able to easily add more
    boolean floodMode=false;
    
    //Menus. This is in one big array because it is so much cleaner like this
    final String[] MENU_NAMES={"Click Actions","Movement","Network","Help"};
    final String[][] MENU_ITEMS={{"Flooding/Drying","Place Pipes"},{"Up","Left","Right","Down"},{"Save","Load","Create New","Change Resolution","Change Pipe Size"},{"Placing Pipes","Actions","Hotkeys"}};
    final char[][] MENU_SHORTCUT = {{'f','g'},{'w','a','d','s'},{'k','l','c','r','p'},{'h','h','h'}};//Need hotkeys for menus that should not have hotkeys. This would be an issue if not for majority of menus having a hotkey anyway. Solution was to give a double up of the h hotkey
    final int ACTION_CONSTANT=0;
    final int MOVEMENT_CONSTANT=1;
    final int SAVE_CONSTANT=2;//There is likely a cleaner way to do this, however considering its purpose, it is fine
    final int HELP_CONSTANT=3;
    //I tried to use keyboard listener for movement but I couldn't get them working so I decided to use hotkeys
    final int MOVEMENT_AMOUNT=1;
    
    //The largest size avalible for any int
    final int MAX_SIZE=7;//This is significantly smaller than the max size, however the user should not be exceding this value. The max size is exponentual, 10^MAX_SIZE

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
    final String LOAD_SEPERATOR="\n";

    //Frame and Panel init
    JPanel panel = new JPanel();

    //Image initialising. Not finials because they have to be defined in a try catch due to files
    final int NUMBER_OF_PIPE_TYPES=3;
    Image[][] imageOfPipe= new Image[NUMBER_OF_PIPE_TYPES][SQUARE_SIDES];
    final String[] PIPE_TYPE_NAME = {"Regular","End","Flooded"};

    //Dialog messages. I am defining them here so I can easily change them later. They are arrays based on
    //each dialog, order being title>message. I know the comments are not required, but a giant wall of text 
    //is difficult to understand. I could define this somewhere else but it is here.
    final String[] PIPE_SIZE_CHANGE_MESSAGE={"Pipe Size","What do you wish to change the pipe size to?"};
    //Making new network
    final String[] NEW_NETWORK_X_MESSAGE={"New network","What do you wish the X size to be?"};
    final String[] NEW_NETWORK_Y_MESSAGE={"New network","What do you wish the Y size to be?"};
    //These are for the save 
    final String[] SAVE_MESSAGE={"Saves","What would you like to call your save?"};
    final String[] LOAD_MESSAGE={"Loading","What would you like to load? The avalible files are: \n"};
    final String[] FILE_ALREADY_MESSAGE={"File already exists","There is already a save file with this name,\nwould you like to override?"};
    //Generic unsaved message
    final String[] UNSAVED_MESSAGE={"Unsaved data","Are you sure you want to do this?\nAll unsaved data will be lost!"};
    //Generic error for invaild input
    final String INVALID_INPUT_MESSAGE="Invaild input, please try again!";
    //Message for when the user attempts to move out of the map
    final String INVALID_MOVEMENT_MESSAGE="This is outside the network!";
    //Generic message for when the user attempts to input a value that is too large
    final String[] TOO_LARGE_MESSAGE={"Selected size too large","This size is too large! \nPlease try again with a smaller size!"};
    //Changing resolution
    final String[] RESOLUTION_X_MESSAGE={"Change Resolution","Please select the new width of the window!"};
    final String[] RESOLUTION_Y_MESSAGE={"Change Resolution","Please select the new height of the window!"};
    //Error messages if the new resolution/network size will break the program
    final String[] RESOLUTION_SIZE_MESSAGE={"Resolution size too large","This size is too large! \nPlease try again with a smaller resolution or increase the size of the network!","This size is too large! \nPlease try again with a smaller resolution, the max resolution is: \n"," by "};
    final String[] NETWORK_SIZE_MESSAGE={"Network size too small","This size is too small! \nPlease try again with a larger network or decrease the resolution!"};
    final String[] SQUARE_SIZE_MESSAGE={"Square size too small","This size is too small! \nPlease try again with a larger network or decrease the resolution!"};
    final String[] LOAD_SIZE_MESSAGE={"Loaded Network size too small","The size this network is too small! \nPlease try again with a larger network or decrease the resolution!"};
    //Generic error messages
    final String[] ERROR_MESSAGE={"Error","There was an error, please try again!"};
    final String FILE_ERROR_MESSAGE="There is a missing file!";
    //Flood message
    final String[] FLOOD_MODE_MESSAGE={"Placing pipes has been selected", "Flooding and drying has been selected"};
    //The help
    final String[] HELP_MESSAGE={"Help","To Place down a pipe, click on a tile on the grid. If you wish to place a line, drag across multiple tiles!",
            "To preform an action, click the action in the menu (or use the hotkey) and click on the tile you wish to preform the action on! \nYou need to click the action in the menu (or use the hotkey) to deactivate it!",
            "Hotkeys allow you to quickly active a menu item by pressing that item's corresponding key! To find the corresponding key of an item, look to the right of the item!"};

    //The main loop has the init for the UI, but pipe init is in another function. Everything in here I would put above, but am unable to
    public Main()
    {
        //Makes the actual pipe network
        remakeNetwork();

        //Set the window size to the screen size. Slightly broken but it will take a while to get it to work perfectly. Note it does this seperatly from the height and
        //width variables as they would not have been defined yet and requires this function to occur to work
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setPreferredSize(screenSize);

        //Menu

        JMenuBar menuBar;
        JMenuItem menuItem;
        JMenu menu;
        menuBar=new JMenuBar();
        this.setJMenuBar(menuBar);

        //Adds the menu items to the menus then ot the menu bars
        for(int i=0;i<MENU_ITEMS.length;i++){
            menu=new JMenu(MENU_NAMES[i]);
            for(int j=0;j<MENU_ITEMS[i].length;j++){
                menuItem=new JMenuItem(MENU_ITEMS[i][j]);
                menuItem.addActionListener(this);
                menuItem.setAccelerator(KeyStroke.getKeyStroke(MENU_SHORTCUT[i][j]));
                menu.add(menuItem);
            }
            menuBar.add(menu);
        }
        
        //Canvas init
        Canvas myGraphic=new Canvas();
        addMouseListener(this);
        this.setSize(screenSize.width,screenSize.height);
        panel.add(myGraphic);
        this.pack();
        this.toFront(); 
        this.setVisible(true);
        this.show();

        //Gets the images of the pipes. Requires a try becuse its opening files
        try{
            for(int i=0; i<NUMBER_OF_PIPE_TYPES; i++){
                for(int j=0; j<imageOfPipe[i].length; j++){
                    imageOfPipe[i][j]=ImageIO.read(new File("pipe"+j+PIPE_TYPE_NAME[i]+".png"));
                }
            }
        }catch (java.io.IOException ioe){
            ioe.printStackTrace();
            openBoolDialog(ERROR_MESSAGE[0],FILE_ERROR_MESSAGE);
        }
        //Default resolution size. Needs to be done here or else reads width and height as 0
        width=getWidth()-X_OFFSET;
        height=getHeight()-Y_OFFSET;
        repaint();//Required to update resolution size
    }
    //Sets up a new network. Ported from old main function
    public void remakeNetwork(){
        pipesArray=new PipeNode[xSize][ySize];
        //Pipe init
        for(int i=0;i<pipesArray.length;i++){
            for(int j=0;j<pipesArray[i].length;j++){
                //Creates each PipeNode
                pipesArray[i][j]=new PipeNode();
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
                //it is more time effecient for me, and I dont see a better way of doing this
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
                PipeNode[] adjacentPipes={pipesArray[i][adjacentLocations[3]],pipesArray[adjacentLocations[0]][j],pipesArray[i][adjacentLocations[2]],pipesArray[adjacentLocations[1]][j]};
                pipesArray[i][j].setAdjacentPipeNode(adjacentPipes);
            }
        }
        repaint();//This ensures that it is up to date
    }
    //Renders everything
    public void paint (Graphics g){
        //This is a bit large but it has to due to the nature of rendering everything
        super.paint(g);
        Graphics2D g2=(Graphics2D)g;
        //Grid
        for(int i=0;i<=width/squareSize;i++){
            if(!(i>pipesArray.length)){
                g.drawLine(i*(squareSize)+X_OFFSET, Y_OFFSET, i*(squareSize)+X_OFFSET, (height)+Y_OFFSET);
            }else{
                i=width/squareSize+1;
            }
        }
        //Has to be done twice due to the chance of differing y and x sizes
        for(int i=0;i<=height/squareSize;i++){
            if(!(i>pipesArray[0].length)){
                g.drawLine(X_OFFSET, i*(squareSize)+Y_OFFSET, width+X_OFFSET, i*(squareSize)+Y_OFFSET);
            }else{
                i=height/squareSize+1;
            }
        }
        //This renders the pipes. It is a bit laggy, I will define it in main
        for(int i=0;i<width/squareSize;i++){
            for(int j=0;j<height/squareSize;j++){
                if (pipesArray[i+x][j+y].pipeThere()){
                    //This is used so it prints the blank parts of the nodes. This is much clleaner and faster than my last solution because it uses funcitons that did not exist then.
                    for(int k=0;k<SQUARE_SIDES;k++){
                        //Requires try catch due to file selection of the draw image
                        //I cannot use icons as you cannot change their size, dispite the fact that it is laggier and causes pixels to be deleted and added when they are of different size
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
                    }
                }
            }
        }
    }
    //Finds what item is clicked in the menu
    public void actionPerformed(ActionEvent e){
        String itemClicked=e.getActionCommand();
        //Finds the name of the item
        for(int i=0;i<MENU_ITEMS.length;i++){
            for(int j=0;j<MENU_ITEMS[i].length;j++){
                if(MENU_ITEMS[i][j]==itemClicked){
                    //This finds the command based on the name because do not know how to save functions
                    //I see no way other than a bunch of ifs or switchs
                    if(i==ACTION_CONSTANT){
                        actions(j);//Not required to make these functions and less effeicent but significantly more readable
                    }else if(i==MOVEMENT_CONSTANT){
                        movement(j);
                    }else if(i==SAVE_CONSTANT){//The 'network menu' use to be called Save menu, but this name was confusing and conflicted with the save function using the new system
                        network(j);
                    }else if(i==HELP_CONSTANT){//The 'network menu' use to be called Save menu, but this name was confusing and conflicted with the save function using the new system
                        help(j);
                    }else{//I am adding an extra line for the error printing as a fail safe and I think it's cleaner
                        openBoolDialog(ERROR_MESSAGE[0],ERROR_MESSAGE[1]);
                    }
                    //There is  else as a fail safe and cleaner code

                    //This breaks the for loop without using the break command, because you didn't like us using them outside of switchs 
                    i=MENU_ITEMS.length-1;
                    j=MENU_ITEMS[i].length-1;
                }
            }
            //Repaints it no matter what 
            repaint();
        }
    }
    //Does the 'actions'. Note, this is a actualy actions not the hotkeys which is what was reffered to in last function. These names originated from my original poor menu system
    public void actions(int keyPressed){
        switch(keyPressed){
                //I think I found an actual use for switchs
            case 0:
                //It just toggles it. No need to test whether it is already enabled anymore
                floodMode=true;
                break;//The only action now is flooding but I am leaving this here for expanability
            case 1:
                floodMode=false;//Changes the flood mode to require to be turned on/off with a seperate funciton because users were often confused 
                break;
            default:
                //This is kept in the terminal because it should be impossible. I will likely remove it once I move the error messages to dialog boxes.
                openBoolDialog(ERROR_MESSAGE[0],ERROR_MESSAGE[1]);
        }    
    }
    //Deals with movement commands. Previously dealed with the actual movement however this was ugly with the fail safes, 
    //however I have not changed the name of the function to ensure they are consistance
    public void movement(int keyPressed){
        int x=0;
        int y=0;
        switch(keyPressed){
                //Here I am using these values that are hard coded, but there isn't a better way, as they are ordered increasing by one, so adding an array would just move the issue onto an array.
            case 0:
                y=-MOVEMENT_AMOUNT;
                break;
            case 1:
                x=-MOVEMENT_AMOUNT;
                break;
            case 2:
                x=MOVEMENT_AMOUNT;
                break;
            case 3:
                y=MOVEMENT_AMOUNT;
                break;
            default:
                openBoolDialog(ERROR_MESSAGE[0],ERROR_MESSAGE[1]);
        }
        move(x,y);//I've converted the move system to this function for cleanliness
    }
    //Deals with 'network stuff'. I referred to this menu as 'save' however I have tried to change it to network to avoid confusion.
    public void network(int keyPressed){
        switch(keyPressed){
                //Unlike actions, I have these do to functions because it would be too messy otherwise.
            case 0:
                save();
                break;
            case 1:
                load();
                break;
            case 2:
                changeNetworkSize();
                break;
            case 3:
                changeResolution();
                break;
            case 4:
                changeSquareSize();
                break;
            default:
                openBoolDialog(ERROR_MESSAGE[0],ERROR_MESSAGE[1]);
        }
    }
    //Helps the user
    public void help(int keyPressed){//If the hotkey is pressed then only the first one will work, however that is the most important one anyway
        //The help message should contain all that is required for the help
        openBoolDialog(HELP_MESSAGE[0],HELP_MESSAGE[keyPressed+1]);//Need to add 1 because the first item in the array is the title
    }
    //Does the actual movement. This was just a better system and if I was using another key input system than hotkeys would allow for easy movement diagonally.
    public void move(int deltaX, int deltaY){
        //Checks to see if user can do said movement
        if(x+deltaX+(width/squareSize)>xSize ||
        y+deltaY+(height/squareSize)>xSize ||
        x+deltaX<0 || y+deltaY<0){
            //If they cannot the user will get and error message
            openBoolDialog(INVALID_INPUT_MESSAGE,INVALID_MOVEMENT_MESSAGE);
        }else{
            //Moves by the inputed amount. These are +s as to move backward, a negitive delta value should be given.
            x+=deltaX;
            y+=deltaY;
        }
    }
    //Remake the netowrk of a different size. Requires to be remade due to hte nature of java arrays
    public void changeNetworkSize(){
        if(openBoolDialog(UNSAVED_MESSAGE[0], UNSAVED_MESSAGE[1])){//Asks if the user really wishes to do this
            boolean checkValidSize=true;
            while(checkValidSize){//This is not the cleanest way, but it is the fastest for me to make
                //Keeps looping to request the new pipe size if they input an invalid case 
                int tempXSize=convertStringInputToInt(NEW_NETWORK_X_MESSAGE,xSize+"");//using +"" rather than converting to string using function
                if(tempXSize>0){//I expalin this in changeResolution
                    //Runs it twice because I dont have time to make two varibles which are used trhoughout the code into an array
                    int tempYSize=convertStringInputToInt(NEW_NETWORK_Y_MESSAGE,ySize+"");
                    if(tempYSize>0){//Tests to see if it is blank, thus user clicked the 'x'. This is not a part of the other if because 
                        //then it would display the network size message if the 'x' was clicked. >0 because it shouldnt be less than 0 anyway

                        //Checks to see if the size is valid
                        if(checkNewNetworkSize(tempXSize,tempYSize,width,height,squareSize)){
                            checkValidSize=false;
                            //Actually makes new netowrk
                            xSize=tempXSize;
                            ySize=tempYSize;
                            remakeNetwork();
                        }else{
                            checkValidSize=openBoolDialog(NETWORK_SIZE_MESSAGE[0],NETWORK_SIZE_MESSAGE[1]);
                        }
                    }else{//Breaks the loop, as the user has clicked the 'x'
                        checkValidSize=false;
                    }
                }else{//Breaks the loop, as the user has clicked the 'x'
                    checkValidSize=false;
                }
            }
        }
    }
    //Changes the square size. Originally one line but varification requires multiple and would be more annoying to make varification into a proper function with so few things that use it
    //expecally considering this bug is going to be the last thing I do on this stupid project.
    public void changeSquareSize(){
        boolean checkValidSize=true;
        while(checkValidSize){
            int tempSquareSize=convertStringInputToInt(PIPE_SIZE_CHANGE_MESSAGE,squareSize+"");
            if(tempSquareSize>0){
                if(checkNewNetworkSize(xSize,ySize,width,height,tempSquareSize)){
                    checkValidSize=false;
                    squareSize=tempSquareSize;
                }else{
                    checkValidSize=openBoolDialog(SQUARE_SIZE_MESSAGE[0],SQUARE_SIZE_MESSAGE[1]);//This makes the user stop looping if they press cancel
                }
            }else{//Breaks the loop
                checkValidSize=false;
            }
        }
    }
    //Changes the size of the resoltion
    public void changeResolution(){//No save thingy as it does not destroy network
        boolean checkValidSize=true;
        while(checkValidSize){
            //Keeps looping to request the new pipe size if they input an invalid case 
            int tempWidth=convertStringInputToInt(RESOLUTION_X_MESSAGE,getWidth()+"");
            if(tempWidth>0){//This needs to happen twice, as otherwise it would appear again if the user clicked 'x'
                //Runs it twice because I dont have time to make two varibles which are used trhoughout the code into an array
                int tempHeight=convertStringInputToInt(RESOLUTION_Y_MESSAGE,getHeight()+"");
                //I explained this in the changeNetworkSize function. While I should have put this in the function itself, that would require me to rework the entire function, which would not be worth it
                if(tempHeight>0){
                    //Checks to see if the size is valid
                    Dimension screenMaxSize = Toolkit.getDefaultToolkit().getScreenSize();
                    if(checkNewNetworkSize(xSize,ySize,tempWidth,tempHeight,squareSize)){
                        if(screenMaxSize.getHeight()>=tempHeight&&screenMaxSize.getWidth()>=tempWidth){
                        checkValidSize=false;
                        //Actually refinds them
                        height=tempHeight;
                        width=tempWidth;
                    }else{
                        checkValidSize=openBoolDialog(RESOLUTION_SIZE_MESSAGE[0],RESOLUTION_SIZE_MESSAGE[2]+screenMaxSize.getWidth()+RESOLUTION_SIZE_MESSAGE[3]+screenMaxSize.getHeight());
                    }
                    }else{
                        checkValidSize=openBoolDialog(RESOLUTION_SIZE_MESSAGE[0],RESOLUTION_SIZE_MESSAGE[1]);//If they click no, then it closes the menu
                    }
                }else{//Breaks the loop
                    checkValidSize=false;
                }//Could make a cleaner way using a temp variable, but I don't want to create another temp variable
            }else{//Breaks the loop
                checkValidSize=false;
            }
        }
        //Updates window size
        setPreferredSize(new Dimension(width,height));
        this.setSize(width,height);
    }//No need to repaint as it does that every menu update
    //This is used to check if a new network or resolution size is large enough to avoid attempting to render non existant tiles. True means safe, false, means that the program would return an error
    public boolean checkNewNetworkSize(int newXSize,int newYSize,int newXResolution, int newYResolution, int newSquareSize){
        //Currently blank
        if((newXSize-x)*newSquareSize<newXResolution//Checks to see if the new X or Y is too large
        ||(newYSize-y)*newSquareSize<newYResolution){
            return false;
        }
        return true;//Assuming current methods, if it hasnt been triggered it should be safe.
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
                    PipeNode selectedNode=pipesArray[xMouse/squareSize+x][yMouse/squareSize+y];
                    int side=subSquares(yMouse%squareSize,xMouse%squareSize,squareSize);
                    if(floodMode){
                        selectedNode.flood(!selectedNode.isWaterHere());
                        //No longer deactivation floodmode, as per review 
                    }else{
                        //This section is outdated
                        yClick=yMouse;//This is used for pipe dragging
                        xClick=xMouse;
                    }
                }
                currentLeftClick=true;//This stops this line from running again until the mouse button is removed
            }
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
        //and right up and only checking 1 and 3 once, however this hardly gets run except in one case
        //so I decided to leave it like this. Not using else ifs because returns

        //It finds what triangle was clicked by first splitting the tile into 4 smaller tiles. This is because it is much easier to hitbox an isosceles right angle triangle. 
        //It then finds what side of the triangle it is on. These values are hard coded as there is no way for the maths to be changed if they are still squares and the number
        //that is returned is the side of the square that is saved by both the PipeNodes and also the rest of the program

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
        //Should not run, but is required for java. I thought about putting the return 2 above here but it looked uglier
        return -1;
    }
    //Swaps the pipes if they are dragged over
    public void fillPipeDrag(int xNode1,int yNode1,int xNode2,int yNode2){
        //Gets the x and y of the location. I have to do this as I need to see if it is dragged across tiles, this is relitiv
        int xNode1Location=xNode1/squareSize;
        int xNode2Location=xNode2/squareSize;
        int yNode1Location=yNode1/squareSize;//I am using the fact that java rounds a lot here
        int yNode2Location=yNode2/squareSize;
        //This finds whether the pipe is up or down
        int numberOfTilesOnX=Math.abs(xNode1Location-xNode2Location);//While I could have put this straight into the funciton it would require repeating these
        int numberOfTilesOnY=Math.abs(yNode1Location-yNode2Location);

        if(numberOfTilesOnX>numberOfTilesOnY){
            //'Drags' the x
            if(xNode1Location<xNode2Location){
                pipeLine(xNode1,xNode2,yNode1,yNode1,true);   
            }else{
                pipeLine(xNode2,xNode1,yNode2,yNode2,true);
            }
        }else if(numberOfTilesOnX==numberOfTilesOnY&&numberOfTilesOnX==0){//Requres to be =0 to ensure it is not just diagonal
            //If there is only 1 tile, it uses subsquares. 
            PipeNode selectedNode=pipesArray[xNode1Location+x][yNode1Location+y];
            selectedNode.swapPipe(subSquares(yNode1%squareSize,xNode1%squareSize,squareSize));//This is the one use case for subSquares I meantioned eariler. It was used a lot more but I added the dragging
        }else{//This will run the y on diagonal, but I think that is better than not doing anything on diagonal
            //'Drags' the y
            if(yNode1Location<yNode2Location){
                pipeLine(xNode1,xNode1,yNode1,yNode2,false);
            }else{
                pipeLine(xNode2,xNode2,yNode2,yNode1,false);
            }
        }

    }
    //Puts a line of pipes down. This is a long piece of code, however it is mostly just initalising.
    public void pipeLine(int x1,int x2,int y1,int y2, boolean xDirection){
        //Finds the first node
        PipeNode firstNode=pipesArray[x1/squareSize+x][y1/squareSize+y];
        PipeNode secondNode;

        int xFirst=firstNode.xLocation();
        int yFirst=firstNode.yLocation();
        int pipeSideDefultState;

        //Initialises the directions
        int [] directions={0,SQUARE_SIDES/2};

        boolean stateToConvertTo; 
        int endCoordenate;
        int startCoordenate;

        //These are needed to ensure the code can be compressed so much
        int firstLocation;
        int secondLocation;

        int endNodeFlip=1;

        //This initalises all the varible that depend on whether the 'drag' is on the x direction or the y direction
        //There is a lot of stuff which is specific to each of the sides 
        if(xDirection){//Everything that is still here I cannot think of a way to remove them from this if statement without creataing an entire other one
            //y is the same as the first node to 'lock' in a line
            secondNode=pipesArray[x2/squareSize+x][y1/squareSize+y];

            endCoordenate=(x2/squareSize)+x;
            startCoordenate=xFirst+1;

            firstLocation=x1;
            secondLocation=x2;

            for(int j=0;j<SQUARE_SIDES/2;j++){
                directions[j]++;
            }
        }else{
            //x is the same as the first node to 'lock' in a line
            secondNode=pipesArray[x1/squareSize+x][y2/squareSize+y];

            endCoordenate=(y2/squareSize)+y;
            startCoordenate=yFirst+1;

            firstLocation=y1;
            secondLocation=y2;

            endNodeFlip=-1;//Required so the end nodes behave
        }
        //Processes the close node
        if(firstLocation%squareSize>squareSize/2){
            pipeSideDefultState=directions[0];
        }else{
            pipeSideDefultState=directions[1];
        }
        //This needs to be done here, because this is after the defult state is found and before the first pipe is changed
        stateToConvertTo=!firstNode.pipeThere(pipeSideDefultState); 
        //I have ensured they are the same for both x and y
        //Processes end node
        if(!xDirection){
            int tempDirection=directions[0];
            directions[0]=directions[1];
            directions[1]=tempDirection;
        }
        processEndNodes(!(firstLocation%squareSize>squareSize/2),directions[0],firstNode,(SQUARE_SIDES/2)*endNodeFlip,stateToConvertTo);
        //Processes the far end node. 
        processEndNodes(secondLocation%squareSize>squareSize/2,directions[1],secondNode,-(SQUARE_SIDES/2)*endNodeFlip,stateToConvertTo);//Math.abs(0+endNodeFlip) only works for left ot right
        //I am going to handle the end pipes seperately, to ensure only particular sides get swapped
        for(int i=startCoordenate;i<endCoordenate;i++){
            //I dont see a better way of doing this currently, as there is no way to my knowledge to
            //input the order of array coordeinates using a variable
            PipeNode selectedNode;
            if(xDirection){
                selectedNode=pipesArray[i][yFirst];
            }else{
                selectedNode=pipesArray[xFirst][i];
            }
            //Dispite this only being 2 long, I decided to use a for loop because its 'cleaner'
            for(int j=0;j<SQUARE_SIDES/2;j++){
                selectedNode.forcePipe(directions[j],stateToConvertTo);
            }
        }
    }
    //Used only by the function above, however used multiple times. 
    public void processEndNodes(boolean twoPipes, int smallSize, PipeNode nodeToSwap, int directionOfOtherSide, boolean stateToConvertTo){
        nodeToSwap.forcePipe(smallSize,stateToConvertTo);
        if(twoPipes){
            nodeToSwap.forcePipe(((smallSize+(directionOfOtherSide))),stateToConvertTo);
        }
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
        return (input==0);//This means it returns falsue unless yes was clicked
    }
    //This ensures the input is valid. Valid means no non number characters (i.e. '2', yes, 'a', no, '.'- this removes decimals) and >0 
    public int convertStringInputToInt(String[] messages, String defaultText){//I ask for the entire message array because its better
        String dialogInput=openDialog(messages[0], messages[1],defaultText);
        boolean validInput=!(dialogInput==null);//I am defining this here so it doesnt have to redefine this every time it loops
        while(validInput){//Dont need a break statement as there is a return, ensures that it does not = null
            if(dialogInput.matches("\\d+")&&dialogInput.length()<MAX_SIZE){//Efficent way to test to see if there is any non digit characters in a string, as try catch is really bad, or too large.
                int temptInt=Integer.parseInt(dialogInput);
                if(temptInt>0){//As all of the program requires the ints that are given by the user to be >1, this function will never accept 0<
                    return temptInt;
                }
            }else{
                dialogInput=openDialog(messages[0], messages[1]+"\n"+INVALID_INPUT_MESSAGE, defaultText);
            }
        }
        return -1;//Using -1, as it required ot return a int, and it cannot otherwise enter a negitive due to '-' being caught
    }
    //Saves a network
    public void save(){
        boolean cancelSave=false;//This is the boolean that allows the user to cancel saving if it already exists or decides against saving
        String fileName=openDialog(SAVE_MESSAGE[0], SAVE_MESSAGE[1], "");
        if(fileName.matches("null")||fileName.matches("")){//If the user cancels saving while giving it a name
            cancelSave=true;
        }
        try{
            System.out.println(fileName);
            File saveFile=new File (SAVE_DIRECTORY+fileName+SAVE_FILE_TYPE);
            //Check to ensure the user wishes to override the file
            if(!saveFile.createNewFile()){
                if(openBoolDialog(FILE_ALREADY_MESSAGE[0], FILE_ALREADY_MESSAGE[1])){//I need this to be seperate from the above if, or else it will ask if the user would like to override the file if it doesn't already exist
                    cancelSave=true;
                }
            }
            if(!cancelSave){
                FileWriter fileWriter=new FileWriter(saveFile);
                //I am creating a singular string to save. I know this could cause issue in complex systems but that is mostly negated with the PipeNode compression,
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
                            saveData+=SAVE_SEPERATOR+primePipeState;//If my code is bad, this should be a 1, however that should not happen. Funny enough, this did happen but its fixed now
                            //I save whether there is water or not using 1 and 0, as booleans are annoying as I am using strings to pass the data
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
            openBoolDialog(ERROR_MESSAGE[0],e+"");//This occurs throughout the code, but I decided to talk about it here, but this is here because I need to inform if there is an error with file stuff, but I removed all other System.out.prinlns
        }
    }
    //Loads a saved network
    public void load(){
        if(openBoolDialog(UNSAVED_MESSAGE[0], UNSAVED_MESSAGE[1])){
            try{
                //First it finds all the saves avalible to ask the user
                File saveFilesFolder=new File(SAVE_DIRECTORY);
                File[] avalibleSaves=saveFilesFolder.listFiles();
                String avalibleSavesNames="";
                for(int i=0;i<avalibleSaves.length;i++){//Adds the avalible save files to a string to give to the user
                    avalibleSavesNames+=(avalibleSaves[i].getName().replace(SAVE_FILE_TYPE,"")+LOAD_SEPERATOR);//Also removes the .txt for cleanliness 
                }
                String fileName;
                if(!(avalibleSavesNames=="")){
                    //Only runs if there are save files avilible
                    String[] avalibleSavesArray=(avalibleSavesNames.split(LOAD_SEPERATOR));//This is much faster than getting it again. I needed it ot be in a string format without that .txt
                    fileName=openDialog(LOAD_MESSAGE[0], LOAD_MESSAGE[1]+avalibleSavesNames, "");
                    boolean isAvalible=false;
                    //Tests to see is it exists
                    for(int i=0;i<avalibleSaves.length;i++){
                        if(avalibleSavesArray[i].equals(fileName)){
                            isAvalible=true;
                        }
                    }
                    //Doesnt load if it doesnt exist
                    if(isAvalible){
                        //Only runs if hte selected file is avalible
                        File saveFile=new File(SAVE_DIRECTORY+fileName+SAVE_FILE_TYPE);
                        Scanner fileReader=new Scanner(saveFile);
                        String[] saveData=fileReader.nextLine().split(SAVE_SEPERATOR);

                        //Parses the string to an int, no need to check if it actually is an int as currently in try
                        //I am using 'magic numbers' here, however these do not ever need to be changed, as more of these
                        //vlaues can be added at the end, and I see no way to put the entirety of varibles into an array

                        //This is used for varification. 
                        boolean checkValidSize=true;
                        while(checkValidSize){
                            int tempXSize=Integer.parseInt(saveData[0]);
                            int tempYSize=Integer.parseInt(saveData[1]);

                            if(checkNewNetworkSize(tempXSize,tempYSize,width,height,squareSize)){
                                checkValidSize=false;
                                xSize=tempXSize;
                                ySize=tempYSize;
                                x=Integer.parseInt(saveData[2]);
                                y=Integer.parseInt(saveData[3]);
                                squareSize=Integer.parseInt(saveData[4]);
                                remakeNetwork();
                                //Pipe data
                                for(int i=METADATA_SIZE;i<((saveData.length))-1;i=i+PIPEDATA_SIZE){
                                    PipeNode selectedNode=pipesArray[Integer.parseInt((saveData[i]))][Integer.parseInt(saveData[((i+1))])];
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
                            }else{
                                checkValidSize=openBoolDialog(LOAD_SIZE_MESSAGE[0],LOAD_SIZE_MESSAGE[1]);
                            }
                        }

                        //I have decided to include some examples which will mean this should never run. I have added a failsafe just in case though, which I removed and instead do what is happening now
                        //There use to be an extra ifelse and then an else which was here, which is why this comment is here
                    }else{
                        openBoolDialog(INVALID_INPUT_MESSAGE,INVALID_INPUT_MESSAGE);
                    }
                }
            }catch(IOException e){
                openBoolDialog(ERROR_MESSAGE[0],e+"");//I used +"" because its faster than me trying to find the actual function to convert io exceptions into strings, and it only runs once anyway
            }
        }
    }
}

/*Each save needs-
 *
 *    //This was just me planning what the save would be, however I kept it here for reference
 *  
 * Meta data-
 * x and y size
 * x and y location
 * pipe size 
 * //Note, Im not storing flood mode
 * //First flood mode has a very small impact on the user, and can be easily reactivated
 * //And pipe size will no longer be saved in a seperate file, and as such will now also be a part of the meta data due to time I have left
 * PipeNodes- //Will need location, because the user is likely to have majority of the file will be blank, thus this will compress the file more
 * Each of the 4 sides - //I will be using primes to effeicently save this, which while will be slower, it will be more space effecient
 * Whether it is flooded or not- //Will be a 1/0
 * //Note, Im not storing adjacent node or their locations as I will be processing and assigning these on load
 * //I am trying to be as space effeicent as a can reasonably be, at least while using regular csv file rather than a proper save file.
 */

