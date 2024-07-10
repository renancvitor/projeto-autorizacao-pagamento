alter table solicitacoes
add column status varchar(30) after valor_total;

select * from solicitacoes;