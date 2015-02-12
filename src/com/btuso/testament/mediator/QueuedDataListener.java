package com.btuso.testament.mediator;

import java.util.ArrayList;
import java.util.List;

import com.btuso.testament.Logger;

public class QueuedDataListener implements DataListener {

    List<Data> queue = new ArrayList<Data>();
    private boolean dirty = false;

    @Override
    public void onDataReceived(Data data) {
        if (dirty) {
            // TODO think about how to deal with the queue not being modified in
            // the update, since no warning would show up. Maybe a default limit
            Logger.log(this.getClass().getSimpleName() + ": Queue was has not been cleared since last operation.");
        }
        queue.add(data);
    }

    public Data head() {
        dirty = true;
        return queue.remove(0);
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public boolean contains(Data data) {
        dirty = true;
        for (Data queuedData : queue) {
            if (queuedData.equals(data)) {
                return true;
            }
        }
        return false;
    }

    public void clearQueue() {
        queue.clear();
        dirty = false;
    }
}
