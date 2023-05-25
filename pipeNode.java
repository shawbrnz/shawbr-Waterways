
/**
 * Pipe node.
 *
 * @Brendan Shaw
 * @v13 - 25/5
 */
public class pipeNode
{
    private boolean[] pipes=new boolean[4];
    private boolean waterHere=false;
    private int name;
    private pipeNode[] adjacentPipes=new pipeNode[4];
    public void setAdjacentPipeNode(pipeNode[] adjaceneNodes){
        for(int i=0; i<4; i++){
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
        for(int i=0; i<4; i++){
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
        boolean continuation=false;
        if(!waterHere&&isWater){
            continuation=true;
            waterHere=true;
        }else if(waterHere&&!isWater){
            continuation=true;
            waterHere=false;
        }
        if(continuation){
            for(int i=0; i<4; i++){
                if(pipes[i]){
                    System.out.println(i);
                    if (adjacentNodeHasPipe(i)){
                        adjacentPipes[i].flood(isWater);
                    }
                }
            }
        }
    }

    public boolean isWaterHere(){
        return waterHere;
    }

    public boolean adjacentNodeHasPipe(int side){
        if(((side>1)&&(adjacentPipes[side].pipeThere(side-2)))||((side<2)&&(adjacentPipes[side].pipeThere(side+2)))){
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
