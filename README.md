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