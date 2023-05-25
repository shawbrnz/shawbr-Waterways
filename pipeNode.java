
/**
 * Pipe node.
 *
 * @Brendan Shaw
 * @v12 - 25/5
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
    //Omnidirectional flooding. Note does not flood itself
    public void flood(){
        waterHere=true;
        for(int i=0; i<4; i++){
            System.out.println("Pipe"+i);
            System.out.println(pipes[i]);
            if(pipes[i]){
                System.out.println("Pipeblank");
                if(i+2>3){
                    System.out.println("AttemptedSent");
                    System.out.println(adjacentPipes[i].pipeThere());
                    adjacentPipes[i].flood(i-2);
                    System.out.println("Sent");
                }else{
                    System.out.println("AttemptedSent");
                    System.out.println(adjacentPipes[i].pipeThere());
                    adjacentPipes[i].flood(i+2);
                    System.out.println("Sent");
                }
                System.out.println("I is"+i);
            }
        }
    }
    //Floods depending on intput
    public void flood(int input){
        System.out.println("Received");
        if(pipes[input]){
            System.out.println("Tested");
            if(!waterHere){
                System.out.println("Watertest");
                waterHere=true;
                for(int i=0; i<4; i++){
                    System.out.println("Spagetti enter");
                    if(pipes[i]){
                        if(i+2>3){
                            adjacentPipes[i].flood(i-2);
                            System.out.println("Sent");
                        }else{
                            adjacentPipes[i].flood(i+2);
                            System.out.println("Sent");
                        }
                    }
                }
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
