
/**
 * Pipe node.
 *
 * @Brendan Shaw
 * @v16 - 6/6
 */
public class pipeNode
{
    //Number of sides on a square (No magic numbers)
    final int SQUARE_SIDES=4;
    private boolean[] pipes=new boolean[SQUARE_SIDES];
    private boolean waterHere=false;
    private int name;
    private pipeNode[] adjacentPipes=new pipeNode[SQUARE_SIDES];
    public void setAdjacentPipeNode(pipeNode[] adjaceneNodes){
        for(int i=0; i<SQUARE_SIDES; i++){
            adjacentPipes[i]=adjaceneNodes[i];
        }
    }

    public void giveName(int givenName){
        name=givenName;
    }

    public int whatName(){
        return(name);
    }

    public boolean pipeThere(){
        for(int i=0; i<SQUARE_SIDES; i++){
            if(pipes[i]){
                return true;
            }
        }
        return false;
    }

    public boolean pipeThere(int pipe){
        return pipes[pipe];
    }

    public void swapPipe(int pipe){
        pipes[pipe]=!pipes[pipe];
    }

    public void flood(boolean isWater){
        if(pipeThere()){
        boolean sendWaterChange=false;
        if(!waterHere&&isWater){
            sendWaterChange=true;
            waterHere=true;
        }else if(waterHere&&!isWater){
            sendWaterChange=true;
            waterHere=false;
        }
        if(sendWaterChange){
            for(int i=0; i<SQUARE_SIDES; i++){
                if(pipes[i]){
                    if (adjacentNodeHasPipe(i)){
                        adjacentPipes[i].flood(isWater);
                    }
                }
            }
        }
    }
    }

    public boolean isWaterHere(){
        return waterHere;
    }

    public boolean adjacentNodeHasPipe(int side){
        if(((side>((SQUARE_SIDES/2)-1))&&(adjacentPipes[side].pipeThere(side-(SQUARE_SIDES/2))))||((side<(SQUARE_SIDES/2))&&(adjacentPipes[side].pipeThere(side+(SQUARE_SIDES/2))))){
            return true;
        }
        return false;
    }

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
