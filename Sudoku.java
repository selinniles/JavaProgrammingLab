import java.util.ArrayList;
import java.util.Random;

// Selin Samra - 2020313582

public class Sudoku {
    private final static int width = 9;
    private final static int height=9;

    private int[][] sudokuAnswer = new int[width][height];
    private int[][] sudokuPuzzle = new int[width][height];
    //add any methods or fields
    ArrayList<Integer> numbers = new ArrayList<Integer>();
    void getSquares(int row){
        if (row % 3 == 0){
            for(int k=0;k<width;k++)
            {
                int n=k+1;
                if (k+1==9){
                    n=k-8;
                }
                sudokuAnswer[row][k]=sudokuAnswer[row-1][n];
            }
        }
        else{
            for(int k=0;k<width;k++)
            {
                int n=k+3;
                if (k+3>8){
                    n=k-6;
                }
                sudokuAnswer[row][k]=sudokuAnswer[row-1][n];

            }
        }
    }
    Sudoku(){
        initSudoku();
        generateAnswer();
        generatePuzzle();
    }
    void initSudoku(){
        for (int i = 0;i<height;i++){
            for (int j=0;j<width;j++){
                sudokuAnswer[i][j] = 0;
                sudokuPuzzle[i][j] = 0;
            }
        }
    }
    void generateAnswer(){
        //your code
        Random rand = new Random();
        rand.setSeed(System.currentTimeMillis());
        int j=0;
        while(j<width){
            int num = 1 + rand.nextInt(9);
                if (!numbers.contains(num)){
                    numbers.add(num);
                    sudokuAnswer[0][j] = num;
                    j++;
                }
        }
        for (int i =1;i<9;i++){
            getSquares(i);
        }
    }
    void generatePuzzle(){
        //your code
        int counter=0;
        Random rand = new Random();
        rand.setSeed(System.currentTimeMillis());
        int start = rand.nextInt(9);
        start += 30;
        if (start%2!=0)
            start-=1;
        while(counter<start/2){
            int num_1 = rand.nextInt(4);
            int num_2 = rand.nextInt(9);
            if (sudokuPuzzle[num_1][num_2]==0){
                sudokuPuzzle[num_1][num_2]=sudokuAnswer[num_1][num_2];
                sudokuPuzzle[8-num_1][8-num_2]=sudokuAnswer[8-num_1][8-num_2];
                counter++;
            }
        }
        int rand_2 = 1 + rand.nextInt(2);
        int c=0;
        while(c < rand_2){
            int rand_4 = rand.nextInt(5);
            if (rand_4==4){
                sudokuPuzzle[4][4] = sudokuAnswer[4][4];
                c++;
            }
            else{
                sudokuPuzzle[4][rand_4]=sudokuAnswer[4][rand_4];
                sudokuPuzzle[4][8-rand_4]=sudokuAnswer[4][8-rand_4];
                c++;
            }
        }
    }
    void printSudoku(int[][] sudoku){
        for (int i =0;i<height;i++){
            if(i%3==0)
                System.out.print("+--------------------+\n");
            for (int j=0;j<width;j++){
                if (j%3==0)
                    System.out.print("|");
                System.out.print(sudoku[i][j] + " ");
            }
            System.out.print("|\n");
        }
        System.out.print("+--------------------+\n");
    }
    int[][] getAnswer(){
        return sudokuAnswer;
    }
    int[][] getPuzzle(){
        return sudokuPuzzle;
    }
    public static void main(String[] args){
        Sudoku sudoku = new Sudoku();

        System.out.println(" # Sudoku Puzzle #");
        sudoku.printSudoku(sudoku.getPuzzle());
        System.out.println("\n #Sudoku Answer #");
        sudoku.printSudoku(sudoku.getAnswer());
    }
}
