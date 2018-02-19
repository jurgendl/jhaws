package org.jhaws.common.batch;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RunnableList implements Runnable {
    private List<Runnable> runnables = new ArrayList<>();

    @Override
    public void run() {
        runnables.forEach(Runnable::run);
    }

    public void add(Runnable runnable) {
        runnables.add(runnable);
    }

    @Override
    public String toString() {
        return "RunnableList[\n\t" + runnables.stream().map(Object::toString).collect(Collectors.joining("\n\t")) + "\n]";
    }
}
