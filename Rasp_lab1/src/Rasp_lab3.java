import mpi.*;
import java.util.*;
public class Rasp_lab3 {

    public static void main(String[] args) throws Exception {

        int message = 0;
        int tag = 0;
        MPI.Init(args);
        int rank = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();
        if (size%2==0){
            if (rank!=0 & rank<size/2){
                Random random = new Random();
                message = random.nextInt(size);
                int[] arr = {message};
                System.out.println(rank + " send " + message + " to " + (size/2) + "\n");
                MPI.COMM_WORLD.Isend(arr, 0, arr.length, MPI.INT, size/2, tag);
            }
            if (rank!=0 & rank>size/2 & rank<size-1){
                Random random = new Random();
                message = random.nextInt(size);
                int[] arr = {message};
                MPI.COMM_WORLD.Isend(arr, 0, arr.length, MPI.INT, size-1, tag);
                System.out.println(rank + " send " + message + " to " + (size-1) + "\n");
            }
            if (rank==size/2 | rank==size-1){
                int arr_size = 0;
                if (rank==size/2){
                    arr_size = size/2 - 1;
                }
                else{
                    arr_size = size/2 - 2;
                }
                int[] rec_message = new int[arr_size];
                Request[] requests = new Request[arr_size];
                int first_source = rank-arr_size;
                for (int i = 0; i < arr_size; ++i) {
                    int source = first_source + i;
                    requests[i] = MPI.COMM_WORLD.Irecv(rec_message, i, 1, MPI.INT, source, tag);
                }
                Request.Waitall(requests);
                Arrays.sort(rec_message);
                MPI.COMM_WORLD.Isend(rec_message, 0, arr_size, MPI.INT, 0, tag);
                System.out.println(rank + " sent " + Arrays.toString(rec_message) + " to  0\n");
            }
            if (rank==0){
                int arr_size = size-3;
                int[] rec_message = new int[arr_size];
                Request[] requests = new Request[2];
                requests[0]=MPI.COMM_WORLD.Irecv(rec_message, 0, size/2-1, MPI.INT, size/2, tag);
                requests[1]=MPI.COMM_WORLD.Irecv(rec_message, size/2 - 1, size/2 - 2, MPI.INT, size-1, tag);
                Request.Waitall(requests);
                Arrays.sort(rec_message);
                System.out.println(rank + " show final result " + Arrays.toString(rec_message) + "\n");
            }
        }
        else{
            if (rank!=0 & rank<(size-1)/2){
                Random random = new Random();
                message = random.nextInt(size);
                int[] arr = {message};
                System.out.println(rank + " send " + message + " to " + ((size-1)/2) + "\n");
                MPI.COMM_WORLD.Isend(arr, 0, arr.length, MPI.INT, ((size-1)/2), tag);
            }
            if (rank!=0 & rank>(size-1)/2 & rank<(size-1)){
                Random random = new Random();
                message = random.nextInt(size);
                int[] arr = {message};
                MPI.COMM_WORLD.Isend(arr, 0, arr.length, MPI.INT, size-1, tag);
                System.out.println(rank + " send " + message + " to " + (size-1) + "\n");
            }
            if (rank==(size-1)/2 | rank==size-1){
                int arr_size = (size-1)/2 - 1 ;
                int[] rec_message = new int[arr_size];
                Request[] requests = new Request[arr_size];
                int first_source = rank-arr_size;
                for (int i = 0; i < arr_size; ++i) {
                    int source = first_source + i;
                    requests[i] = MPI.COMM_WORLD.Irecv(rec_message, i, 1, MPI.INT, source, tag);
                }
                Request.Waitall(requests);
                Arrays.sort(rec_message);
                MPI.COMM_WORLD.Isend(rec_message, 0, arr_size, MPI.INT, 0, tag);
                System.out.println(rank + " sent " + Arrays.toString(rec_message) + " to  0\n");
            }
            if (rank==0){
                int arr_size = size-3;
                int[] rec_message = new int[arr_size];
                Request[] requests = new Request[2];
                requests[0]=MPI.COMM_WORLD.Irecv(rec_message, 0, (size-1)/2 - 1, MPI.INT, (size-1)/2, tag);
                requests[1]=MPI.COMM_WORLD.Irecv(rec_message, (size-1)/2 - 1, (size-1)/2 - 1, MPI.INT, size-1, tag);
                Request.Waitall(requests);
                Arrays.sort(rec_message);
                System.out.println(rank + " show final result " + Arrays.toString(rec_message) + "\n");
            }
        }
        MPI.Finalize();
    }
}
