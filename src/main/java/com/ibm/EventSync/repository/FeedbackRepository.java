package com.ibm.EventSync.repository;

import com.ibm.EventSync.model.Feedback;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
@AllArgsConstructor
public class FeedbackRepository {

    private final JdbcTemplate jdbcTemplate;

    private RowMapper<Feedback> feedbackRowMapper() {
        return (rs, rowNum) -> Feedback.builder()
                .id(UUID.fromString(rs.getString("ID")))
                .event(UUID.fromString(rs.getString("EVENT")))
                .submitter(rs.getString("SUBMITTER"))
                .feedback(rs.getString("FEEDBACK"))
                .positive(rs.getDouble("POSITIVE"))
                .neutral(rs.getDouble("NEUTRAL"))
                .negative(rs.getDouble("NEGATIVE"))
                .submitted(rs.getObject("SUBMITTED", Instant.class))
                .build();
    }

    public Feedback getFeedback(UUID eventId, UUID id) {
        String sql = "SELECT * FROM event_feedback WHERE ID = ? AND EVENT = ?";
        Feedback feedback = jdbcTemplate.queryForObject(
                sql,
                feedbackRowMapper(),
                id,
                eventId
        );
        return feedback;
    }

    public List<Feedback> getFeedbackByEvent(UUID eventid) {
        String sql = "SELECT * FROM event_feedback WHERE EVENT = ?";
        List<Feedback> feedback = jdbcTemplate.query(
                sql,
                feedbackRowMapper(),
                eventid
        );
        return feedback;
    }

    public void insertFeedback(Feedback feedback) {
        String sql = "INSERT INTO event_feedback (ID, EVENT, SUBMITTER, FEEDBACK, POSITIVE, NEUTRAL, NEGATIVE) VALUES (?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(
                sql,
                feedback.getId(),
                feedback.getEvent(),
                feedback.getSubmitter(),
                feedback.getFeedback(),
                feedback.getPositive(),
                feedback.getNeutral(),
                feedback.getNegative()
        );

    }

    public void setSentiment(UUID id, Double positive, Double neutral, Double negative) {
        String sql = "UPDATE event_feedback SET POSITIVE = ?, NEUTRAL = ?, NEGATIVE = ? WHERE ID = ?";
        jdbcTemplate.update(
                sql,
                positive,
                neutral,
                negative,
                id
        );
    }

    public void deleteFeedback(UUID eventId,UUID id) {
        String sql = "DELETE FROM event_feedback WHERE ID = ? AND EVENT = ?";
        jdbcTemplate.update(
                sql,
                id,
                eventId
        );
    }

}
