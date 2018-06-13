package farmer;

import java.io.Serializable;
import java.rmi.Remote;

public interface IWorker extends Remote, Serializable {

    Integer getMin(Integer[] arr);
}
