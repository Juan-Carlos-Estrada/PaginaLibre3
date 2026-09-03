CREATE DATABASE IF NOT EXISTS libreriadb_in4cm;
USE libreriadb_in4cm;

CREATE TABLE usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    rol ENUM('admin', 'bodega', 'cajero') NOT NULL,
    activo BOOLEAN DEFAULT TRUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

DELIMITER //
DROP PROCEDURE IF EXISTS sp_registrar_usuario //
CREATE PROCEDURE sp_registrar_usuario(
    IN _username VARCHAR(50),
    IN _password_hash VARCHAR(255),
    IN _rol VARCHAR(20)
)
BEGIN
    INSERT INTO usuarios (username, password_hash, rol)
    VALUES (_username, _password_hash, _rol);
END //
DELIMITER ;

DELIMITER //
DROP PROCEDURE IF EXISTS sp_iniciar_sesion //
CREATE PROCEDURE sp_iniciar_sesion(
    IN _username VARCHAR(50),
    IN _password_hash VARCHAR(255)
)
BEGIN
    SELECT id, username, rol
    FROM usuarios
    WHERE username = _username
      AND password_hash = _password_hash
      AND activo = TRUE
    LIMIT 1;
END //
DELIMITER ;

-- poblado
CALL sp_registrar_usuario('admin1', SHA2('admin123', 256), 'admin');
CALL sp_registrar_usuario('bodega1', SHA2('bodega123', 256), 'bodega');
CALL sp_registrar_usuario('cajero1', SHA2('cajero123', 256), 'cajero');

-- verificacion
SELECT * FROM usuarios;
