package com.vijay.journalify.repository;

import com.vijay.journalify.entity.JournalEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JournalEntryRepository extends JpaRepository<JournalEntry, Long> {

    Page<JournalEntry> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    Optional<JournalEntry> findByIdAndUserId(Long id, Long userId);

    @Query("""
        SELECT e FROM JournalEntry e
        WHERE e.user.id = :userId
          AND (:query IS NULL OR LOWER(e.title) LIKE LOWER(CONCAT('%', :query, '%'))
               OR LOWER(e.content) LIKE LOWER(CONCAT('%', :query, '%')))
          AND (:mood IS NULL OR e.mood = :mood)
          AND (:tag IS NULL OR LOWER(e.tags) LIKE LOWER(CONCAT('%', :tag, '%')))
        ORDER BY e.createdAt DESC
        """)
    Page<JournalEntry> findWithFilters(
            @Param("userId") Long userId,
            @Param("query") String query,
            @Param("mood") String mood,
            @Param("tag") String tag,
            Pageable pageable
    );

    long countByUserId(Long userId);
}