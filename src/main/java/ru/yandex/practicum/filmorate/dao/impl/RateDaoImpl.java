package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.RateDao;
import ru.yandex.practicum.filmorate.model.Rate;

import java.util.ArrayList;
import java.util.Optional;

@Component
public class RateDaoImpl implements RateDao {
    private final JdbcTemplate jdbcTemplate;

    public RateDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Rate> findRateById(int id) {
        SqlRowSet rateRows = jdbcTemplate.queryForRowSet("SELECT * FROM rates WHERE id = ?", id);

        if (rateRows.next()) {
            Rate rate = new Rate(
                    rateRows.getInt("id"),
                    rateRows.getString("name")
            );

            return Optional.of(rate);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public ArrayList<Rate> getAllRates() {
        SqlRowSet rateRows = jdbcTemplate.queryForRowSet("SELECT * FROM rates");

        ArrayList<Rate> rates = new ArrayList<>();

        while (rateRows.next()) {
            rates.add(new Rate(
                rateRows.getInt("id"),
                rateRows.getString("name")
            ));
        }

        return rates;
    }
}
