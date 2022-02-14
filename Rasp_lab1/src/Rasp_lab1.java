import mpi.*;
public class Rasp_lab1 {

    public static void main(String[] args) throws Exception{

        int tag = 0;
        int message;
        MPI.Init(args);
        int rank = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();
        message = rank;
        int[] arr = { message };
        if ((rank % 2) == 0)
        {
            if((rank + 1) != size)
            {
                MPI.COMM_WORLD.Send(arr, 0, arr.length, MPI.INT, rank + 1, tag);

            }
        }else
            {  if(rank != 0)
                    MPI.COMM_WORLD.Recv(arr, 0, arr.length, MPI.INT, rank - 1, tag);
                System.out.println(message + " receive from "+ (rank-1) +"\n");
            }
        MPI.Finalize();
    }
}
