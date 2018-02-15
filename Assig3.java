
/*KNOWN ISSUES:
 * when searching for two words, if they are vertical and right next to each other, it counts as a solution even though it shouldn't
 */
import java.io.*;
import java.util.*;

public class Assig3
{
    ArrayList<Integer> posList=new ArrayList<Integer>();
    public static void main(String [] args)
    {
        new Assig3();
    }

    public Assig3()
    {
        Scanner inScan = new Scanner(System.in);
        Scanner fReader;
        File fName;
        String fString = "", phrase = "";

        while (true)
        {
            try
            {
                System.out.println("Please enter grid filename:");
                fString = inScan.nextLine();
                fName = new File(fString);
                fReader = new Scanner(fName);

                break;
            }
            catch (IOException e)
            {
                System.out.println("Problem " + e);
            }
        }

        // Parse input file to create 2-d grid of characters
        String [] dims = (fReader.nextLine()).split(" ");
        int rows = Integer.parseInt(dims[0]);
        int cols = Integer.parseInt(dims[1]);

        char [][] theBoard = new char[rows][cols];

        for (int i = 0; i < rows; i++)
        {
            String rowString = fReader.nextLine();
            for (int j = 0; j < rowString.length(); j++)
            {
                theBoard[i][j] = Character.toLowerCase(rowString.charAt(j));
            }
        }

        // Show user the grid
        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < cols; j++)
            {
                System.out.print(theBoard[i][j] + " ");
            }
            System.out.println();
        }

        System.out.println("Please enter the phrase to search for (Sep. by single spaces):");
        String [] words = (inScan.nextLine()).split(" ");

        while(!(words[0].equals("")))
        {
            System.out.print("\nLooking for: ");
            for(int p=0;p<words.length;p++)
            {
                System.out.print(words[p]+" ");
            }
            System.out.println("\nContaining "+words.length+" words");
            int x=0, y=0;

            boolean found=false;
            for(int p=0;p<words.length;p++)
            {
                String word=words[p];

                for (int r = 0; (r < rows && !found); r++)
                {
                    for (int c = 0; (c < cols && !found); c++)
                    {
                        found = Assig3(r, c, words, 0, theBoard,1);
                        if (found)
                        {
                            x = r;  // store starting indices of solution
                            y = c;
                        }
                    }
                }
            }

            if (found)
            {
                System.out.print("The phrase:");
                for(int p=0;p<words.length;p++)
                {
                    System.out.print(words[p]+" ");
                }
                System.out.println("\nwas found starting at location ("+posList.get(0)+","+posList.get(1)+")");

                for(int p=0,i=3;p<words.length;p++,i+=4)
                {
                    System.out.println(words[p]+": ("+posList.get(i-3)+","+posList.get(i-2)+")"+" to ("+posList.get(i-1)+","+posList.get(i)+")");
                }
                for (int i = 0; i < rows; i++)
                {
                    for (int j = 0; j < cols; j++)
                    {
                        System.out.print(theBoard[i][j] + " ");
                        theBoard[i][j] = Character.toLowerCase(theBoard[i][j]);
                    }
                    System.out.println();
                }
            }
            else
            {
                System.out.println("The phrase was not found");
            }
            while(!posList.isEmpty())
                posList.remove(0);

            System.out.println("Please enter the phrase to search for:");
            words = new String(inScan.nextLine()).split(" ");
        }
    }
    //direction 1=right 2=down 3=left 4=up
    public boolean findWord(int r, int c, String word, int loc, char [][] bo, int direction)
    {
        //System.out.println("findWord: " + r + "," + c + " " + word + ": " + loc+" dir: "+direction); // trace code
        boolean answer=false;
        if (r >= bo.length || r < 0 || c >= bo[0].length || c < 0)
            return false;
        else if (bo[r][c] != word.charAt(loc))  // char does not match
        {
            return false;
        }
        else
        {
            bo[r][c] = Character.toUpperCase(bo[r][c]); 

            if (loc == word.length()-1)     // base case - word found
            {
                answer = true;

            }
            else
            {
                if(direction==1)
                    answer=findWord(r,c+1,word,loc+1,bo,1);
                if(direction==2)
                    answer=findWord(r+1,c,word,loc+1,bo,2);
                if(direction==3)
                    answer=findWord(r,c-1,word,loc+1,bo,3);
                if(direction==4)
                    answer=findWord(r-1,c,word,loc+1,bo,4); 
                if(!answer)
                {
                    bo[r][c] = Character.toLowerCase(bo[r][c]);
                }
            }

        }
        return answer;
    }

    public boolean Assig3(int r, int c, String[] words, int wordCt, char [][] bo,int dir)
    {
        //System.out.println( "Assig3 "+r+","+c+" "+wordCt+" Dir:"+dir);
        if((wordCt*4)<posList.size()) //backtracking
        {
            int endC=posList.remove(posList.size()-1);   //removes the last two coordinates added
            int endR=posList.remove(posList.size()-1);
            int startC=posList.remove(posList.size()-1);
            int startR=posList.remove(posList.size()-1);

            if(startR==endR)//these FOR loops and IF statements figure out if the word is all within the same column or row and then changes all of the letters in the word to lowercase
            {
                if(startC>endC)
                {
                    for(int p=endC;p<=startC;p++)
                    {
                        bo[startR][p] = Character.toLowerCase(bo[startR][p]);
                    }
                }
                if(startC<endC)
                {
                    for(int p=startC;p<=endC;p++)
                    {
                        bo[startR][p] = Character.toLowerCase(bo[startR][p]);
                    }
                }
            } 
            else
            if(startC==endC)
            {
                if(startR>endR)
                {
                    for(int p=endR;p<=startR;p++)
                    {
                        bo[p][startC] = Character.toLowerCase(bo[p][startC]);
                    }
                }
                if(startR<endR)
                {
                    for(int p=startR;p<=endR;p++)
                    {
                        bo[p][startC] = Character.toLowerCase(bo[p][startC]);
                    }
                }
            }
        }

        if(wordCt==words.length) //if all words have been found
        {
            return true;
        }
        String word=words[wordCt];
        int direction=dir;

        boolean answer=findWord(r,c,word,0,bo,direction); //right direction
        if(wordCt==0)
        {
            if(!answer)//down
            {
                direction++;
                if(direction>4)
                    direction=1;
                answer=findWord(r,c,word,0,bo,direction);
            }
            if(!answer)//left
            {
                direction++;
                if(direction>4)
                    direction=1;
                answer=findWord(r,c,word,0,bo,direction);
            }
            if(!answer)//up
            {
                direction++;
                if(direction>4)
                    direction=1;
                answer=findWord(r,c,word,0,bo,direction);
            }
        }

        if(answer)
        {
            wordCt++;

            posList.add(r);posList.add(c); //add starting position of current word to list
            if(direction==1)
                c=c+(word.length()-1);
            if(direction==2)
                r=r+(word.length()-1);
            if(direction==3)
                c=c-(word.length()-1);
            if(direction==4)
                r=r-(word.length()-1);
            posList.add(r);posList.add(c); //add ending point of word

            answer=Assig3(r,c+1,words,wordCt,bo,1); //right
            if(!answer)//down
                answer=Assig3(r+1,c,words,wordCt,bo,2);
            if(!answer)//left
                answer=Assig3(r,c-1,words,wordCt,bo,3);
            if(!answer)//up
                answer=Assig3(r-1,c,words,wordCt,bo,4);
        }
        return answer;
    }
}