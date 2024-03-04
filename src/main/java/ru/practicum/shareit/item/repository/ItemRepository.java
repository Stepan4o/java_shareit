package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByUserId(Long userId, Pageable pageable);

    List<Item> findAllByAvailableTrueAndDescriptionContainingIgnoreCaseOrNameContainingIgnoreCase(
            String description, String name, Pageable pageable
    );

    List<Item> findAllByItemRequestIdOrderByItemRequestCreatedAsc(Long itemRequestId);
}
