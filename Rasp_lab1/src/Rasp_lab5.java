//Стандартный блокирующий обмен (Send)

import mpi.*;
import java.util.*;
public class Rasp_lab5 {
    public static void main(String[] args) throws Exception {
        Random random = new Random(10);
        int vector_size = 100;
        int[] vector = new int[vector_size];
        int[][] matrix = new int[vector_size][vector_size];
        for (int i = 0; i < vector_size; ++i) {
            vector[i] = random.nextInt(11);
//            System.out.print(vector[i]+" ");
        }
//        System.out.println();
//        System.out.println("----");
        for (int i = 0; i < vector_size; ++i) {
            for (int j = 0; j < vector_size; ++j){
                matrix[i][j] = random.nextInt(11);
//                System.out.print(matrix[i][j]+" ");
            }
//            System.out.println();
        }
        int tag = 0;
        MPI.Init(args);
        int rank = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();
        if (size == 2){
            if ( rank == 0){
                long progTime = System.currentTimeMillis();
                int[][] sMatrix = matrix;
                int[] sVector = vector;
                MPI.COMM_WORLD.Send(sMatrix, 0, sMatrix.length, MPI.OBJECT, 1, tag);
                MPI.COMM_WORLD.Send(sVector, 0, sVector.length, MPI.INT, 1, 1);
                int[] resultVector = new int[vector_size];
                MPI.COMM_WORLD.Recv(resultVector,0,vector_size,MPI.INT,1,tag);
                progTime = System.currentTimeMillis() - progTime;
                System.out.println("Время выполнения программы с стандартным блокирующим обменом: "+ progTime);
//                for (int elem: resultVector) {
//                    System.out.println(elem);
//                }
            }
            else{
                int[][] tempMatrix = new int[vector_size][vector_size];
                int[] tempVector = new int [vector_size];
                MPI.COMM_WORLD.Recv(tempMatrix,0,vector_size,MPI.OBJECT,0,tag);
                MPI.COMM_WORLD.Recv(tempVector,0,vector_size,MPI.INT,0,1);
                int[] result = calcMatrix(tempMatrix,tempVector);
                MPI.COMM_WORLD.Send(result, 0, result.length, MPI.INT, 0, tag);
            }
        }
        else if (vector_size % (size-1) == 0){
            if (rank != 0){
                int[][] tempMatrix = new int[(vector_size/(size-1))][vector_size];
                int[] tempVector = new int[vector_size];
                MPI.COMM_WORLD.Recv(tempMatrix,0,(vector_size/(size-1)),MPI.OBJECT,0,0);
                MPI.COMM_WORLD.Recv(tempVector,0,vector_size,MPI.INT,0,1);
                int [] message = calcMatrix(tempMatrix,tempVector);
                MPI.COMM_WORLD.Send(message, 0, message.length, MPI.INT, 0, tag);
            }
            else{
                long progTime = System.currentTimeMillis();
                for (int i = 0; i < size - 1; i++) {
                    int dest = i + 1;
                    int[][] message = Arrays.copyOfRange(matrix, (vector_size/(size-1))*i, ((vector_size/(size-1))*(i + 1)));

                    MPI.COMM_WORLD.Send(message, 0, message.length, MPI.OBJECT, dest, 0);
                    MPI.COMM_WORLD.Send(vector, 0, vector_size, MPI.INT, dest, 1);
                }
                int[] resVector = new int[vector_size];
                Request[] requests = new Request[size-1];
                for (int proc = 0; proc < size-1; ++proc) {
                    int source = proc+1;
                    int off = (vector_size/(size-1))*(source-1);
                    int mesSize = (vector_size/(size-1));
                    requests[proc] = MPI.COMM_WORLD.Irecv(resVector, off, mesSize, MPI.INT, source, tag);
                }
                Request.Waitall(requests);
                progTime = System.currentTimeMillis() - progTime;
                System.out.println("Время выполнения программы с стандартным блокирующим обменом: "+ progTime);
//                for (int elem: resVector) {
//                    System.out.println(elem);
//                }
            }
        }
        else if (vector_size % (size-1) != 0){
            if (rank != 0){
                int[][] tempMatrix = new int[(vector_size/(size-1))][vector_size];
                int[] tempVector = new int[vector_size];
                MPI.COMM_WORLD.Recv(tempMatrix,0,(vector_size/(size-1)),MPI.OBJECT,0,0);
                MPI.COMM_WORLD.Recv(tempVector,0,vector_size,MPI.INT,0,1);
                int [] message = calcMatrix(tempMatrix,tempVector);
                MPI.COMM_WORLD.Send(message, 0, message.length, MPI.INT, 0, tag);
            }
            else{
                long progTime = System.currentTimeMillis();
                for (int i = 0; i < size - 1; i++) {
                    int dest = i + 1;
                    int[][] message = Arrays.copyOfRange(matrix, (vector_size/(size-1))*i, ((vector_size/(size-1))*(i + 1)));

                    MPI.COMM_WORLD.Send(message, 0, message.length, MPI.OBJECT, dest, 0);
                    MPI.COMM_WORLD.Send(vector, 0, vector_size, MPI.INT, dest, 1);
                }
                int[] resVector = new int[vector_size];
                int[][] tempMatrix = Arrays.copyOfRange(matrix, (vector_size/(size-1))*(size-1), vector_size);
                int[] tempVector = calcMatrix(tempMatrix,vector);
                for (int i = 0; i < tempVector.length; ++i){
                    resVector[(vector_size/(size-1))*(size-1)+i] = tempVector[i];
                }
                Request[] requests = new Request[size-1];
                for (int proc = 0; proc < size-1; ++proc) {
                    int source = proc+1;
                    int off = (vector_size/(size-1))*(source-1);
                    int mesSize = (vector_size/(size-1));
                    requests[proc] = MPI.COMM_WORLD.Irecv(resVector, off, mesSize, MPI.INT, source, tag);
                }
                Request.Waitall(requests);
                progTime = System.currentTimeMillis() - progTime;
                System.out.println("Время выполнения программы с стандартным блокирующим обменом: "+ progTime);
//                for (int elem: resVector) {
//                    System.out.println(elem);
//                }
            }
        }
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




//Синхронный блокирующий обмен  (Ssend)

//import mpi.*;
//        import java.util.*;
//public class Rasp_lab5 {
//    public static void main(String[] args) throws Exception {
//        Random random = new Random(10);
//        int vector_size = 100;
//        int[] vector = new int[vector_size];
//        int[][] matrix = new int[vector_size][vector_size];
//        for (int i = 0; i < vector_size; ++i) {
//            vector[i] = random.nextInt(11);
////            System.out.print(vector[i]+" ");
//        }
////        System.out.println();
////        System.out.println("----");
//        for (int i = 0; i < vector_size; ++i) {
//            for (int j = 0; j < vector_size; ++j){
//                matrix[i][j] = random.nextInt(11);
////                System.out.print(matrix[i][j]+" ");
//            }
////            System.out.println();
//        }
//        int tag = 0;
//        MPI.Init(args);
//        int rank = MPI.COMM_WORLD.Rank();
//        int size = MPI.COMM_WORLD.Size();
//        if (size == 2){
//            if ( rank == 0){
//                long progTime = System.currentTimeMillis();
//                int[][] sMatrix = matrix;
//                int[] sVector = vector;
//                MPI.COMM_WORLD.Ssend(sMatrix, 0, sMatrix.length, MPI.OBJECT, 1, tag);
//                MPI.COMM_WORLD.Ssend(sVector, 0, sVector.length, MPI.INT, 1, 1);
//                int[] resultVector = new int[vector_size];
//                MPI.COMM_WORLD.Recv(resultVector,0,vector_size,MPI.INT,1,tag);
//                progTime = System.currentTimeMillis() - progTime;
//                System.out.println("Время выполнения программы с стандартным блокирующим обменом: "+ progTime);
////                for (int elem: resultVector) {
////                    System.out.println(elem);
////                }
//            }
//            else{
//                int[][] tempMatrix = new int[vector_size][vector_size];
//                int[] tempVector = new int [vector_size];
//                MPI.COMM_WORLD.Recv(tempMatrix,0,vector_size,MPI.OBJECT,0,tag);
//                MPI.COMM_WORLD.Recv(tempVector,0,vector_size,MPI.INT,0,1);
//                int[] result = calcMatrix(tempMatrix,tempVector);
//                MPI.COMM_WORLD.Ssend(result, 0, result.length, MPI.INT, 0, tag);
//            }
//        }
//        if (size == 3 | size ==11){
//            if (rank != 0){
//                int[][] tempMatrix = new int[(vector_size/(size-1))][vector_size];
//                int[] tempVector = new int[vector_size];
//                MPI.COMM_WORLD.Recv(tempMatrix,0,(vector_size/(size-1)),MPI.OBJECT,0,0);
//                MPI.COMM_WORLD.Recv(tempVector,0,vector_size,MPI.INT,0,1);
//                int [] message = calcMatrix(tempMatrix,tempVector);
//                MPI.COMM_WORLD.Ssend(message, 0, message.length, MPI.INT, 0, tag);
//            }
//            else{
//                long progTime = System.currentTimeMillis();
//                for (int i = 0; i < size - 1; i++) {
//                    int dest = i + 1;
//                    int[][] message = Arrays.copyOfRange(matrix, (vector_size/(size-1))*i, ((vector_size/(size-1))*(i + 1)));
//
//                    MPI.COMM_WORLD.Ssend(message, 0, message.length, MPI.OBJECT, dest, 0);
//                    MPI.COMM_WORLD.Ssend(vector, 0, vector_size, MPI.INT, dest, 1);
//                }
//                int[] resVector = new int[vector_size];
//                Request[] requests = new Request[size-1];
//                for (int proc = 0; proc < size-1; ++proc) {
//                    int source = proc+1;
//                    int off = (vector_size/(size-1))*(source-1);
//                    int mesSize = (vector_size/(size-1));
//                    requests[proc] = MPI.COMM_WORLD.Irecv(resVector, off, mesSize, MPI.INT, source, tag);
//                }
//                Request.Waitall(requests);
//                progTime = System.currentTimeMillis() - progTime;
//                System.out.println("Время выполнения программы с синхронный блокирующим обменом: "+ progTime);
////                for (int elem: resVector) {
////                    System.out.println(elem);
////                }
//            }
//        }
//        if (size == 8){
//            if (rank != 0){
//                int[][] tempMatrix = new int[(vector_size/(size-1))][vector_size];
//                int[] tempVector = new int[vector_size];
//                MPI.COMM_WORLD.Recv(tempMatrix,0,(vector_size/(size-1)),MPI.OBJECT,0,0);
//                MPI.COMM_WORLD.Recv(tempVector,0,vector_size,MPI.INT,0,1);
//                int [] message = calcMatrix(tempMatrix,tempVector);
//                MPI.COMM_WORLD.Ssend(message, 0, message.length, MPI.INT, 0, tag);
//            }
//            else{
//                long progTime = System.currentTimeMillis();
//                for (int i = 0; i < size - 1; i++) {
//                    int dest = i + 1;
//                    int[][] message = Arrays.copyOfRange(matrix, (vector_size/(size-1))*i, ((vector_size/(size-1))*(i + 1)));
//
//                    MPI.COMM_WORLD.Ssend(message, 0, message.length, MPI.OBJECT, dest, 0);
//                    MPI.COMM_WORLD.Ssend(vector, 0, vector_size, MPI.INT, dest, 1);
//                }
//                int[] resVector = new int[vector_size];
//                int[][] tempMatrix = Arrays.copyOfRange(matrix, (vector_size/(size-1))*(size-1), vector_size);
//                int[] tempVector = calcMatrix(tempMatrix,vector);
//                for (int i = 0; i < tempVector.length; ++i){
//                    resVector[(vector_size/(size-1))*(size-1)+i] = tempVector[i];
//                }
//                Request[] requests = new Request[size-1];
//                for (int proc = 0; proc < size-1; ++proc) {
//                    int source = proc+1;
//                    int off = (vector_size/(size-1))*(source-1);
//                    int mesSize = (vector_size/(size-1));
//                    requests[proc] = MPI.COMM_WORLD.Irecv(resVector, off, mesSize, MPI.INT, source, tag);
//                }
//                Request.Waitall(requests);
//                progTime = System.currentTimeMillis() - progTime;
//                System.out.println("Время выполнения программы с синхронным блокирующим обменом: "+ progTime);
////                for (int elem: resVector) {
////                    System.out.println(elem);
////                }
//            }
//        }
//    }
//
//    public static int[] calcMatrix (int[][] matrixPart, int[] vector){
//        int[] result = new int[matrixPart.length];
//        for (int i=0; i < matrixPart.length; ++i){
//            for (int j=0; j < vector.length; ++j ){
//                result[i] += matrixPart[i][j]*vector[j];
//            }
//        }
//        return result;
//    }
//}


//Блокирующий обмен по готовности (Rsend)

//import mpi.*;
//import java.util.*;
//public class Rasp_lab5 {
//    public static void main(String[] args) throws Exception {
//        Random random = new Random(10);
//        int vector_size = 100;
//        int[] vector = new int[vector_size];
//        int[][] matrix = new int[vector_size][vector_size];
//        for (int i = 0; i < vector_size; ++i) {
//            vector[i] = random.nextInt(11);
////            System.out.print(vector[i]+" ");
//        }
////        System.out.println();
////        System.out.println("----");
//        for (int i = 0; i < vector_size; ++i) {
//            for (int j = 0; j < vector_size; ++j){
//                matrix[i][j] = random.nextInt(11);
////                System.out.print(matrix[i][j]+" ");
//            }
////            System.out.println();
//        }
//        int tag = 0;
//        MPI.Init(args);
//        int rank = MPI.COMM_WORLD.Rank();
//        int size = MPI.COMM_WORLD.Size();
//        if (size == 2){
//            if ( rank == 0){
//                long progTime = System.currentTimeMillis();
//                int[][] sMatrix = matrix;
//                int[] sVector = vector;
//                MPI.COMM_WORLD.Rsend(sMatrix, 0, sMatrix.length, MPI.OBJECT, 1, tag);
//                MPI.COMM_WORLD.Rsend(sVector, 0, sVector.length, MPI.INT, 1, 1);
//                int[] resultVector = new int[vector_size];
//                MPI.COMM_WORLD.Recv(resultVector,0,vector_size,MPI.INT,1,tag);
//                progTime = System.currentTimeMillis() - progTime;
//                System.out.println("Время выполнения программы с стандартным блокирующим обменом: "+ progTime);
////                for (int elem: resultVector) {
////                    System.out.println(elem);
////                }
//            }
//            else{
//                int[][] tempMatrix = new int[vector_size][vector_size];
//                int[] tempVector = new int [vector_size];
//                MPI.COMM_WORLD.Recv(tempMatrix,0,vector_size,MPI.OBJECT,0,tag);
//                MPI.COMM_WORLD.Recv(tempVector,0,vector_size,MPI.INT,0,1);
//                int[] result = calcMatrix(tempMatrix,tempVector);
//                MPI.COMM_WORLD.Rsend(result, 0, result.length, MPI.INT, 0, tag);
//            }
//        }
//        if (size == 3 | size ==11){
//            if (rank != 0){
//                int[][] tempMatrix = new int[(vector_size/(size-1))][vector_size];
//                int[] tempVector = new int[vector_size];
//                MPI.COMM_WORLD.Recv(tempMatrix,0,(vector_size/(size-1)),MPI.OBJECT,0,0);
//                MPI.COMM_WORLD.Recv(tempVector,0,vector_size,MPI.INT,0,1);
//                int [] message = calcMatrix(tempMatrix,tempVector);
//                MPI.COMM_WORLD.Rsend(message, 0, message.length, MPI.INT, 0, tag);
//            }
//            else{
//                long progTime = System.currentTimeMillis();
//                for (int i = 0; i < size - 1; i++) {
//                    int dest = i + 1;
//                    int[][] message = Arrays.copyOfRange(matrix, (vector_size/(size-1))*i, ((vector_size/(size-1))*(i + 1)));
//
//                    MPI.COMM_WORLD.Rsend(message, 0, message.length, MPI.OBJECT, dest, 0);
//                    MPI.COMM_WORLD.Rsend(vector, 0, vector_size, MPI.INT, dest, 1);
//                }
//                int[] resVector = new int[vector_size];
//                Request[] requests = new Request[size-1];
//                for (int proc = 0; proc < size-1; ++proc) {
//                    int source = proc+1;
//                    int off = (vector_size/(size-1))*(source-1);
//                    int mesSize = (vector_size/(size-1));
//                    requests[proc] = MPI.COMM_WORLD.Irecv(resVector, off, mesSize, MPI.INT, source, tag);
//                }
//                Request.Waitall(requests);
//                progTime = System.currentTimeMillis() - progTime;
//                System.out.println("Время выполнения программы с блокирующим обменом по готовности: "+ progTime);
////                for (int elem: resVector) {
////                    System.out.println(elem);
////                }
//            }
//        }
//        if (size == 8){
//            if (rank != 0){
//                int[][] tempMatrix = new int[(vector_size/(size-1))][vector_size];
//                int[] tempVector = new int[vector_size];
//                MPI.COMM_WORLD.Recv(tempMatrix,0,(vector_size/(size-1)),MPI.OBJECT,0,0);
//                MPI.COMM_WORLD.Recv(tempVector,0,vector_size,MPI.INT,0,1);
//                int [] message = calcMatrix(tempMatrix,tempVector);
//                MPI.COMM_WORLD.Rsend(message, 0, message.length, MPI.INT, 0, tag);
//            }
//            else{
//                long progTime = System.currentTimeMillis();
//                for (int i = 0; i < size - 1; i++) {
//                    int dest = i + 1;
//                    int[][] message = Arrays.copyOfRange(matrix, (vector_size/(size-1))*i, ((vector_size/(size-1))*(i + 1)));
//
//                    MPI.COMM_WORLD.Rsend(message, 0, message.length, MPI.OBJECT, dest, 0);
//                    MPI.COMM_WORLD.Rsend(vector, 0, vector_size, MPI.INT, dest, 1);
//                }
//                int[] resVector = new int[vector_size];
//                int[][] tempMatrix = Arrays.copyOfRange(matrix, (vector_size/(size-1))*(size-1), vector_size);
//                int[] tempVector = calcMatrix(tempMatrix,vector);
//                for (int i = 0; i < tempVector.length; ++i){
//                    resVector[(vector_size/(size-1))*(size-1)+i] = tempVector[i];
//                }
//                Request[] requests = new Request[size-1];
//                for (int proc = 0; proc < size-1; ++proc) {
//                    int source = proc+1;
//                    int off = (vector_size/(size-1))*(source-1);
//                    int mesSize = (vector_size/(size-1));
//                    requests[proc] = MPI.COMM_WORLD.Irecv(resVector, off, mesSize, MPI.INT, source, tag);
//                }
//                Request.Waitall(requests);
//                progTime = System.currentTimeMillis() - progTime;
//                System.out.println("Время выполнения программы с блокирующим обменом по готовности: "+ progTime);
////                for (int elem: resVector) {
////                    System.out.println(elem);
////                }
//            }
//        }
//    }
//
//    public static int[] calcMatrix (int[][] matrixPart, int[] vector){
//        int[] result = new int[matrixPart.length];
//        for (int i=0; i < matrixPart.length; ++i){
//            for (int j=0; j < vector.length; ++j ){
//                result[i] += matrixPart[i][j]*vector[j];
//            }
//        }
//        return result;
//    }
//}


