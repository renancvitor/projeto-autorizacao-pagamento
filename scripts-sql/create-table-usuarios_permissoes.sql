create table usuarios_permissoes (
	id_usuario int,
    id_permissao int,
    primary key (id_usuario, id_permissao),
    foreign key (id_usuario) references usuarios(id),
    foreign key (id_permissao) references permissoes(id)
);