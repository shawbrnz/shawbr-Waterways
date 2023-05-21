
/**
 * Pipe node.
 *
 * @Brendan Shaw
 * @v11 - 22/5
 */
public class pipeNode
{
    private boolean[] pipes=new boolean[4];
    private boolean waterHere=false;
    private pipeNode[] adjacentPipes=new pipeNode[4];
    public void setAdjacentPipeNode(pipeNode[] adjaceneNodes){
        for(int i=0; i<4; i++){
            adjacentPipes[i]=adjaceneNodes[i];
        }
    }

    public boolean pipeThere(int pipe){
        return pipes[pipe];
    }

    public void swapPipe(int pipe){
        pipes[pipe]=!pipes[pipe];
    }
    //Omnidirectional flooding. Note does not flood itself
    public void flood(){
        for(int i=0; i<4; i++){
            if(pipes[i]){
                if(i+2>3){
                    adjacentPipes[i].flood(i-2);
                }else{
                    adjacentPipes[i].flood(i+2);
                }
            }
        }
    }
    //Floods depending on intput
    public void flood(int input){
        if(pipes[input]){
            if(waterHere){
                for(int i=0; i<4; i++){
                    if(pipes[i]){
                        if(i+2>3){
                            adjacentPipes[i].flood(i-2);
                        }else{
                            adjacentPipes[i].flood(i+2);
                        }
                    }
                }
            }else{
                waterHere=true;
            }
        }
    }
    //Very broken. Will not spot, thus will not use yet
    public void flood(int input, boolean force){
        for(int i=0; i<4; i++){
            if(pipes[i]){
                waterHere=true;
                adjacentPipes[i].flood(0,true);
            }
        }
    }

    public void dry(){
        waterHere=false;
    }

    public boolean isWaterHere(){
        return waterHere;
    }
}
