package com.pouso.repository;

import com.pouso.dto.PetOwnerListDTO.OwnerItem;
import com.pouso.dto.PetOwnerListDTO.PetItem;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Repository
public class PetRepository {

    private static final Set<String> ALLOWED_SORT = Set.of("nome", "data_registro", "pet_count");
    private static final Set<String> ALLOWED_DIR = Set.of("asc", "desc");

    private final JdbcTemplate jdbc;

    public PetRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<OwnerItem> listarProprietariosPaginado(int offset, int limit, String sortBy, String sortDir, String q) {
        String col = ALLOWED_SORT.contains(sortBy) ? sortBy : "nome";
        String dir = ALLOWED_DIR.contains(sortDir) ? sortDir.toUpperCase() : "ASC";

        String orderClause;
        switch (col) {
            case "data_registro":
                orderClause = "MAX(p.data_registro) " + dir;
                break;
            case "pet_count":
                orderClause = "COUNT(pet.nome) " + dir;
                break;
            default:
                orderClause = "p.nome " + dir;
        }

        boolean hasFilter = q != null && !q.isBlank();

        String sql = """
                SELECT p.cpf, p.nome, COUNT(pet.nome) AS pet_count
                FROM usuario u
                JOIN pessoa p ON p.cpf = u.cpf
                JOIN pet ON pet.cpf_dono = u.cpf
                %s
                GROUP BY p.cpf, p.nome
                ORDER BY %s
                LIMIT ? OFFSET ?
                """.formatted(
                hasFilter ? "WHERE LOWER(p.nome) LIKE LOWER(?) OR LOWER(pet.nome) LIKE LOWER(?)" : "",
                orderClause
        );

        Object[] params;
        if (hasFilter) {
            params = new Object[]{"%" + q + "%", "%" + q + "%", limit, offset};
        } else {
            params = new Object[]{limit, offset};
        }

        return jdbc.query(sql, (rs, rowNum) -> {
            return new OwnerItem(
                    rs.getString("cpf"),
                    rs.getString("nome"),
                    rs.getLong("pet_count")
            );
        }, params);
    }

    public long contarProprietarios(String q) {
        boolean hasFilter = q != null && !q.isBlank();

        String sql = """
                SELECT COUNT(DISTINCT u.cpf)
                FROM usuario u
                JOIN pessoa p ON p.cpf = u.cpf
                JOIN pet ON pet.cpf_dono = u.cpf
                %s
                """.formatted(
                hasFilter ? "WHERE LOWER(p.nome) LIKE LOWER(?) OR LOWER(pet.nome) LIKE LOWER(?)" : ""
        );

        Object[] params = hasFilter ? new Object[]{"%" + q + "%", "%" + q + "%"} : new Object[]{};
        Long result = jdbc.queryForObject(sql, Long.class, params);
        return result != null ? result : 0;
    }

    public List<PetItem> buscarPetsPorCpfs(List<String> cpfs) {
        if (cpfs == null || cpfs.isEmpty()) {
            return Collections.emptyList();
        }

        StringBuilder sql = new StringBuilder("""
                SELECT pet.nome, tp.nome AS tipo_pet_nome, pet.cpf_dono
                FROM pet
                LEFT JOIN tipo_pet tp ON tp.id = pet.tipo_pet
                WHERE pet.cpf_dono IN (
                """);

        String placeholders = cpfs.stream().map(c -> "?").collect(java.util.stream.Collectors.joining(","));
        sql.append(placeholders);
        sql.append(") ORDER BY pet.nome");

        return jdbc.query(sql.toString(), (rs, rowNum) -> {
            return new PetItem(
                    rs.getString("nome"),
                    rs.getString("tipo_pet_nome"),
                    rs.getString("cpf_dono")
            );
        }, cpfs.toArray());
    }
}
