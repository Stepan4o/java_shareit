package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ItemRepositoryImpl implements ItemRepository {
    private final List<Item> items = new ArrayList<>();
    private Long id = 0L;

    @Override
    public Item save(Item item) {
        item.setId(++id);
        items.add(item);
        return item;
    }

    @Override
    public Item update(ItemDto itemDto, Long id, Long userId) {
        Item item = getById(id);
        if (Objects.equals(item.getUserId(), userId)) {
            if (itemDto.getName() != null) {
                item.setName(itemDto.getName());
            }
            if (itemDto.getDescription() != null) {
                item.setDescription(itemDto.getDescription());
            }
            if (itemDto.getAvailable() != null) {
                item.setAvailable(itemDto.getAvailable());
            }
        } else {
            throw new AccessDeniedException(String.format(
                    "Пользователь id:%d не является владельцем вещи", userId
            ));
        }
        return item;
    }

    @Override
    public Item getById(Long id) {
        Optional<Item> item = items.stream()
                .filter(i -> Objects.equals(i.getId(), id)).findFirst();

        return item.orElseThrow(() -> new NotFoundException(
                String.format("Вещь с id:%d не найдена", id)));
    }

    @Override
    public List<Item> findAllByUserId(Long userId) {

        return items.stream()
                .filter(i -> Objects.equals(i.getUserId(), userId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> findBySubstring(String text) {

        return items.stream()
                .filter(Item::isAvailable)
                .filter(i -> i.getName().toLowerCase().contains(text)
                        || i.getDescription().toLowerCase().contains(text))
                .collect(Collectors.toList());
    }
}
