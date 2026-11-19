# Sistema de Gestión Golden Center Gym

Evidencia GA7-220501096-AA2-EV01 - Codificación de módulos del software.

## Módulos desarrollados
- Gestión de Clientes: registrar, consultar, actualizar y desactivar clientes.
- Gestión de Membresías: registrar, consultar, actualizar y desactivar membresías asociadas a un cliente existente.

## Estructura del proyecto
- src/config: conexión a MySQL con JDBC (ConexionBD).
- src/model: clases Cliente y Membresia.
- src/dao: clases ClienteDao y MembresiaDao (operaciones CRUD).
- src/App.java: menú de consola.
- lib: conector MySQL Connector/J 9.7.0.
- database: script SQL de la base de datos.

## Requisitos
- JDK 21
- MySQL 8.0

## Cómo ejecutarlo (PowerShell, desde la carpeta del proyecto)
1. Ejecutar el script database/golden_center_gym.sql en MySQL Workbench.
2. Definir la contraseña de MySQL (usuario root, puerto 3306):
   $env:DB_PASSWORD='su_contraseña'
3. Compilar:
   javac -encoding UTF-8 -d out -cp "lib/*" -sourcepath src src/App.java
4. Para ver bien las tildes:
   [Console]::OutputEncoding = [System.Text.Encoding]::UTF8
   [Console]::InputEncoding = [System.Text.Encoding]::UTF8
5. Ejecutar:
   java "-Dfile.encoding=UTF-8" -cp "out;lib/*" App