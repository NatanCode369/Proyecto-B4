drop database if exists catalogo_digital_in4am;
create database catalogo_digital_in4am;
use catalogo_digital_in4am;

create table Estudiante(
    ID_estudiante varchar(36) not null,
    nombre_estudiante varchar(50) not null,
    apellido_estudiante varchar(50) not null,
    constraint pk_estudiante primary key (ID_estudiante)
);

create table Jefe(
    ID_jefe varchar(36) not null,
    nombre_jefe varchar(50) not null,
    apellido_jefe varchar(50) not null,
    correo varchar(50) not null,
    contrasena varchar(30) not null,
    constraint pk_jefe primary key (ID_jefe)
);

create table Bibliotecario(
    ID_bibliotecario varchar(36) not null,
    nombre_bibliotecario varchar(50) not null,
    apellido_bibliotecario varchar(50) not null,
    correo varchar(50) not null,
    contrasena varchar(30) not null,
    constraint pk_bibliotecario primary key (ID_bibliotecario)
);

create table Libro(
    ISBN varchar(13) not null,
    titulo_libro varchar(100) not null,
    autor_libro varchar(100) not null,
    editorial_libro varchar(100) not null,
    año_publicacion date not null,
    stock int not null,
    constraint pk_libro primary key (ISBN)
);

create table Prestamo(
    ID_prestamo varchar(36) not null,
    ID_estudiante varchar(36) not null,
    ISBN varchar(13) not null,
    fecha_adquisicion date not null,
    fecha_vencimiento date not null,
    aprobacion_prestamo boolean not null,
    constraint pk_prestamo primary key (ID_prestamo),
    constraint fk_prestamo_estudiante foreign key (ID_estudiante) references Estudiante(ID_estudiante),
    constraint fk_prestamo_libro foreign key (ISBN) references Libro(ISBN)
);

-- -----------------------------------CRUDS Libro-------------------------------------------
 
-- CREATE
Delimiter $$
create procedure sp_create_libro(
    in ISBN_p varchar(13),
    in titulo_libro_p varchar(100),
    in autor_libro_p varchar(100),
    in editorial_libro_p varchar(100),
    in año_publicacion_p date,
    in stock_p int
)
begin
    insert into Libro(ISBN, titulo_libro, autor_libro, editorial_libro, año_publicacion, stock)
    values(ISBN_p, titulo_libro_p, autor_libro_p, editorial_libro_p, año_publicacion_p, stock_p);
end$$
Delimiter ;

-- READ
Delimiter $$
create procedure sp_read_libro()
begin
    select * from Libro;
end$$
Delimiter ;

-- UPDATE
Delimiter $$
create procedure sp_update_libro(
    in ISBN_p varchar(13),
    in titulo_libro_p varchar(100),
    in autor_libro_p varchar(100),
    in editorial_libro_p varchar(100),
    in año_publicacion_p date,
    in stock_p int
)
begin
    update Libro
    set
        titulo_libro = titulo_libro_p,
        autor_libro = autor_libro_p,
        editorial_libro = editorial_libro_p,
        año_publicacion = año_publicacion_p,
        stock = stock_p
    where ISBN = ISBN_p;
end$$
Delimiter ;

-- DELETE
Delimiter $$
create procedure sp_delete_libro(in ISBN_p varchar(13))
begin
    delete from Libro where ISBN = ISBN_p;
end$$
Delimiter ;

-- SEARCH
Delimiter $$
create procedure sp_search_libro(in ISBN_p varchar(13))
begin
    select * from Libro where ISBN = ISBN_p;
end$$
Delimiter ;

-- -----------------------------------CRUDS Estudiante------------------------------------------

-- CREATE
Delimiter $$
create procedure sp_create_estudiante(
    in ID_estudiante_p varchar(36),
    in nombre_estudiante_p varchar(50),
    in apellido_estudiante_p varchar(50)
)
begin
    insert into Estudiante(ID_estudiante, nombre_estudiante, apellido_estudiante)
    values(ID_estudiante_p, nombre_estudiante_p, apellido_estudiante_p);
end$$
Delimiter ;

-- READ
Delimiter $$
create procedure sp_read_estudiante()
begin
    select * from Estudiante;
end$$
Delimiter ;

-- UPDATE
Delimiter $$
create procedure sp_update_estudiante(
    in ID_estudiante_p varchar(36),
    in nombre_estudiante_p varchar(50),
    in apellido_estudiante_p varchar(50)
)
begin
    update Estudiante
    set
        nombre_estudiante = nombre_estudiante_p,
        apellido_estudiante = apellido_estudiante_p
    where ID_estudiante = ID_estudiante_p;
end$$
Delimiter ;

-- DELETE
Delimiter $$
create procedure sp_delete_estudiante(in ID_estudiante_p varchar(36))
begin
    delete from Estudiante where ID_estudiante = ID_estudiante_p;
end$$
Delimiter ;

-- SEARCH
Delimiter $$
create procedure sp_search_estudiante(in ID_estudiante_p varchar(36))
begin
    select * from Estudiante where ID_estudiante = ID_estudiante_p;
end$$
Delimiter ;

-- -------------------------------------CRUDS Jefe-------------------------------------------

-- CREATE
Delimiter $$
create procedure sp_create_jefe(
    in ID_jefe_p varchar(36),
    in nombre_jefe_p varchar(50),
    in apellido_jefe_p varchar(50),
    in correo_p varchar(50),
    in contrasena_p varchar(30)
)
begin
    insert into Jefe(ID_jefe, nombre_jefe, apellido_jefe, correo, contrasena)
    values(ID_jefe_p, nombre_jefe_p, apellido_jefe_p, correo_p, contrasena_p);
