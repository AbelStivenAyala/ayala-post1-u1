# Análisis de Patrones de Diseño GoF en Spring Framework

**Nombre:** Abel Stiven Ayala Llanes
**Código:** [1152354]
**Curso:** Patrones de Diseño de Software — Sexto Semestre
**Unidad:** 1 — Fundamentos de Patrones de Diseño y Buenas Prácticas
**Fecha:** [7/9/2026]

---

## 1. Introducción

Los patrones de diseño del catálogo de la Banda de los Cuatro (GoF) no son
solo ejercicios académicos: son soluciones recurrentes que aparecen en los
frameworks que se usan a diario en la industria. Spring Framework, uno de
los frameworks más adoptados para el desarrollo de aplicaciones empresariales
en Java, es un caso de estudio particularmente rico porque su arquitectura
completa —el contenedor de inversión de control, el módulo de programación
orientada a aspectos (AOP) y el sistema de eventos de aplicación— está
construida a partir de patrones GoF combinados de forma deliberada. Este
documento tiene como objetivo identificar tres de esos patrones, provenientes
de las tres categorías del catálogo GoF (Creacional, Estructural y de
Comportamiento), analizando en cada caso la clase o interfaz concreta donde
se implementan, el problema real que resuelven dentro de Spring Boot, y el
principio SOLID que refuerzan.

## 2. Análisis de Patrón 1 — Singleton (Categoría Creacional)

### ¿Cuál es el patrón y a qué categoría pertenece?

Singleton es un patrón creacional cuyo propósito es garantizar que una clase
tenga una única instancia accesible desde un punto de acceso global,
controlando así la creación del objeto en lugar de dejarla en manos del
código cliente.

### ¿Dónde aparece en Spring Framework?

Aparece en la clase `org.springframework.beans.factory.support.DefaultSingletonBeanRegistry`,
ubicada en el módulo `spring-beans`, que permite registrar instancias
singleton compartidas entre todos los solicitantes del contenedor
(Spring Framework, s.f.-a).

### ¿Qué problema resuelve en este contexto?

En una aplicación empresarial, componentes como un `DataSource`, un
`ObjectMapper` o un servicio de negocio sin estado no deberían crearse una y
otra vez cada vez que se necesitan: hacerlo desperdiciaría memoria y tiempo
de inicialización, y además complicaría compartir estado de configuración
entre las distintas partes de la aplicación. Spring resuelve esto con el
*scope* `singleton`, el predeterminado para cualquier bean: el contenedor
crea la instancia una sola vez por contenedor (`ApplicationContext`) y la
reutiliza para todas las inyecciones posteriores. La particularidad frente
al Singleton clásico del GoF —que usa un campo estático y un constructor
privado— es que aquí el "único punto de acceso global" no es la propia
clase, sino el contenedor: esto permite que convivan varios `ApplicationContext`
en la misma JVM, cada uno con su propia instancia "singleton" del mismo bean.

### ¿Qué evidencia de código lo confirma?

El registro interno de `DefaultSingletonBeanRegistry` mantiene un mapa de
nombre de bean a instancia ya creada, y su método `registerSingleton`
lanza una excepción si se intenta registrar dos veces un bean con el mismo
nombre, reforzando la garantía de unicidad (ver
`evidencia/01-singleton-DefaultSingletonBeanRegistry.md`).

### ¿Qué principio SOLID refuerza?

Refuerza principalmente el **Principio de Responsabilidad Única (SRP)**:
al centralizar la creación y el ciclo de vida de los beans en el
contenedor, las clases de negocio quedan libres de gestionar su propia
instanciación. De forma secundaria, también favorece el **Principio de
Inversión de Dependencias (DIP)**, porque el resto del framework depende
de la abstracción `SingletonBeanRegistry` y no de una implementación
concreta de gestión de instancias.


---

## 3. Análisis de Patrón 2 — Proxy (Categoría Estructural)

### ¿Cuál es el patrón y a qué categoría pertenece?

Proxy es un patrón estructural que provee un objeto sustituto o
intermediario para controlar el acceso a otro objeto, permitiendo añadir
comportamiento adicional (control de acceso, registro, transacciones,
caché) sin modificar la clase original.

### ¿Dónde aparece en Spring Framework?

Aparece en la clase `org.springframework.aop.framework.JdkDynamicAopProxy`,
ubicada en el módulo `spring-aop`(Spring Framework, s.f.-b). Esta clase implementa
`java.lang.reflect.InvocationHandler` y es una de las dos estrategias que
usa Spring para crear proxies (la otra es CGLIB, para clases sin interfaz).

### ¿Qué problema resuelve en este contexto?

Funcionalidades transversales como el manejo declarativo de transacciones
(`@Transactional`), la caché (`@Cacheable`) o la seguridad a nivel de
método necesitan ejecutarse *alrededor* de un método de negocio sin que la
clase que contiene ese método tenga que escribir código repetitivo para
abrir transacciones, verificar permisos o consultar la caché en cada
método. Spring resuelve esto generando en tiempo de ejecución un proxy
dinámico que implementa las mismas interfaces que el objeto real: el bean
que el desarrollador recibe al inyectar una dependencia con `@Autowired` no
es el objeto de negocio en sí, sino este proxy, que intercepta la llamada,
ejecuta la lógica transversal correspondiente y solo entonces delega en el
objeto real.

