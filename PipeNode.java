
/**
 * Pipe node.
 *
 * @Brendan Shaw
 * @v38 - 12/10
 * 
 *  This is the node for each pipe
 * 
 */
public class PipeNode
{
    //I made these private because it means I dont accidently change these, and I've written the main function now and it is easier to read I think
    //Number of sides on a square (No magic numbers)
    final int SQUARE_SIDES=4;
    private boolean[] pipes=new boolean[SQUARE_SIDES];
    private boolean waterHere=false;
    private int xLoc;
    private int yLoc;
    private PipeNode[] adjacentPipes=new PipeNode[SQUARE_SIDES];
    //Sets the adjacent nodes of this node so flooding can be fast
    public void setAdjacentPipeNode(PipeNode[] adjacentNodes){
        for(int i=0; i<SQUARE_SIDES; i++){
            adjacentPipes[i]=adjacentNodes[i];
        }
    }
    //This sets the location id of this pipe
    public void giveLocation(int x,int y){
        xLoc=x;
        yLoc=y;
    }
    //Returns x location
    public int xLocation(){
        return(xLoc);
    }
    //Returns y location
    public int yLocation(){
        return(yLoc);
    }
    //Tests to see if there is a pipe there
    public boolean pipeThere(){
        for(int i=0; i<SQUARE_SIDES; i++){
            if(pipes[i]){
                return true;
            }
        }
        return false;
    }
    //Tests to see if there is a pipe in this specific node. Same name as above but also needs an int
    public boolean pipeThere(int pipe){
        return pipes[pipe];
    }
    //Swaps the pipes state
    public void swapPipe(int pipe){
        pipes[pipe]=!pipes[pipe];
        if(!pipeThere()){
            //Removes the water if there is no pipes
            waterHere=false;
        }else{
            //I will explain why this is required later
            floodNodeIfShouldBe(pipe);
        }
    }
    //Forces a pipe into a state
    public void forcePipe(int pipe, boolean pipeState){
        pipes[pipe]=pipeState;
        //This part is explained above. I thought about moving these into a function but I didn't think it was nessisary, as it is just an if statement
        if(!pipeThere()){
            waterHere=false;
        }else{
            floodNodeIfShouldBe(pipe);
        }
    }
    //Floods/unfloods this node and then the adjacent nodes. True input means it will flood, false will dry.
    public void flood(boolean isWater){
        if(pipeThere()){
            boolean sendWaterChange=false;
            //I thought about shortening this, however I think this is the best way of doing it to allow for the next comment
            if(!waterHere&&isWater){
                sendWaterChange=true;
                waterHere=true;
            }else if(waterHere&&!isWater){
                sendWaterChange=true;
                waterHere=false;
            }
            //This ensures it does not send out a requiest to flood if it is currently flooded, or visa versa so it wont just get stuck in a loop
            if(sendWaterChange){
                for(int i=0; i<SQUARE_SIDES; i++){
                    //It actually has to have a pipe facing that direction and the adjacent node facing towards this node.
                    if(pipes[i]){
                        if(adjacentNodeHasPipe(i)){
                            adjacentPipes[i].flood(isWater);
                        }
                    }
                }
            }
        }
    }
    //Returns if water is here
    public boolean isWaterHere(){
        return waterHere;
    }
    //Tests to see if the adjacent node has the pipe facing this node
    public boolean adjacentNodeHasPipe(int side){
        //There is no way to make this smaller without added extra ifs. This is because it needs to add 2 if it is 0-1 and remove 2 if 2-3, to get the opposite side.
        if(((side>((SQUARE_SIDES/2)-1))&&(adjacentPipes[side].pipeThere(side-(SQUARE_SIDES/2))))
        ||((side<(SQUARE_SIDES/2))&&(adjacentPipes[side].pipeThere(side+(SQUARE_SIDES/2))))){
            return true;
        }
        return false;
    }
    //This solves an edge case where a node is placed down and is currently flooded, would in the old code would not flood the adjacent node.
    public void floodNodeIfShouldBe(int side){
        if(adjacentNodeHasPipe(side)){
            if(adjacentPipes[side].isWaterHere()){
                flood(true);
            }else if(waterHere){
                adjacentPipes[side].flood(true);
            }
        }
    }
}
