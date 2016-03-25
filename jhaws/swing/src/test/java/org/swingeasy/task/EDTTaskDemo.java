package org.swingeasy.task;

import javax.swing.JFrame;
import javax.swing.SwingWorker;

import org.swingeasy.task.EventThreadTask;

/**
 * @author Jurgen
 */
public class EDTTaskDemo {
    protected static EventThreadTask<String> createEDTT(final String key) {
        return new EventThreadTask<String>() {
            @Override
            protected String doInBackground() throws Exception {
                System.out.println("busy_" + key + " " + Thread.currentThread());
                Thread.sleep(3000);
                return "done";
            }

            @Override
            protected void doOnEventThread(String returnValue) throws Exception {
                System.out.println(key + " " + returnValue + " " + Thread.currentThread());
            }

            @Override
            protected void handleException(Throwable cause) {
                cause.printStackTrace();
            }
        };
    }

    public static void main(String[] args) {
        try {
            EDTTaskDemo.test2();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected static void test1() throws Exception {
        JFrame f = new JFrame("close to exit");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);

        SwingWorker<String, Void> dw = new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() throws Exception {
                System.out.println("busy1 " + Thread.currentThread());
                Thread.sleep(3000);

                SwingWorker<String, Void> dw_inner = new SwingWorker<String, Void>() {
                    @Override
                    protected String doInBackground() throws Exception {
                        System.out.println("busy2 " + Thread.currentThread());
                        Thread.sleep(3000);
                        return "done2 " + Thread.currentThread();
                    }
                };
                dw_inner.execute();
                System.out.println(dw_inner.get());

                return "done1 " + Thread.currentThread();
            }
        };
        dw.execute();
        System.out.println(dw.get());
    }

    protected static void test2() throws Exception {
        JFrame f = new JFrame("close to exit");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);

        EventThreadTask<String> outer_task = new EventThreadTask<String>() {
            @Override
            protected String doInBackground() throws Exception {
                System.out.println("busy_" + "outer" + " " + Thread.currentThread());
                Thread.sleep(3000);
                EDTTaskDemo.createEDTT("inner 1").execute();
                return "done";
            }

            @Override
            protected void doOnEventThread(String returnValue) throws Exception {
                System.out.println("outer" + " " + returnValue + " " + Thread.currentThread());
                EDTTaskDemo.createEDTT("inner 2").execute();
            }

            @Override
            protected void handleException(Throwable cause) {
                cause.printStackTrace();
            }
        };
        outer_task.execute();
    }
}