### ¿Qué evidencia de código lo confirma?

El método `invoke` de `JdkDynamicAopProxy` es el punto donde cada llamada a
un método del bean pasa primero por el proxy antes de llegar al objeto de
destino real, permitiendo aplicar la cadena de "advices" configurada (ver
`evidencia/02-proxy-JdkDynamicAopProxy.md`).

### ¿Qué principio SOLID refuerza?

Refuerza sobre todo el **Principio de Responsabilidad Única (SRP)**: la
lógica transversal (transacciones, seguridad, caché) queda completamente
separada de la lógica de negocio del bean real. También refuerza el
**Principio de Abierto/Cerrado (OCP)**, porque es posible añadir nuevo
comportamiento transversal sin modificar ni el bean de negocio ni el
mecanismo de proxy en sí.


---

## 4. Análisis de Patrón 3 — Observer (Categoría de Comportamiento)

### ¿Cuál es el patrón y a qué categoría pertenece?

Observer es un patrón de comportamiento que define una dependencia
uno-a-muchos entre objetos, de forma que cuando un objeto (el "sujeto")
cambia de estado o produce un evento, todos sus dependientes (los
"observadores") son notificados y actualizados automáticamente, sin que el
sujeto necesite conocer los detalles de sus observadores.

### ¿Dónde aparece en Spring Framework?

Aparece en el trío formado por `org.springframework.context.ApplicationEvent`,
`org.springframework.context.ApplicationListener` y
`org.springframework.context.event.ApplicationEventMulticaster`, todas en
el módulo `spring-context`. La propia documentación oficial de Spring
menciona explícitamente que `ApplicationListener` está basado en la
interfaz estándar de Java para el patrón Observer.
La documentación oficial de Spring señala que `ApplicationListener` está
basado en la interfaz estándar de Java e implementa el patrón Observer
(Spring Framework, s.f.-c).

### ¿Qué problema resuelve en este contexto?

Cuando ocurre algo relevante durante el ciclo de vida de la aplicación
—como que el contenedor termine de inicializarse, o un evento propio del
dominio, como la creación de una orden—, distintas partes de la aplicación
pueden necesitar reaccionar: enviar un correo, invalidar una caché,
registrar una auditoría. Si el componente que genera el evento tuviera que
invocar directamente a cada uno de esos consumidores, quedaría fuertemente
acoplado a ellos. Spring resuelve esto permitiendo que cualquier bean
publique un evento a través de `ApplicationEventPublisher` sin conocer
quién lo va a consumir, mientras que los beans interesados simplemente
implementan `ApplicationListener<TipoDeEvento>` y se registran en el
contenedor.

### ¿Qué evidencia de código lo confirma?

La interfaz `ApplicationListener` define un único método,
`onApplicationEvent(E event)`, que el contenedor invoca automáticamente
sobre cada listener registrado cuyo tipo de evento coincide con el evento
publicado (ver `evidencia/03-observer-ApplicationListener.md`).

### ¿Qué principio SOLID refuerza?

Refuerza principalmente el **Principio de Abierto/Cerrado (OCP)**: se
pueden agregar nuevos listeners para reaccionar a un evento existente sin
modificar el código del componente que lo publica. También refuerza el
**Principio de Segregación de Interfaces (ISP)**, ya que `ApplicationListener`
es una interfaz mínima de un solo método, y el **Principio de Inversión de
Dependencias (DIP)**, porque tanto el publicador como los consumidores
dependen únicamente de la abstracción `ApplicationEvent`/`ApplicationListener`.

---

## 5. Conclusiones

El análisis de estos tres patrones —Singleton, Proxy y Observer— muestra
que Spring Framework no usa los patrones GoF como recetas aisladas, sino
como piezas que se combinan para sostener su arquitectura completa: el
contenedor gestiona instancias únicas por bean (Singleton), envuelve esos
beans en proxies para inyectar comportamiento transversal sin ensuciar la
lógica de negocio (Proxy), y permite que los componentes se comuniquen
mediante eventos sin conocerse entre sí (Observer). La lección más
importante para el diseño propio es que los patrones de diseño deberían
adoptarse cuando resuelven un problema real de extensibilidad o de
acoplamiento, y no como un fin en sí mismos.

---

## 6. Referencias



Spring Framework. (s.f.-a). *DefaultSingletonBeanRegistry (Spring Framework API)*.
https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/beans/factory/support/DefaultSingletonBeanRegistry.html

Spring Framework. (s.f.-b). *JdkDynamicAopProxy (Spring Framework API)*.
https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/aop/framework/JdkDynamicAopProxy.html

Spring Framework. (s.f.-c). *ApplicationListener (Spring Framework API)*.
https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/context/ApplicationListener.html


