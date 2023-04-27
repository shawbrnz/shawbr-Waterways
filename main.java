
/**
 * Waterways project
 *
 * @Brendan Shaw
 * @v2 - 27/4
 * 
 * Need to:
 * 1- GUI,
 * 2- Clean changing system,
 * 3- Water,
 * 4- File stuff
 */

//Importing
import java.util.Scanner;

public class main
{
    Scanner scanner = new Scanner(System.in);
    int xSize=10;
    int ySize=10;
    boolean [][][] pipesArray=new boolean[xSize][ySize][4];
    boolean keepRunning=true;
    public main()
    {
        while (keepRunning){
            System.out.println("Where do you want change?");
            String[] data=scanner.nextLine().split(" ");
            int[] pipeLocation=
                {Integer.valueOf(data[0]),
                Integer.valueOf(data[1]),
                Integer.valueOf(data[2])};
            pipesArray[pipeLocation[0]][pipeLocation[1]][pipeLocation[2]]=
            swap(pipesArray[pipeLocation[0]][pipeLocation[1]][pipeLocation[2]]);
            for(int i=0;i<pipesArray.length;i++){
                for(int j=0;j<pipesArray[i].length;j++){
                    for(int k=0;k<pipesArray[i][j].length;k++){
                        if(pipesArray[i][j][k]){
                            System.out.print("i");
                        }else{
                            System.out.print(" ");
                        }
                    }
                    System.out.print("|");
                }
                System.out.println("");
            }
        }
    }
    boolean swap(boolean swapper){
        if(swapper){
            return false;
        }
        return true;
    }
}
