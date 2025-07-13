package com.app.userservice.repos;

import com.app.userservice.models.CardInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface CardRepository extends JpaRepository<CardInfo, Integer> {
    @Query(value = "SELECT * FROM card_info WHERE id IN (:ids)", nativeQuery = true)
    List<CardInfo> findCardsByIds(List<Integer> ids);

    List<CardInfo> findAllByUserId(int userId);
}
