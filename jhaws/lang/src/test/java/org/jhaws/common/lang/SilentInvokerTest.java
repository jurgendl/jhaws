package org.jhaws.common.lang;

import static org.jhaws.common.lang.CollectionUtils8.enhanceFunction;
import static org.jhaws.common.lang.functions.SilentInvokerFactory.call;

import java.net.URL;
import java.util.Arrays;
import java.util.List;

import org.jhaws.common.lang.functions.EFunction;
import org.junit.Test;

public class SilentInvokerTest {
    @Test
    public void test() {
        List<String> arr = Arrays.asList("http://localhost/", "https://google.com");
        // unhandled checked exception in: arr.stream().map(URL::new).forEach(System.out::println);
        // unhandled checked exception in: arr.stream().map(url -> new URL(url)).forEach(System.out::println);
        arr.stream().map(url -> call(() -> new URL(url))).forEach(System.out::println);
        arr.stream().map((EFunction<String, URL>) URL::new).forEach(System.out::println);
        arr.stream().map(enhanceFunction(URL::new)).forEach(System.out::println);
    }
}
