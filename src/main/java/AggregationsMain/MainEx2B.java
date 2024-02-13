package AggregationsMain;

import jakarta.enterprise.inject.se.SeContainer;
import jakarta.enterprise.inject.se.SeContainerInitializer;
import services.impl.AggregationsServices;

public class MainEx2B {
    public static void main(String[] args) {
        SeContainerInitializer initializer=SeContainerInitializer.newInstance();
        final SeContainer container = initializer.initialize();
        AggregationsServices aggregationsServices = container.select(AggregationsServices.class).get();
        System.out.println(aggregationsServices.ex2b().get());
    }
}
