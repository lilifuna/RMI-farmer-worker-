package farmer;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class CreateWorkers {

    public static void main(String[] args){

        String address = "/localhost:80";
        int numberOfWorkers = 3;

        if (System.getSecurityManager() == null) {
            String policy = "file:/C:/srv.policy";
            System.setProperty("java.security.policy", policy);
            System.setProperty("sun.rmi.registry.registryFilter", "java.**;rsi3.**");
            System.setSecurityManager(new SecurityManager());
        }

        try {
            LocateRegistry.createRegistry(1099);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        List<Worker> workers = Stream.generate(Worker::new).limit(numberOfWorkers).collect(Collectors.toList());

        IntStream.range(0, workers.size()).forEach(index -> {
            try {
                java.rmi.Naming.rebind(address + "/worker" + index, workers.get(index));
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        });

        Farmer farmer = new Farmer(numberOfWorkers, address, "worker");

        try {
            String name = address + "/min";
            java.rmi.Naming.rebind(name, farmer );
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
