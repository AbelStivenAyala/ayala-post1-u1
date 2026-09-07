# Evidencia — Patrón Proxy

**Clase:** `org.springframework.aop.framework.JdkDynamicAopProxy`
**Módulo:** `spring-aop`
**Repositorio:** https://github.com/spring-projects/spring-framework

## Descripción de la clase (paráfrasis de la documentación oficial)

`JdkDynamicAopProxy` es la implementación de `InvocationHandler` que usa el
framework de AOP de Spring para construir proxies dinámicos basados en las
interfaces del objeto de destino. Los proxies que produce se obtienen
siempre a través de una fábrica de proxies configurada con un
`AdvisedSupport`.

## Fragmento de código representativo (extracto corto, con fines académicos)

```java
final class JdkDynamicAopProxy implements AopProxy, InvocationHandler, Serializable {

    private final AdvisedSupport advised;

    @Override
    public Object getProxy() {
        return Proxy.newProxyInstance(
            classLoader, this.advised.getProxiedInterfaces(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object target = this.advised.getTargetSource().getTarget();
        // ... aplica la cadena de "advices" (interceptores) ...
        Object retVal = method.invoke(target, args);
        return retVal;
    }
}
```

## Referencia
Spring Framework. (s.f.-b). *JdkDynamicAopProxy (Spring Framework API)*.
https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/aop/framework/JdkDynamicAopProxy.html