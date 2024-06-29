create table usuarios (
	id int auto_increment primary key,
    login varchar(255) not null unique,
    senha varchar(255) not null,
    id_tipo_usuario int,
    foreign key (id_tipo_usuario) references tipos_usuarios(id)
);