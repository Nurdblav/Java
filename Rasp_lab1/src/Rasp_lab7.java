import mpi.*;
import java.util.*;

public class Rasp_lab7 {
    public static void main(String[] args) throws Exception {
        Random random = new Random(10);
        int matrixSize = 1000;
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
//            sendMatrix = matrix;
            progTime = System.currentTimeMillis();
        }

        MPI.COMM_WORLD.Bcast(sendMatrix, 0, matrixSize, MPI.OBJECT, 0);

        int[][] tempMatrix;
        int start = 0;
        if (rank == size - 1) {
            tempMatrix = Arrays.copyOfRange(sendMatrix, (matrixSize / (size)) * (rank), sendMatrix.length);
            start = (matrixSize / (size)) * (rank);
        } else {
            start = (matrixSize / (size)) * (rank);
            tempMatrix = Arrays.copyOfRange(sendMatrix, (matrixSize / (size)) * (rank), ((matrixSize / (size)) * (rank + 1)));
        }

        int[] tempDegree = {maxDegree(tempMatrix,matrixSize,start)};
        int[] result = new int[1];
        MPI.COMM_WORLD.Reduce(tempDegree, 0, result, 0, tempDegree.length, MPI.INT, MPI.MAX, 0);

        if (rank == 0){
            System.out.println("Максимальная степень вершины в данном граффе " + result[0]);
            progTime = System.currentTimeMillis() - progTime;
            System.out.println("Время работы программы: " + progTime);
        }
    }

    public static int maxDegree (int[][] matrixPart, int matrixSize, int start){
        int maxDegree = 0;
        int curDegree = 0;
        for (int i = 0; i < matrixPart.length; ++i) {
            curDegree = 0;
            for (int j = 0; j < matrixSize; ++j) {
                if (matrixPart[i][start] == 1 ){
                    curDegree+=2;
                }
                else if (matrixPart[i][j] == 1){
                    curDegree++;
                }
            }
            if (curDegree > maxDegree){
                maxDegree = curDegree;
            }
            start++;
        }
        return maxDegree;
    }
}
