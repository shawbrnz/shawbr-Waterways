
/**
 * Pipe node.
 *
 * @Brendan Shaw
 * @v5 - 4/5
 */
public class pipeNode
{
    boolean[] pipes=new boolean[4];
    public pipeNode(){
        boolean[] pipes=new boolean[4];
    }
    public boolean pipeThere(int pipe){
        return pipes[pipe];
    }
    public void swapPipe(int pipe){
        pipes[pipe]=!pipes[pipe];
    }
}
