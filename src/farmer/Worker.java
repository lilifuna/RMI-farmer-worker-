package farmer;

import java.util.Arrays;

public class Worker implements IWorker{
    @Override
    public Integer getMin(Integer[] arr) {
        return Arrays.stream(arr)
                .min(Integer::compare)
                .get();
    }
}
