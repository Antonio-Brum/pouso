-- Senhas em plaintext (consistente com auth atual sem BCrypt)
INSERT INTO pessoa (cpf, nome, email, senha) VALUES
    ('11111111111', 'Admin Supremo', 'admin@pouso.com', 'admin123'),
    ('22222222222', 'Admin Moderado', 'mod@pouso.com', 'mod123'),
    ('33333333333', 'João Silva', 'joao@email.com', '123456'),
    ('44444444444', 'Maria Souza', 'maria@email.com', '123456'),
    ('55555555555', 'Carlos Pereira', 'carlos@email.com', '123456');

INSERT INTO administrador (cpf, nivel) VALUES
    ('11111111111', 'S'),
    ('22222222222', 'M');

INSERT INTO usuario (cpf, username, bio, genero, telefone) VALUES
    ('33333333333', 'joaosilva', 'Amante de animais', 'M', '11911111111'),
    ('44444444444', 'mariasouza', 'Veterinária apaixonada por pets', 'F', '11922222222'),
    ('55555555555', 'carlosp', 'Procurando um companheiro', 'M', '11933333333');

INSERT INTO endereco (usuario_cpf, cep, rua, numero, bairro, cidade, uf) VALUES
    ('33333333333', '01001000', 'Rua das Flores', '123', 'Centro', 'São Paulo', 'SP'),
    ('44444444444', '20040000', 'Av. Atlântica', '500', 'Copacabana', 'Rio de Janeiro', 'RJ'),
    ('55555555555', '30140000', 'Rua dos Pinheiros', '80', 'Funcionários', 'Belo Horizonte', 'MG');
