# Challenge Backend Alkemy - Java Spring Boot (API)

**Objetivo:** Desarrollar una API para explorar el mundo de Disney, la cual permitirá conocer y modificar los personajes que lo componen y entender en qué películas estos participaron. Por otro lado, deberá exponer la información para que cualquier frontend pueda consumirla.

# Tecnologías
- Java 11
- Spring Boot 2.6.2
- MySQL

## Correr el proyecto

- Modifique las credenciales de inicio de sesión de MySQL en el archivo de configuración `src/src/main/resources/application.yml`

- Ejecute el script `sql/database.sql` para crear la base de datos.
	> Nota: Si modifica el nombre de la base de datos, también deberá hacerlo en el archivo de configuración.
	
- El proyecto utiliza el servicio **SendGrid** para el envío de emails por lo que deberá proveer su `key` privada  y un `templateId` en el archivo de configuración.
	> Nota: En caso de no querer configurar SendGrid deje las variables `key` y `templateId` en blanco y el sistema funcionará con normailidad sin envío de emails.
