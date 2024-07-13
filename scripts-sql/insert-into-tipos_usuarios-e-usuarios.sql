insert into tipos_usuarios (tipo_usuario)
values ( 'admin' );

insert into usuarios (login, senha, id_tipo_usuario)
values ( 'admin', 'adminNewAdmin@1900', 1 );

select * from usuarios;

select * from solicitacoes;