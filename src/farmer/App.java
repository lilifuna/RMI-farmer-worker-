package farmer;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Random;

public class App {

    public static void main(String[] args){
        IFarmer  farmer = null;
        Integer result;



        Integer[] array = new Random().ints(1000, 21, 100).boxed().toArray(Integer[]::new);


        String address = "/localhost:80/min";

        // if (System.getSecurityManager() == null)
        // System.setSecurityManager(new SecurityManager());


        try {
            farmer = (IFarmer) Naming.lookup(address);
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }


        result = farmer.calculate(array);


        System.out.println("Result: " + result);


        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
