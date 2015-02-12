package com.btuso.testament.mediator;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import android.util.Log;

public class EntityDataMediator implements DataMediator {

    Set<DataListener> listeners = new HashSet<DataListener>();

    @Override
    public void registerListener(DataListener listener) {
        Log.d("Mediator", "Registering listener: " + listener.getClass().getSimpleName());
        listeners.add(listener);
    }

    @Override
    public void broadcast(Data data) {
        Log.d("Mediator", "Broadcasting: " + data);
        Iterator<DataListener> it = listeners.iterator();
        while (it.hasNext()) {
            DataListener dataListener = it.next();
            dataListener.onDataReceived(data);
        }
    }

}
