# Evidencia — Patrón Singleton

**Clase:** `org.springframework.beans.factory.support.DefaultSingletonBeanRegistry`
**Módulo:** `spring-beans`
**Repositorio:** https://github.com/spring-projects/spring-framework

## Descripción de la clase (paráfrasis de la documentación oficial)

`DefaultSingletonBeanRegistry` es un registro genérico de instancias de bean
compartidas. Implementa la interfaz `SingletonBeanRegistry` y permite
registrar instancias singleton que deben compartirse entre todos los
solicitantes del contenedor, obtenidas a partir del nombre del bean.

## Fragmento de código representativo (extracto corto, con fines académicos)

```java
public class DefaultSingletonBeanRegistry extends SimpleAliasRegistry
        implements SingletonBeanRegistry {

    private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>(256);

    @Override
    public void registerSingleton(String beanName, Object singletonObject) {
        synchronized (this.singletonObjects) {
            Object oldObject = this.singletonObjects.get(beanName);
            if (oldObject != null) {
                throw new IllegalStateException(
                    "Could not register object under bean name '" + beanName +
                    "': there is already an object bound");
            }
            addSingleton(beanName, singletonObject);
        }
    }
}
```

## Referencia
Spring Framework. (s.f.-a). *DefaultSingletonBeanRegistry (Spring Framework API)*.
https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/beans/factory/support/DefaultSingletonBeanRegistry.html