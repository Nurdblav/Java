import mpi.*;
public class Rasp_lab2 {

    public static void main(String[] args) throws Exception{

        int message = 0;
        int tag = 0;
        MPI.Init(args);
        int rank = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();

        int[] arr = { message };
        if (rank == 0)
        {
            arr[0] = arr[0]+rank;
            MPI.COMM_WORLD.Send(arr, 0, arr.length, MPI.INT, rank + 1, tag);
            MPI.COMM_WORLD.Recv(arr, 0, arr.length, MPI.INT, size - 1, tag);
            System.out.println(rank + " receive from "+ (size - 1) +". sum = " +arr[0]+ "\n");
        }
        else if (rank == size-1)
        {
            MPI.COMM_WORLD.Recv(arr, 0, arr.length, MPI.INT, rank - 1, tag);
            arr[0] = arr[0]+rank;
            System.out.println(rank + " receive from "+ (0) +". sum = " +arr[0]+ "\n");
            MPI.COMM_WORLD.Send(arr, 0, arr.length, MPI.INT, 0, tag);
        }
        else {
            MPI.COMM_WORLD.Recv(arr, 0, arr.length, MPI.INT, rank - 1, tag);
            arr[0] = arr[0]+rank;
            System.out.println(rank + " receive from "+ (rank-1) +". sum = " +arr[0]+ "\n");
            MPI.COMM_WORLD.Send(arr, 0, arr.length, MPI.INT, rank + 1, tag);
        }
        MPI.Finalize();
//        MPI.Init(args);
//        int rank = MPI.COMM_WORLD.Rank();
//        int size = MPI.COMM_WORLD.Size();
//
//        int[] arr = { message };
//        if (rank == 0)
//        {
//            arr[0] +=rank;
//            MPI.COMM_WORLD.Isend(arr, 0, arr.length, MPI.INT, rank + 1, tag);
//            Request request = MPI.COMM_WORLD.Irecv(arr, 0, arr.length, MPI.INT, size-1, tag);
//            request.Wait();
//            System.out.println(rank + " receive from "+ (size - 1) +". sum = " +arr[0]+ "\n");
//        }
//        else if (rank == size-1)
//        {
//            Request request = MPI.COMM_WORLD.Irecv(arr, 0, arr.length, MPI.INT, rank-1, tag);
//            request.Wait();
//            System.out.println(rank + " receive from "+ (rank-1) +". sum = " +arr[0]+ "\n");
//            arr[0] += rank;
//            MPI.COMM_WORLD.Isend(arr, 0, arr.length, MPI.INT, 0, tag);
//        }
//        else {
//            Request request = MPI.COMM_WORLD.Irecv(arr, 0, arr.length, MPI.INT, rank-1, tag);
//            request.Wait();
//            System.out.println(rank + " receive from "+ (rank-1) +". sum = " +arr[0]+ "\n");
//            arr[0] += rank;
//            MPI.COMM_WORLD.Isend(arr, 0, arr.length, MPI.INT, rank + 1, tag);
//        }
//        MPI.Finalize();
    }
}