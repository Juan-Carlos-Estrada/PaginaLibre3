USE libreriadb_in4cm;

-- crud: usuarios
DELIMITER //
DROP PROCEDURE IF EXISTS sp_listar_usuarios //
CREATE PROCEDURE sp_listar_usuarios()
BEGIN
    SELECT id, username, rol, activo, fecha_creacion FROM usuarios;
END //
DELIMITER ;

DELIMITER //
DROP PROCEDURE IF EXISTS sp_buscar_usuario //
CREATE PROCEDURE sp_buscar_usuario(
    IN _id INT
)
BEGIN
    SELECT id, username, rol, activo, fecha_creacion
    FROM usuarios
    WHERE id = _id;
END //
DELIMITER ;

DELIMITER //
DROP PROCEDURE IF EXISTS sp_actualizar_usuario //
CREATE PROCEDURE sp_actualizar_usuario(
    IN _id INT,
    IN _rol VARCHAR(20),
    IN _activo BOOLEAN
)
BEGIN
    UPDATE usuarios
    SET rol = _rol,
        activo = _activo
    WHERE id = _id;
END //
DELIMITER ;

DELIMITER //
DROP PROCEDURE IF EXISTS sp_desactivar_usuario //
CREATE PROCEDURE sp_desactivar_usuario(
    IN _id INT
)
BEGIN
    UPDATE usuarios SET activo = FALSE WHERE id = _id;
END //
DELIMITER ;

-- cambio de contraseña
DELIMITER //
DROP PROCEDURE IF EXISTS sp_cambiar_password //
CREATE PROCEDURE sp_cambiar_password(
    IN _id INT,
    IN _password_hash_actual VARCHAR(255),
    IN _password_hash_nuevo VARCHAR(255)
)
BEGIN
    UPDATE usuarios
    SET password_hash = _password_hash_nuevo
    WHERE id = _id
      AND password_hash = _password_hash_actual;
END //
DELIMITER ;
