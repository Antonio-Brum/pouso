package com.pouso.repository;

import com.pouso.model.UsuarioLista;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UsuarioRepository {

    private final JdbcTemplate jdbc;

    public UsuarioRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<UsuarioLista> listarTodos() {
        String sql = """
                SELECT p.cpf, p.nome, p.email, e.cidade
                FROM pessoa p
                JOIN usuario u ON u.cpf = p.cpf
                LEFT JOIN endereco e ON e.usuario_cpf = p.cpf
                ORDER BY p.nome
            """;
        return jdbc.query(sql, (rs, rowNum) ->
            new UsuarioLista(
                rs.getString("cpf"),
                rs.getString("nome"),
                rs.getString("email"),
                rs.getString("cidade")
            )
        );
    }

    public String buscarNivelAdmin(String cpf) {
        String sql = "SELECT nivel FROM administrador WHERE cpf = ?";
        List<String> resultados = jdbc.query(sql, (rs, rowNum) -> rs.getString("nivel"), cpf);
        return resultados.isEmpty() ? null : resultados.get(0);
    }
}
