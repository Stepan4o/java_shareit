package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByUserId(long userId, Pageable pageable);

    @Query("SELECT i FROM Item as i " +
            "WHERE upper(i.name) LIKE UPPER(concat('%',?1,'%')) " +
            "OR upper(i.description) LIKE UPPER(concat('%',?1,'%')) " +
            "AND i.available = true")
    List<Item> searchItemsBySubstring(
            String text, Pageable pageable
    );

    List<Item> findAllByItemRequestIdOrderByItemRequestCreatedAsc(long itemRequestId);
}
