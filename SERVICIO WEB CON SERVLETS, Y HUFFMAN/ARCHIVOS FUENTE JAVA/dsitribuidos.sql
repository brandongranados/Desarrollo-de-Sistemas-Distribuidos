create database distribuidos;

drop database distribuidos;

use distribuidos;

CREATE USER 'userDistribuidos'@'localhost' identified by 'S2019300476señales+';

grant all privileges on distribuidos.* to 'userDistribuidos'@'localhost';

flush privileges;

create table articulo (
idArticulo integer auto_increment primary key, nombre varchar(100) not null unique, 
descripcion varchar(200) not null, precio float not null, cantAlmacen bigint not null,
fechaRegistro datetime not null, foto longblob
);

create unique index nombreIndex on articulo(nombre);
create unique index descripINdex on articulo(descripcion);

create table carrito_compra (
idCarrito integer auto_increment, idArticulo integer, cantCompra integer not null,
primary key(idCarrito, idArticulo), foreign key(idArticulo) references articulo (idArticulo) 
on delete cascade on update cascade
);

create unique index idArticuloIndex on carrito_compra(idArticulo);
use distribuidos;
select * from articulo;