package com.ibm.EventSync.repository;

import com.ibm.EventSync.model.Event;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


@Repository
@AllArgsConstructor
public class EventRepository {

    private final JdbcTemplate jdbcTemplate;


    public RowMapper<Event> eventRowMapper() {
        return (rs, rowNum) ->
                Event.builder().
                        id(UUID.fromString(rs.getString("ID")))
                        .title(rs.getString("TITLE"))
                        .description(rs.getString("DESCRIPTION"))
                        .build();
    }

    public Event getEvent(UUID id) {
        String sql = "SELECT * FROM events WHERE ID = ?";

        // Query and map result to Event object
        Event event = jdbcTemplate.queryForObject(
                sql,
                eventRowMapper(),
                id
        );
        return event;
    }

    public void insertEvent(Event event) {
        String sql = "INSERT INTO events (ID, TITLE, DESCRIPTION) VALUES (?, ?, ?)";
        jdbcTemplate.update(
                sql,
                event.getId(),       // UUID
                event.getTitle(),    // String
                event.getDescription() // String
        );
    }

    public void updateEvent(Event event) {
        String sql = "UPDATE events SET TITLE = ?, DESCRIPTION = ? WHERE ID = ?";
        jdbcTemplate.update(
                sql,
                event.getTitle(),
                event.getDescription(),
                event.getId()
        );
    }

    public void deleteEventById(UUID id) {
        String sql = "DELETE FROM events WHERE ID = ?";
        jdbcTemplate.update(
                sql,
                id
        );
    }

    public List<Event> getEvents() {
        String sql = "SELECT * FROM events";
        List<Event> events = jdbcTemplate.query(
                sql,
                eventRowMapper()
        );
        return events;
    }


}