end$$
Delimiter ;

-- READ
Delimiter $$
create procedure sp_read_jefe()
begin
    select * from Jefe;
end$$
Delimiter ;

-- UPDATE
Delimiter $$
create procedure sp_update_jefe(
    in ID_jefe_p varchar(36),
    in nombre_jefe_p varchar(50),
    in apellido_jefe_p varchar(50),
    in correo_p varchar(50),
    in contrasena_p varchar(30)
)
begin
    update Jefe
    set
        nombre_jefe = nombre_jefe_p,
        apellido_jefe = apellido_jefe_p,
        correo = correo_p,
        contrasena = contrasena_p
    where ID_jefe = ID_jefe_p;
end$$
Delimiter ;

-- DELETE
Delimiter $$
create procedure sp_delete_jefe(in ID_jefe_p varchar(36))
begin
    delete from Jefe where ID_jefe = ID_jefe_p;
end$$
Delimiter ;

-- SEARCH
Delimiter $$
create procedure sp_search_jefe(in ID_jefe_p varchar(36))
begin
    select * from Jefe where ID_jefe = ID_jefe_p;
end$$
Delimiter ;

-- --------------------------------------CRUDS Bibliotecario----------------------------------

-- CREATE
Delimiter $$
create procedure sp_create_bibliotecario(
    in ID_bibliotecario_p varchar(36),
    in nombre_bibliotecario_p varchar(50),
    in apellido_bibliotecario_p varchar(50),
    in correo_p varchar(50),
    in contrasena_p varchar(30)
)
begin
    insert into Bibliotecario(ID_bibliotecario, nombre_bibliotecario, apellido_bibliotecario, correo, contrasena)
    values(ID_bibliotecario_p, nombre_bibliotecario_p, apellido_bibliotecario_p, correo_p, contrasena_p);
end$$
Delimiter ;

-- READ
Delimiter $$
create procedure sp_read_bibliotecario()
begin
    select * from Bibliotecario;
end$$
Delimiter ;

-- UPDATE
Delimiter $$
create procedure sp_update_bibliotecario(
    in ID_bibliotecario_p varchar(36),
    in nombre_bibliotecario_p varchar(50),
    in apellido_bibliotecario_p varchar(50),
    in correo_p varchar(50),
    in contrasena_p varchar(30)
)
begin
    update Bibliotecario
    set
        nombre_bibliotecario = nombre_bibliotecario_p,
        apellido_bibliotecario = apellido_bibliotecario_p,
        correo = correo_p,
        contrasena = contrasena_p
    where ID_bibliotecario = ID_bibliotecario_p;
end$$
Delimiter ;

-- DELETE
Delimiter $$
create procedure sp_delete_bibliotecario(in ID_bibliotecario_p varchar(36))
begin
    delete from Bibliotecario where ID_bibliotecario = ID_bibliotecario_p;
end$$
Delimiter ;

-- SEARCH
Delimiter $$
create procedure sp_search_bibliotecario(in ID_bibliotecario_p varchar(36))
begin
    select * from Bibliotecario where ID_bibliotecario = ID_bibliotecario_p;
end$$
Delimiter ;

-- ----------------------------------CRUDS Prestamo--------------------------------------------

-- CREATE
Delimiter $$
create procedure sp_create_prestamo(
    in ID_prestamo_p varchar(36),
    in ID_estudiante_p varchar(36),
    in ISBN_p varchar(13),
    in fecha_adquisicion_p date,
    in fecha_vencimiento_p date,
    in aprobacion_prestamo_p boolean
)
begin
    insert into Prestamo(ID_prestamo, ID_estudiante, ISBN, fecha_adquisicion, fecha_vencimiento, aprobacion_prestamo)
    values(ID_prestamo_p, ID_estudiante_p, ISBN_p, fecha_adquisicion_p, fecha_vencimiento_p, aprobacion_prestamo_p);
end$$
Delimiter ;

-- READ
Delimiter $$
create procedure sp_read_prestamo()
begin
    select * from Prestamo;
end$$
Delimiter ;

-- UPDATE
Delimiter $$
create procedure sp_update_prestamo(
    in ID_prestamo_p varchar(36),
    in ID_estudiante_p varchar(36),
    in ISBN_p varchar(13),
    in fecha_adquisicion_p date,
    in fecha_vencimiento_p date,
    in aprobacion_prestamo_p boolean
)
begin
    update Prestamo
    set
        ID_estudiante = ID_estudiante_p,
        ISBN = ISBN_p,
        fecha_adquisicion = fecha_adquisicion_p,
        fecha_vencimiento = fecha_vencimiento_p,
        aprobacion_prestamo = aprobacion_prestamo_p
    where ID_prestamo = ID_prestamo_p;
end$$
Delimiter ;

-- DELETE
Delimiter $$
create procedure sp_delete_prestamo(in ID_prestamo_p varchar(36))
begin
    delete from Prestamo where ID_prestamo = ID_prestamo_p;
end$$
Delimiter ;

-- SEARCH
Delimiter $$
create procedure sp_search_prestamo(in ID_prestamo_p varchar(36))
begin
    select * from Prestamo where ID_prestamo = ID_prestamo_p;
end$$
Delimiter ;