package com.btuso.testament.mediator;

public interface DataMediator {

    void registerListener(DataListener listener);

    void broadcast(Data data);

}
