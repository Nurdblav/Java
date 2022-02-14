import mpi.*;
import java.util.*;
public class Rasp_lab8 {
    public static void main(String[] args) throws Exception {
        Random random = new Random(10);
        int matrixSize = 500;
//        int[][] matrix = {
//                {0,1,1,0,0,0,0,0,0,0,0,0,1,0},
//                {1,0,0,1,0,0,0,0,0,0,0,0,0,1},
//                {1,0,0,1,1,0,0,0,0,0,0,0,0,0},
//                {0,1,1,0,0,1,0,0,0,0,0,0,0,0},
//                {0,0,1,0,0,1,1,0,0,0,0,0,0,0},
//                {0,0,0,1,1,0,0,1,0,0,0,0,0,0},
//                {0,0,0,0,1,0,0,1,1,0,0,0,0,0},
//                {0,0,0,0,0,1,1,0,0,1,0,0,0,0},
//                {0,0,0,0,0,0,1,0,0,1,1,0,0,0},
//                {0,0,0,0,0,0,0,1,1,0,0,1,0,0},
//                {0,0,0,0,0,0,0,0,1,0,0,1,1,0},
//                {0,0,0,0,0,0,0,0,0,1,1,0,0,1},
//                {1,0,0,0,0,0,0,0,0,0,1,0,0,1},
//                {0,1,0,0,0,0,0,0,0,0,0,1,1,0},
//        }
        int[][] matrix = new int[matrixSize][matrixSize];
        for (int i = 0; i < matrixSize; i++) {
            for (int j = i + 1; j < matrixSize; j++) {
                int value = random.nextInt(2);
                matrix[i][j] = value;
                matrix[j][i] = value;
            }
        }
        MPI.Init(args);
        int rank = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();
        long progTime = 0;

        int[][] sendMatrix = new int[matrixSize][matrixSize];

        if (rank == 0) {
//            for (int i = 0; i < matrixSize; ++i) {
//                for (int j = 0; j < matrixSize; ++j) {
//                    System.out.print(matrix[i][j]+" ");
//                }
//                System.out.println();
//            }
//            System.out.println();
            sendMatrix = matrix;
            progTime = System.currentTimeMillis();
        }

        MPI.COMM_WORLD.Bcast(sendMatrix, 0, matrixSize, MPI.OBJECT, 0);

        int[][] tempMatrix;
        int start = 0;
        int end = 0;

        if (rank == size - 1) {
            start = (matrixSize / (size)) * (rank);
            end = sendMatrix.length;
        } else {
            start = (matrixSize / (size)) * (rank);
            end = (matrixSize / (size)) * (rank + 1);
        }

        boolean[] tempDegree = {torusCheck(sendMatrix,start,end)};
        boolean[] result = new boolean[1];
        MPI.COMM_WORLD.Reduce(tempDegree, 0, result, 0, tempDegree.length, MPI.BOOLEAN, MPI.LAND, 0);

        if (rank == 0){
            progTime = System.currentTimeMillis() - progTime;
            if (result[0]){
                System.out.println("Граф является тором");
                System.out.println("Время работы программы: " + progTime);
            }
            else{
                System.out.println("Граф не является тором");
                System.out.println("Время работы программы: " + progTime);
            }
        }
        MPI.Finalize();
    }

    public static boolean torusCheck (int[][] matrix, int start, int end){
        boolean check = true;
        for (int i = start; i<end; i++){
            for (int j = 0; j < matrix.length; j++){
                if (i == 0){
                    if ((j == matrix.length-2) || (j == i+1) || (j == i+2)) {
                        if (matrix[i][j] != 1){
                            check = false;
                        }
                    }
                    else if (matrix[i][j] == 1){
                        check = false;
                    }
                }
                else if (i == matrix.length-2){
                    if ((j == i-2) || (j == i+1) || (j == 0))  {
                        if (matrix[i][j] != 1) {
                            check = false;
                        }
                    }
                    else if (matrix[i][j] == 1){
                        check = false;
                    }
                }
                else if (i == 1){
                    if ((j == matrix.length-1) || (j == i-1) || (j == i+2))  {
                        if (matrix[i][j] != 1) {
                            check = false;
                        }
                    }
                    else if (matrix[i][j] == 1){
                        check = false;
                    }
                }
                else if (i == matrix.length-1){
                    if ((j == i-2) || (j == i-1) || (j == 1))  {
                        if (matrix[i][j] != 1) {
                            check = false;
                        }
                    }
                    else if (matrix[i][j] == 1){
                        check = false;
                    }
                }
                else if (i % 2 == 0){
                    if ((j == i-2) || (j == i+1) || (j == i+2))  {
                        if (matrix[i][j] != 1) {
                            check = false;
                        }
                    }
                    else if (matrix[i][j] == 1){
                        check = false;
                    }
                }
                else {
                    if ((j == i-2) || (j == i-1) || (j == i+2))   {
                        if (matrix[i][j] != 1) {
                            check = false;
                        }
                    }
                    else if (matrix[i][j] == 1){
                        check = false;
                    }
                }
            }
        }
        return check;
    }
}
