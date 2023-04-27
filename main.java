
/**
 * Waterways project
 *
 * @Brendan Shaw
 * @v3 - 28/4
 * 
 * Need to:
 * 1- GUI,
 * 2- Clean changing system,
 * 3- Water,
 * 4- File stuff
 */

//Importing
import java.util.Scanner;
import java.awt.*;
import javax.swing.*;
import javax.swing.JFrame;   
import java.awt.Dimension;
import java.awt.event.*;
public class main extends JFrame
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
            JPanel panel = new JPanel();
            panel.setPreferredSize(new Dimension(400,400));
            Canvas myGraphic=new Canvas();
            panel.add(myGraphic);
            this.pack();
            this.toFront(); 
            this.setVisible(true);
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
