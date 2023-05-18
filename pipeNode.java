
/**
 * Pipe node.
 *
 * @Brendan Shaw
 * @v9 - 18/5
 */
public class pipeNode
{
    boolean[] pipes=new boolean[4];
    boolean waterHere=false;
    public pipeNode(){
        boolean[] pipes=new boolean[4];
        boolean waterHere=false;
    }
    public boolean pipeThere(int pipe){
        return pipes[pipe];
    }
    public void swapPipe(int pipe){
        pipes[pipe]=!pipes[pipe];
    }
    public void flood(){
        waterHere=true;
    }
    public void dry(){
        waterHere=false;
    }
    public boolean isWaterHere(){
        return waterHere;
    }
}
