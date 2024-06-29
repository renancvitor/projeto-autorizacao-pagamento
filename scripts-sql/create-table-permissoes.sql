create table permissoes (
	id int auto_increment primary key,
    nome_permissao varchar(255) not null unique,
    descricao text
);