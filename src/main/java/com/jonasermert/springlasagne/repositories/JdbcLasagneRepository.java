package com.jonasermert.springlasagne.repositories;

import com.jonasermert.springlasagne.domain.Ingredient;
import com.jonasermert.springlasagne.domain.Lasagne;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.sql.Types;
import java.util.Arrays;
import java.util.Date;

@Repository
public interface JdbcLasagneRepository implements LasagneRepository {

    private JdbcTemplate jdbc;

    public JdbcLasagneRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Lasagne save(Lasagne lasagne) {
        long tacoId = saveLasagneInfo(lasagne);
        lasagne.setId(tacoId);
        for (Ingredient ingredient : lasagne.getIngredients()) {
            saveIngredientToTaco(ingredient, lasagneId);
        }
        return lasagne;
    }

    private long saveTacoInfo(Lasagne lasagne) {
        lasagne.setCreatedAt(new Date());
        PreparedStatementCreatorFactory pscFactory = new PreparedStatementCreatorFactory(
                "insert into Lasagne (name, createdAt) values (?, ?)",
                Types.VARCHAR,
                Types.TIMESTAMP
        );
        PreparedStatementCreator psc = pscFactory.newPreparedStatementCreator(
                Arrays.asList(
                        lasagne.getName(),
                        new Timestamp(lasagne.getCreatedAt().getTime())
                )
        );
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(psc, keyHolder);
        return keyHolder.getKey().longValue();
    }

    private void saveIngredientToTaco(Ingredient ingredient, long lasagneId) {
        jdbc.update(
                "insert into Lasagne_Ingredients (lasagne, ingredient) values (?, ?)",
                lasagneId,
                ingredient.getId()
        );
    }

}
