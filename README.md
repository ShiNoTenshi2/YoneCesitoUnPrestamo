Sistema de Préstamo de Salas y Audiovisuales
Descripción
Este proyecto es un sistema de gestión de préstamos de salas y audiovisuales desarrollado como parte del curso Ingeniería del Software I (2025-I). Permite a usuarios (estudiantes, profesores y coordinadores) registrarse, autenticarse, solicitar préstamos de salas y audiovisuales, devolverlos, y gestionar sanciones o mantenimientos. El sistema sigue una arquitectura MVC (Modelo-Vista-Controlador), utiliza patrones de diseño como Factory Method y Singleton, y está documentado con diagramas UML (clases y secuencias).
Lenguaje de Desarrollo

Java

Interfaz Gráfica (GUI)

JavaFX
Scene Builder (para diseño de interfaces FXML)

Entorno de Desarrollo Integrado (IDE)

Eclipse Portable

Motor de Base de Datos

Oracle Database 10g

Drivers y Dependencias

JDBC (para conexión con Oracle Database)
JavaFX SDK (para la interfaz gráfica)

Estructura del Proyecto

model: Contiene las clases del dominio (Usuario, Prestamo, Sala, Audiovisual, Devolucion, Sancion, Mantenimiento).
data: Capa de acceso a datos con DAOs (UsuarioDAO, PrestamoDAO, DevolucionDAO, etc.) y clases de conexión (DBConnectionFactory, CoordinadorConnection).
controller: Controladores JavaFX para la lógica de la interfaz (MenuRegistroController, PrestamoController, DevolucionController, etc.).
view: Archivos FXML diseñados con Scene Builder para la interfaz gráfica.

Requisitos

Java 8 o superior
Oracle Database 10g instalado y configurado
JavaFX SDK
Eclipse IDE (o cualquier IDE compatible con JavaFX)
Driver JDBC para Oracle (ojdbc14.jar u ojdbc6.jar, según tu versión de Java)

Instalación

Clona este repositorio:git clone https://github.com/ShiNoTenshi2/sistema-prestamo-salas-audiovisuales.git


Configura Oracle Database 10g:
Crea las tablas necesarias según el modelo relacional proporcionado (ver documentación).
Asegúrate de que el usuario coordinadorYNC con contraseña yonecesito esté configurado (o ajusta las credenciales en CoordinadorConnection.java).


Importa el proyecto en Eclipse:
Abre Eclipse y selecciona File > Import > Existing Projects into Workspace.
Selecciona la carpeta del repositorio clonado.


Configura las dependencias:
Añade el JavaFX SDK al proyecto (Properties > Java Build Path > Libraries > Add External JARs).
Añade el driver JDBC de Oracle al proyecto.


Ejecuta la aplicación desde Main.java (o el archivo principal de tu proyecto).

Uso

Registro de usuario:
Ingresa tus datos (cédula, nombre, contraseña, rol, correo) en la pantalla de registro.
Los coordinadores deben aprobar las solicitudes de nuevos usuarios.


Autenticación:
Inicia sesión con tu cédula, contraseña y rol.


Préstamo de sala o audiovisual:
Selecciona una sala o audiovisual disponible, ingresa las fechas de inicio y fin, y solicita el préstamo.


Devolución:
En la pantalla de devolución, selecciona el préstamo y registra la devolución con la fecha correspondiente.


Gestión administrativa (coordinadores):
Aprueba o deniega solicitudes de usuarios.
Gestiona sanciones y mantenimientos.



Documentación

Diagrama de clases UML: Representa la arquitectura del sistema (modelo, DAOs, controladores).
Diagramas de secuencia:
Pedir un préstamo de sala.
Devolución de un préstamo.


Los diagramas están disponibles en la carpeta docs o en el informe del proyecto.

Contribuidores

David Trujillo - @ShiNoTenshi2
Sebastian Nieto - 
