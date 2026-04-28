# Decisiones de Diseño - TP1 BiblioTech

Este documento resume las decisiones arquitectónicas y de diseño tomadas para el TP1.

## 1. Arquitectura en Capas
Se optó por una arquitectura de tres capas para lograr el desacoplamiento de responsabilidades:
* **Capa de Modelo (`model`):** Definición de entidades de dominio utilizando `records` (Java 25) para garantizar inmutabilidad y concisión.
* **Capa de Repositorio (`repository`):** Gestión de la persistencia en memoria mediante el uso de Genéricos (`Repository<T, ID>`), permitiendo una fácil transición a bases de datos en el futuro.
* **Capa de Servicio (`service`):** Donde reside la lógica de negocio. Actúa como mediador entre la interfaz de usuario y los datos.

## 2. Aplicación de Polimorfismo
En lugar de utilizar estructuras condicionales extensas (`if/else` o `switch`) para diferenciar los tipos de recursos, se implementó:
* **Interfaz `Recurso`:** Define el contrato común.
* **Método `mostrarInfo()`:** Cada subclase (`Ebook`, `LibroFisico`) implementa su propia lógica de visualización. Esto permite que la interfaz de usuario (CLI) sea agnóstica al tipo de recurso que está listando.

## 3. Integridad y Validación de Datos
Para asegurar que el sistema sea robusto, se implementaron validaciones en la capa de **Servicio**:
* **Unicidad de ISBN:** Antes de registrar un recurso, el `RecursoService` verifica que el identificador no exista previamente, lanzando una excepción personalizada (`BibliotecaException`) en caso de conflicto.
* **Gestión de Préstamos:** La lógica de límites de libros por socio y disponibilidad de recursos se centraliza en `PrestamoService`.

## 4. Manejo de Excepciones
Se definió una jerarquía de excepciones propias para separar errores de entrada del usuario (formatos numéricos) de errores de lógica de negocio (recurso no disponible o socio inexistente). Esto garantiza que el sistema no interrumpa su ejecución ante errores comunes.

## 5. Experiencia de Usuario (CLI)
Se diseñó una interfaz de línea de comandos basada en menús anidados con validación de entradas para asegurar una navegación fluida y prevenir cierres inesperados del programa por ingresos de datos erróneos.