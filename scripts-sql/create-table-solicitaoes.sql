create table solicitacoes (
	id int auto_increment primary key,
    fornecedor varchar(255) not null,
    descricao text,
    data_criacao timestamp default current_timestamp,
    data_pagamento date,
    forma_pagamento varchar(255),
    parcelas int,
    valor_parcelas decimal (10, 2),
    valor_total decimal (10, 2),
    id_usuario int,
    foreign key (id_usuario) references usuarios(id)
);