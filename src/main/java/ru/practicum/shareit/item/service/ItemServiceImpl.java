package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public ItemDto addItem(ItemDto itemDto, Long userId) {
        User savedUser = userRepository.findById(userId);
        itemDto.setUserId(savedUser.getId());
        Item item = ItemMapper.toItem(itemDto);
        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    public ItemDto getItemById(Long id) {
        Item item = itemRepository.getById(id);
        return ItemMapper.toItemDto(itemRepository.getById(item.getId())
        );
    }

    @Override
    public List<ItemDto> getAllItemsByUserId(Long userId) {
        User savedUser = userRepository.findById(userId);
        List<Item> itemList = itemRepository.findAllByUserId(savedUser.getId());

        return itemList.stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> getItemsBySubstring(String text) {
        List<Item> itemList = itemRepository.findBySubstring(text);

        return itemList.stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto patchUpdateItem(ItemDto itemDto, Long id, Long userId) {
        User savedUser = userRepository.findById(userId);
        Item item = itemRepository.getById(id);

        return ItemMapper.toItemDto(itemRepository.update(
                itemDto, item.getId(), savedUser.getId()
        ));
    }
}
