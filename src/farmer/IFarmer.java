package farmer;

import java.io.Serializable;
import java.rmi.Remote;

public interface IFarmer extends Remote, Serializable {

    int calculate(Integer[] arr);
}
