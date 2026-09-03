use libreriadb_in4cm;

-- libros
alter table libros
add column stock_actual int not null default 0,
add column stock_minimo int not null default 0,
add column activo boolean not null default true;

-- proveedores
create table proveedores(
	id_proveedor int primary key auto_increment,
    nombre_proveedor varchar(100) not null,
    telefono_proveedor varchar(15),
    direccion_proveedor varchar(100)
);

-- movimientos_inventario
create table movimientos_inventario(
	id_movimiento int primary key auto_increment,
    isbn varchar(20),
    tipo_movimiento enum('INGRESO','VENTA','MERMA','TRASLADO','DEVOLUCION','AJUSTE') not null,
    cantidad int not null,
    fecha_movimiento timestamp default current_timestamp,
    id_usuario int,
    id_proveedor int,
    observacion varchar(255)
);

-- llaves foraneas
alter table movimientos_inventario
add constraint fk_mov_libro foreign key (isbn) references libros(isbn) on delete cascade,
add constraint fk_mov_usuario foreign key (id_usuario) references usuarios(id) on delete cascade,
add constraint fk_mov_proveedor foreign key (id_proveedor) references proveedores(id_proveedor) on delete set null;

-- crud: proveedores
delimiter $$

create procedure sp_insertarproveedor(
    in _nombre_proveedor varchar(100),
    in _telefono_proveedor varchar(15),
    in _direccion_proveedor varchar(100)
)
begin
    insert into proveedores(nombre_proveedor, telefono_proveedor, direccion_proveedor)
    values (_nombre_proveedor, _telefono_proveedor, _direccion_proveedor);
end $$

create procedure sp_listarproveedores()
begin
    select id_proveedor, nombre_proveedor, telefono_proveedor, direccion_proveedor from proveedores;
end $$

create procedure sp_buscarproveedor(
    in _id_proveedor int
)
begin
    select id_proveedor, nombre_proveedor, telefono_proveedor, direccion_proveedor
    from proveedores
    where id_proveedor = _id_proveedor;
end $$

create procedure sp_actualizarproveedor(
    in _id_proveedor int,
    in _nombre_proveedor varchar(100),
    in _telefono_proveedor varchar(15),
    in _direccion_proveedor varchar(100)
)
begin
    update proveedores
    set nombre_proveedor = _nombre_proveedor,
        telefono_proveedor = _telefono_proveedor,
        direccion_proveedor = _direccion_proveedor
    where id_proveedor = _id_proveedor;
end $$

create procedure sp_eliminarproveedor(
    in _id_proveedor int
)
begin
    delete from proveedores where id_proveedor = _id_proveedor;
end $$

delimiter ;

-- crud: movimientos_inventario
delimiter $$

create procedure sp_listarmovimientosinventario()
begin
    select id_movimiento, isbn, tipo_movimiento, cantidad, fecha_movimiento, id_usuario, id_proveedor, observacion
    from movimientos_inventario;
end $$

create procedure sp_buscarmovimientoinventario(
    in _id_movimiento int
)
begin
    select id_movimiento, isbn, tipo_movimiento, cantidad, fecha_movimiento, id_usuario, id_proveedor, observacion
    from movimientos_inventario
    where id_movimiento = _id_movimiento;
end $$

delimiter ;

-- control de stock
delimiter $$

create procedure sp_listarlibroscritico()
begin
    select isbn, titulo, stock_actual, stock_minimo
    from libros
    where stock_actual <= stock_minimo and activo = true;
end $$

create procedure sp_registraringreso(
    in _isbn varchar(20),
    in _cantidad int,
    in _id_usuario int,
    in _id_proveedor int,
    in _observacion varchar(255)
)
begin
    update libros set stock_actual = stock_actual + _cantidad where isbn = _isbn;
    insert into movimientos_inventario(isbn, tipo_movimiento, cantidad, id_usuario, id_proveedor, observacion)
    values (_isbn, 'INGRESO', _cantidad, _id_usuario, _id_proveedor, _observacion);
end $$

create procedure sp_registrarsalida(
    in _isbn varchar(20),
    in _tipo_movimiento enum('INGRESO','VENTA','MERMA','TRASLADO','DEVOLUCION','AJUSTE'),
    in _cantidad int,
    in _id_usuario int,
    in _observacion varchar(255)
)
begin
    update libros set stock_actual = stock_actual - _cantidad where isbn = _isbn;
    insert into movimientos_inventario(isbn, tipo_movimiento, cantidad, id_usuario, observacion)
    values (_isbn, _tipo_movimiento, _cantidad, _id_usuario, _observacion);
