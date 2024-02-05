package ru.practicum.shareit.item;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByUserId(Long userId);

    List<Item> findAllByAvailableTrueAndDescriptionContainingIgnoreCaseOrNameContainingIgnoreCase(
            String description, String name
    );
//    List<Item> findByAvailableWhereDescriptionOrNameAllContainingIgnoreCase(boolean available, String description, String name);
}
