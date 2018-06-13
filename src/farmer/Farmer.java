package farmer;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class Farmer implements IFarmer, Serializable {



    int workersCount;
    String address;
    String workerPrefix;

    List<IWorker> workers;

    @Override
    public int calculate(Integer[] arr) {
        ArrayList<Integer> temp  = new ArrayList<Integer>(Arrays.asList(arr));

        int chunkSize = (int)Math.ceil(temp.size() / (double)workersCount);
        AtomicInteger counter = new AtomicInteger(0);

        Collection<List<Integer>> result =
                temp.stream().collect(Collectors.groupingBy(it ->
                        counter.getAndIncrement() / chunkSize))
                        .values();

        Iterator it = result.iterator();

        Integer[] results = workers.stream()
                .map(worker -> {
                    List<Integer> current = (List<Integer>) it.next();
                    Integer[] currArr = current.toArray(new Integer[current.size()]);
                    return worker.getMin(currArr);
                })
                .toArray(Integer[]::new);

       return Arrays.stream(results)
               .min(Integer::compare)
               .get();

    }

    public Farmer(int count, String addr, String prefix){
        workerPrefix = prefix;
        workersCount = count;
        address = addr;

        workers = Stream.generate(Worker::new).limit(count).collect(Collectors.toList());

        for(int i = 0; i < workersCount; i++){

            try {
                IWorker worker = (IWorker) java.rmi.Naming.lookup(address + "/" + workerPrefix + i);
                workers.set(i, worker );
            } catch (NotBoundException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }







}