end $$

delimiter ;

-- correccion: editoriales
alter table editoriales change column direccion_editoria direccion_editorial varchar(100);

drop procedure if exists sp_insertareditorial;
delimiter $$
create procedure sp_insertareditorial(
    in _nit varchar(20),
    in _nombre_editorial varchar(100),
    in _telefono_editorial varchar(15),
    in _direccion_editorial varchar(100)
)
begin
    insert into editoriales(nit, nombre_editorial, telefono_editorial, direccion_editorial)
    values (_nit, _nombre_editorial, _telefono_editorial, _direccion_editorial);
end $$
delimiter ;

drop procedure if exists sp_listareditoriales;
delimiter $$
create procedure sp_listareditoriales()
begin
    select nit, nombre_editorial, telefono_editorial, direccion_editorial from editoriales;
end $$
delimiter ;

drop procedure if exists sp_buscareditorial;
delimiter $$
create procedure sp_buscareditorial(
    in _nit varchar(20)
)
begin
    select nit, nombre_editorial, telefono_editorial, direccion_editorial
    from editoriales
    where nit = _nit;
end $$
delimiter ;

drop procedure if exists sp_actualizareditorial;
delimiter $$
create procedure sp_actualizareditorial(
    in _nit varchar(20),
    in _nombre_editorial varchar(100),
    in _telefono_editorial varchar(15),
    in _direccion_editorial varchar(100)
)
begin
    update editoriales
    set nombre_editorial = _nombre_editorial,
        telefono_editorial = _telefono_editorial,
        direccion_editorial = _direccion_editorial
    where nit = _nit;
end $$
delimiter ;

create or replace view vw_lista_editoriales as
select
    nit as 'nit editorial',
    nombre_editorial as 'editorial',
    telefono_editorial as 'teléfono',
    direccion_editorial as 'dirección'
from editoriales;

-- ventas
create table ventas(
	id_venta int primary key auto_increment,
    fecha_venta timestamp default current_timestamp,
    subtotal decimal(10,2) not null default 0,
    descuento decimal(10,2) not null default 0,
    total decimal(10,2) not null default 0,
    estado enum('COMPLETADA','ANULADA','DEVUELTA') not null default 'COMPLETADA',
    cui_cliente bigint,
    id_usuario int,
    usuario_autoriza_descuento int,
    fecha_anulacion timestamp null,
    usuario_anulacion int,
    motivo_anulacion varchar(255)
);

create table detalle_venta(
	id_detalle int primary key auto_increment,
    id_venta int,
    isbn varchar(20),
    cantidad int not null,
    precio_unitario decimal(10,2) not null,
    subtotal decimal(10,2) not null
);

alter table ventas
add constraint fk_venta_cliente foreign key (cui_cliente) references clientes(cui) on delete set null,
add constraint fk_venta_usuario foreign key (id_usuario) references usuarios(id) on delete set null,
add constraint fk_venta_autoriza foreign key (usuario_autoriza_descuento) references usuarios(id) on delete set null,
add constraint fk_venta_anulacion foreign key (usuario_anulacion) references usuarios(id) on delete set null;

alter table detalle_venta
add constraint fk_detalle_venta foreign key (id_venta) references ventas(id_venta) on delete cascade,
add constraint fk_detalle_libro foreign key (isbn) references libros(isbn) on delete cascade;

-- crud: ventas
delimiter $$

create procedure sp_insertarventa(
    in _cui_cliente bigint,
    in _id_usuario int
)
begin
    insert into ventas(cui_cliente, id_usuario)
    values (_cui_cliente, _id_usuario);
    select last_insert_id() as id_venta;
end $$

create procedure sp_listarventas()
begin
    select id_venta, fecha_venta, subtotal, descuento, total, estado, cui_cliente, id_usuario
    from ventas;
end $$

create procedure sp_buscarventa(
    in _id_venta int
)
begin
    select id_venta, fecha_venta, subtotal, descuento, total, estado, cui_cliente, id_usuario
    from ventas
    where id_venta = _id_venta;
end $$

