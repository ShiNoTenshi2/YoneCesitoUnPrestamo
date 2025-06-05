# Sistema de Préstamo de Salas y Audiovisuales
# (YONEcesito un préstamo)
## Descripción
- Este proyecto es un sistema de gestión de préstamos de salas y audiovisuales. Permite a usuarios (estudiantes, profesores y coordinadores) registrarse, autenticarse, solicitar préstamos de salas y audiovisuales, devolverlos, y gestionar sanciones o mantenimientos. Utiliza una arquitectura MVC, patrones de diseño como Factory Method y Singleton, y está documentado con diagramas UML (clases, secuencias, entidad-relacion, etc).

## Lenguaje de Desarrollo
- Java

## GUI: Graphical User Interface
- JavaFX
- Scene Builder (para diseño de interfaces FXML)

## Entorno de Desarrollo Integrado
- Eclipse Portable

## Motor de Base de Datos
- Oracle Database 10g

## Drivers
- JDBC (para conexión con Oracle Database)
- JavaFX SDK (para la interfaz gráfica)

## Estructura del Proyecto
- model: Clases del dominio (Usuario, Prestamo, Sala, Audiovisual, Devolucion, Sancion, Mantenimiento).
- data: Capa de acceso a datos con DAOs (UsuarioDAO, PrestamoDAO, DevolucionDAO, etc.) y conexiones (DBConnectionFactory, DBConnection, CoordinadorConnection, etc).
- controller: Controladores JavaFX (MenuRegistroController, PrestamoController, DevolucionController, etc).
- view: Archivos FXML diseñados con Scene Builder.

## Requisitos
- Java 8 o superior
- Oracle Database 10g 
- JavaFX SDK
- Eclipse IDE 
- Driver JDBC para Oracle 

## Instalación

1) Clona el repositorio: https://github.com/ShiNoTenshi2/YoneCesitoUnPrestamo.git
2) Configura Oracle Database 10g
3) Crea las tablas según el modelo relacional.
4) Importa en Eclipse:
File > Import > Existing Projects into Workspace y selecciona la carpeta clonada.
Añade dependencias:
-JavaFX SDK en Properties > Java Build Path > Libraries > Add External JARs.
-Driver JDBC de Oracle.
5) Ejecuta desde Main.java (o el archivo principal).

## Contribuidores
- David Trujillo - @ShiNoTenshi2 (https://github.com/ShiNoTenshi2).
- Sebastian Nieto - @qawer1 (https://github.com/qawer1).
