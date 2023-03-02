
/**
 * Waterways project
 *
 * @Brendan Shaw
 * @v1 - 2/3
 * 
 * Need to:
 * Clean changing system,
 * Display grid
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
            if(pipesArray[pipeLocation[0]][pipeLocation[1]][pipeLocation[2]]){
                pipesArray[pipeLocation[0]][pipeLocation[1]][pipeLocation[2]]=false;
            }else{
                pipesArray[pipeLocation[0]][pipeLocation[1]][pipeLocation[2]]=true;
            }
            for(int i=0;i<pipesArray.length;i++){
                for(int j=0;j<pipesArray[i].length;j++){
                    for(int k=0;k<pipesArray[i][j].length;k++){
                        System.out.print(i);
                        System.out.print(" ");
                        System.out.print(j);
                        System.out.print(" ");
                        System.out.print(k);
                        System.out.print(" ");
                        System.out.println(pipesArray[i][j][k]);
                    }
                }
            }
        }
    }
}
