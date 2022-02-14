////Broadcast and Reduce

import mpi.*;
import java.util.*;
public class Rasp_lab6 {
    public static void main(String[] args) throws Exception {
        Random random = new Random(10);
        int vector_size = 1000;
        int[] vector = new int[vector_size];
        int[][] matrix = new int[vector_size][vector_size];
//        for (int i = 0; i < vector_size; ++i) {
//            vector[i] = random.nextInt(11);
//            System.out.print(vector[i] + " ");
//        }
//        System.out.println();
//        System.out.println("----");
        for (int i = 0; i < vector_size; ++i) {
            for (int j = 0; j < vector_size; ++j) {
                matrix[i][j] = random.nextInt(11);
//                System.out.print(matrix[i][j] + " ");
            }
//            System.out.println();
        }
        int tag = 0;
        MPI.Init(args);
        int rank = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();
        long progTime = 0;
        int[][] sendMatrix = new int[vector_size][vector_size];
        int[] sendVector = new int[vector_size];
        if (rank == 0) {
            sendMatrix = matrix;
            sendVector = vector;
            progTime = System.currentTimeMillis();
        }
        MPI.COMM_WORLD.Bcast(sendMatrix, 0, vector_size, MPI.OBJECT, 0);
        MPI.COMM_WORLD.Bcast(sendVector, 0, vector_size, MPI.INT, 0);

        int[][] tempMatrix;

        if (rank == size - 1) {
            tempMatrix = Arrays.copyOfRange(sendMatrix, (vector_size / (size)) * (rank), sendMatrix.length);
        } else {
            tempMatrix = Arrays.copyOfRange(sendMatrix, (vector_size / (size)) * (rank), ((vector_size / (size)) * (rank + 1)));
        }

        int[] resultProc = calcMatrix(tempMatrix, sendVector);
        int[] resultVector = new int[vector_size];
        int step = ((vector_size / (size)) * (rank));
        for (int i : resultProc) {
            resultVector[step] = i;
            step++;
        }
//        System.out.println("Процесс: "+rank);
//        for (int i : resultProc){
//            System.out.println(i);
//        }
        int[] result = new int[vector_size];
        MPI.COMM_WORLD.Reduce(resultVector, 0, result, 0, resultVector.length, MPI.INT, MPI.SUM, size - 1);

//        for (int i : resultProc){
//            System.out.println(i);
//        }
        if (rank == 0){
            progTime = System.currentTimeMillis() - progTime;
            System.out.println("Время работы программы: " + progTime);
        }
//        if (rank == size - 1) {
//            System.out.println("Результат: ");
//            for (int i : result) {
//                System.out.println(i);
//            }
//        }
        MPI.Finalize();
    }

    public static int[] calcMatrix (int[][] matrixPart, int[] vector){
        int[] result = new int[matrixPart.length];
        for (int i=0; i < matrixPart.length; ++i){
            for (int j=0; j < vector.length; ++j ){
                result[i] += matrixPart[i][j]*vector[j];
            }
        }
        return result;
    }
}

//Стандартный блокирующий обмен (Send)

//import mpi.*;
//import java.util.*;
//public class Rasp_lab6 {
//    public static void main(String[] args) throws Exception {
//        Random random = new Random(10);
//        int vector_size = 1000;
//        int[] vector = new int[vector_size];
//        int[][] matrix = new int[vector_size][vector_size];
////        for (int i = 0; i < vector_size; ++i) {
////            vector[i] = random.nextInt(11);
////            System.out.print(vector[i] + " ");
////        }
////        System.out.println();
////        System.out.println("----");
////        for (int i = 0; i < vector_size; ++i) {
////            for (int j = 0; j < vector_size; ++j) {
////                matrix[i][j] = random.nextInt(11);
////                System.out.print(matrix[i][j] + " ");
////            }
////            System.out.println();
////        }
//        int tag = 0;
//        MPI.Init(args);
//        int rank = MPI.COMM_WORLD.Rank();
//        int size = MPI.COMM_WORLD.Size();
//        int[][] sendMatrix = new int[vector_size][vector_size];
//        int[] sendVector = new int[vector_size];
//        int[] sendCount = new int[size];
//        int[] displs = new int[size];
//        long progTime = 0;
//        if (rank == 0) {
//            sendMatrix = matrix;
//            sendVector = vector;
//            sendCount = new int[size];
//            progTime = System.currentTimeMillis();
//        }
//        for (int i = 0; i < sendCount.length; i++) {
//            if (i == 0) {
//                sendCount[i] = vector_size - (vector_size / size) * (size - 1);
//            } else {
//                sendCount[i] = vector_size / size;
//            }
//        }
//        displs[0] = 0;
//        for (int i = 1; i < sendCount.length; i++) {
//            displs[i] = displs[i - 1] + sendCount[i - 1];
//        }
//
//        int[][] recvMatrix= new int[sendCount[rank]][vector_size];
//        MPI.COMM_WORLD.Scatterv(sendMatrix, 0, sendCount, displs, MPI.OBJECT, recvMatrix,0,recvMatrix.length,MPI.OBJECT,0 );
//        MPI.COMM_WORLD.Bcast(sendVector, 0, vector_size, MPI.INT, 0);
//
//        int[] resultProc = calcMatrix(recvMatrix, sendVector);
//
////        System.out.println("Процесс: "+rank);
////        for (int i : resultProc){
////            System.out.println(i);
////            }
//        int[] result = new int[vector_size];
//        MPI.COMM_WORLD.Gatherv(resultProc,0,resultProc.length,MPI.INT,result,0,sendCount,displs,MPI.INT,0);
//
////        for (int i : resultProc){
////            System.out.println(i);
////        }
//        if (rank == 0) {
//            progTime = System.currentTimeMillis() - progTime;
//            System.out.println("Время работы программы: " + progTime);
////            System.out.println("Результат: ");
////            for (int i : result) {
////                System.out.println(i);
////            }
//        }
//        MPI.Finalize();
//    }
//
//    public static int[] calcMatrix ( int[][] matrixPart, int[] vector){
//        int[] result = new int[matrixPart.length];
//        for (int i = 0; i < matrixPart.length; ++i) {
//            for (int j = 0; j < vector.length; ++j) {
//                result[i] += matrixPart[i][j] * vector[j];
//            }
//        }
//        return result;
//    }
//}