# Evidencia — Patrón Observer

**Interfaces:** `org.springframework.context.ApplicationListener`,
`org.springframework.context.ApplicationEvent`,
`org.springframework.context.event.ApplicationEventMulticaster`
**Módulo:** `spring-context`
**Repositorio:** https://github.com/spring-projects/spring-framework

## Descripción (paráfrasis de la documentación oficial)

La propia documentación de Spring reconoce el origen del patrón:
`ApplicationListener` está basado en la interfaz estándar
`java.util.EventListener` e implementa el patrón de diseño Observer. Un
listener declara el tipo de evento que le interesa; al registrarse en un
`ApplicationContext`, solo será invocado cuando ocurra un evento que
coincida con ese tipo.

## Fragmento de código representativo (extracto corto, con fines académicos)

```java
@FunctionalInterface
public interface ApplicationListener<E extends ApplicationEvent> extends EventListener {
    void onApplicationEvent(E event);
}

@Component
public class OrderCreatedListener implements ApplicationListener<OrderCreatedEvent> {
    @Override
    public void onApplicationEvent(OrderCreatedEvent event) {
        System.out.println("Notificando creación de orden: " + event.getOrderId());
    }
}

@Service
public class OrderService {
    private final ApplicationEventPublisher publisher;

    public OrderService(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    public void createOrder(String orderId) {
        publisher.publishEvent(new OrderCreatedEvent(this, orderId));
    }
}
```

## Referencia
Spring Framework. (s.f.-c). *ApplicationListener (Spring Framework API)*.
https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/context/ApplicationListener.html