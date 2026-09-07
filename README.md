# ayala-post1-u1
Post-contenido — Refactorización SOLID y análisis de patrones GoF en Spring
## Análisis de Violaciones SOLID
(cada | representa una separacion de acuerdo al encabezado de la tabla)
----------------------------------------------------------------------------------------
| Principio | Método/Sección afectada | Descripción de la violación |
----------------------------------------------------------------------------------------
| SRP | `calculateTotal` + `applyDiscount` + `saveOrder` + `sendEmail` + `printReport` | La clase concentra cinco razones de cambio distintas (cálculo fiscal, política de descuentos, persistencia, notificación y presentación) en un mismo tipo, de modo que una modificación en cualquiera de esas áreas obliga a tocar la misma clase y arriesga romper las demás responsabilidades.|
----------------------------------------------------------------------------------------
| OCP | `applyDiscount` (if/else sobre `customerType`) | Agregar un nuevo tipo de cliente (por ejemplo, "PREMIUM") exige editar el cuerpo del método existente en lugar de extender el comportamiento, violando el principio de que el código debe estar abierto a extensión pero cerrado a modificación.|
----------------------------------------------------------------------------------------
| DIP | Toda la clase (dependencias internas sin abstracciones) | `OrderProcessor` implementa directamente la persistencia (lista interna) y la notificación por correo dentro de sus propios métodos, sin depender de abstracciones inyectables; esto acopla la lógica de negocio a detalles concretos de infraestructura y dificulta sustituir o probar esos componentes de forma aislada.|


---

## Parte 2 — Análisis de Patrones GoF en Spring

| # | Patrón | Categoría | Clase en Spring |
|---|--------|-----------|------------------|
| 1 | Singleton | Creacional | `org.springframework.beans.factory.support.DefaultSingletonBeanRegistry` |
| 2 | Proxy | Estructural | `org.springframework.aop.framework.JdkDynamicAopProxy` |
| 3 | Observer | Comportamiento | `org.springframework.context.ApplicationListener` / `ApplicationEvent` |

Ver el análisis completo en [`parte-2-analisis-gof-spring/documento-analisis.md`](parte-2-analisis-gof-spring/documento-analisis.md).

## Herramientas utilizadas
- Java 17, Apache Maven, VS Code, Git, GitHub
- Código fuente de Spring Framework (investigación): https://github.com/spring-projects/spring-framework

## Conclusiones
Refactorizar OrderProcessor mostró en la práctica cómo una clase que
"funciona" puede seguir violando SOLID de forma severa, y cómo aplicar SRP,
OCP y DIP de manera incremental produce un diseño donde cada pieza es fácil
de entender, probar y extender por separado. El análisis de Spring
Framework confirmó que estos mismos principios no son un ejercicio teórico:
son la razón de ser de patrones como Singleton, Proxy y Observer dentro de
un framework usado en producción por millones de aplicaciones.