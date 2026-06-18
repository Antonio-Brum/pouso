package com.pouso.repository;

import com.pouso.model.Person;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PersonRepository {

    private final JdbcTemplate jdbc;

    public PersonRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public Person buscarPorEmail(String email) {
        String sql = """
                SELECT cpf, senha
                FROM pessoa
                WHERE email = ?
            """;

        List<Person> Persons = jdbc.query(
            sql,
            (rs, rowNum) ->
                new Person(rs.getString("cpf"), rs.getString("senha")),
            email
        );

        if (Persons.isEmpty()) {
            return null;
        }

        return Persons.get(0);
    }
}
