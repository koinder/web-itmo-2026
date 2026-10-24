package ru.itmo.wp.web.page;

import java.util.Map;


 // Интерфейс с default-методом. Его подключает предок страницы DemoBase.
@SuppressWarnings({"unused", "RedundantSuppression"})
public interface Greeter {
    default void greet(Map<String, Object> view) {
        view.put("message", "greet(view): default-метод интерфейса Greeter, который подключил предок DemoBase");
    }
}