create procedure sp_listarventasdeusuario(
    in _id_usuario int
)
begin
    select id_venta, fecha_venta, subtotal, descuento, total, estado
    from ventas
    where id_usuario = _id_usuario;
end $$

create procedure sp_anularventa(
    in _id_venta int,
    in _id_usuario_anulacion int,
    in _motivo varchar(255)
)
begin
    update ventas
    set estado = 'ANULADA',
        fecha_anulacion = current_timestamp,
        usuario_anulacion = _id_usuario_anulacion,
        motivo_anulacion = _motivo
    where id_venta = _id_venta;
end $$

delimiter ;

-- crud: detalle_venta
delimiter $$

create procedure sp_insertardetalleventa(
    in _id_venta int,
    in _isbn varchar(20),
    in _cantidad int,
    in _precio_unitario decimal(10,2)
)
begin
    insert into detalle_venta(id_venta, isbn, cantidad, precio_unitario, subtotal)
    values (_id_venta, _isbn, _cantidad, _precio_unitario, _cantidad * _precio_unitario);

    update ventas
    set subtotal = (select sum(subtotal) from detalle_venta where id_venta = _id_venta),
        total = (select sum(subtotal) from detalle_venta where id_venta = _id_venta) - descuento
    where id_venta = _id_venta;
end $$

create procedure sp_listardetalleventa(
    in _id_venta int
)
begin
    select dv.id_detalle, dv.id_venta, dv.isbn, l.titulo, dv.cantidad, dv.precio_unitario, dv.subtotal
    from detalle_venta dv
    inner join libros l on dv.isbn = l.isbn
    where dv.id_venta = _id_venta;
end $$

delimiter ;

create or replace view vw_lista_ventas as
select
    v.id_venta as 'no. venta',
    v.fecha_venta as 'fecha',
    v.total as 'total',
    v.estado as 'estado',
    concat(cl.nombre_cliente, ' ', cl.apellido_cliente) as 'cliente',
    u.username as 'cajero'
from ventas v
left join clientes cl on v.cui_cliente = cl.cui
left join usuarios u on v.id_usuario = u.id;

-- vistas
create or replace view vw_libros_stock_critico as
select
    isbn as 'isbn',
    titulo as 'título',
    stock_actual as 'stock actual',
    stock_minimo as 'stock mínimo'
from libros
where stock_actual <= stock_minimo and activo = true;

create or replace view vw_lista_proveedores as
select
    id_proveedor as 'id proveedor',
    nombre_proveedor as 'proveedor',
    telefono_proveedor as 'teléfono',
    direccion_proveedor as 'dirección'
from proveedores;

create or replace view vw_lista_movimientos_inventario as
select
    m.id_movimiento as 'id movimiento',
    l.titulo as 'libro',
    m.tipo_movimiento as 'tipo',
    m.cantidad as 'cantidad',
    m.fecha_movimiento as 'fecha',
    p.nombre_proveedor as 'proveedor'
from movimientos_inventario m
inner join libros l on m.isbn = l.isbn
left join proveedores p on m.id_proveedor = p.id_proveedor;

-- poblado
call sp_insertarproveedor('Distribuidora Maya S.A.', '22110011', 'Zona 4, Ciudad');
call sp_insertarproveedor('Libros y Más Guatemala', '22110022', 'Zona 9, Ciudad');
call sp_insertarproveedor('Importadora Editorial Centroamérica', '22110033', 'Zona 12, Ciudad');

call sp_registraringreso('978-0-123', 20, 2, 1, 'stock inicial');
call sp_registraringreso('978-0-124', 15, 2, 2, 'stock inicial');
call sp_registraringreso('978-0-126', 25, 2, 1, 'stock inicial');
call sp_registraringreso('978-0-129', 3, 2, 2, 'stock inicial');
call sp_registraringreso('978-0-130', 10, 2, 3, 'stock inicial');
call sp_registraringreso('978-0-134', 6, 2, 3, 'stock inicial');

update libros set stock_minimo = 5 where isbn = '978-0-129';

call sp_insertarventa(2000100010101, 3);
call sp_insertardetalleventa(1, '978-0-123', 2, 150.00);
call sp_insertardetalleventa(1, '978-0-126', 1, 180.00);

-- verificacion
select * from vw_libros_stock_critico;
select * from vw_lista_movimientos_inventario;
select * from vw_lista_ventas;
select * from vw_lista_editoriales;
