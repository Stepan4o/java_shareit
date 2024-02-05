package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.*;
import java.util.function.BiConsumer;


@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public ItemDto createItem(ItemDto itemDto, Long userId) {
        User savedUser = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(
                        String.format("Пользователь с id:%d не найден", userId)
                ));
        Item item = ItemMapper.toItem(itemDto);
        item.setUser(savedUser);
        itemRepository.save(item);
        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemDto getItemById(Long id, Long userId) {
        Item item = itemRepository.findById(id).orElseThrow(
                () -> new NotFoundException(
                        String.format("Предмет с id:%d не найден", id)
                ));
        log.debug("userId:{} поиск вещи по itemId:{}", userId, item.getId());
        return ItemMapper.toItemDto(item);
    }

    @Override
    public List<ItemDto> getAllItemsByUserId(Long userId) {
        User savedUser = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(
                        String.format("Пользователь с id:%d не найден", userId)
                ));
        List<Item> itemList = itemRepository.findByUserId(savedUser.getId());

        return ItemMapper.toItemsDto(itemList);
    }

    @Override
    public List<ItemDto> getItemsBySubstring(String searchText, Long userId) {
        if (searchText.isBlank()) {
            return new ArrayList<>();
        } else {
            List<Item> itemList = itemRepository
                    .findAllByAvailableTrueAndDescriptionContainingIgnoreCaseOrNameContainingIgnoreCase(
                            searchText, searchText
                    );

            log.debug("userId:{} поиск вещи по строке text:{}", userId, searchText);
            return ItemMapper.toItemsDto(itemList);
        }
    }

    @Override
    public ItemDto patchUpdateItem(ItemDto itemDto, Long id, Long userId) {
        Item item = itemRepository.findById(id).orElseThrow(
                () -> new NotFoundException(
                        String.format("Вещь с id:%d не найдена", id)
                ));

        if (Objects.equals(item.getUser().getId(), userId)) {

            Item updatedItem = updateItemFields(item, itemDto);
            itemRepository.save(updatedItem);
            return ItemMapper.toItemDto(updatedItem);

        } else {
            throw new AccessDeniedException(
                    "Внесение изменений доступно только владельцам"
            );
        }
    }

    private Item updateItemFields(Item item, ItemDto itemDto) {
        Map<String, BiConsumer<Item, ItemDto>> fieldsUpdaters = new HashMap<>();
        fieldsUpdaters.put("name", (i, iDto) -> i.setName(iDto.getName()));
        fieldsUpdaters.put("description", (i, iDto) -> i.setDescription(iDto.getDescription()));
        fieldsUpdaters.put("available", (i, iDto) -> i.setAvailable(iDto.getAvailable()));

        fieldsUpdaters.forEach((field, updater) -> {
            switch (field) {
                case "name":
                    if (itemDto.getName() != null) {
                        updater.accept(item, itemDto);
                    }
                    break;
                case "description":
                    if (itemDto.getDescription() != null) {
                        updater.accept(item, itemDto);
                    }
                    break;
                case "available":
                    if (itemDto.getAvailable() != null) {
                        updater.accept(item, itemDto);
                    }
                    break;
            }
        });
        return item;
    }
}
