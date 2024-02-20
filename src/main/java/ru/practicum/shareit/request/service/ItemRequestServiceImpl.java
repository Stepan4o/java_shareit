package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.ItemRequestMapper;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoIn;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public ItemRequestDto addNewItemRequest(ItemRequestDtoIn itemRequestDtoIn, Long userId) {
        User user = getUser(userId);
        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestDtoIn);
        itemRequest.setUser(user);
        itemRequestRepository.save(itemRequest);
        return ItemRequestMapper.toItemRequestDto(itemRequest);
    }

    @Override()
    public ItemRequestDto getItemRequestByOwnerId(Long userId, Long requestId) {
        User user = getUser(userId);
        ItemRequest itemRequest = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException(String.format(
                        "ItemRequest с id:%d не найден", requestId
                )));
        ItemRequestDto itemRequestDto = ItemRequestMapper.toItemRequestDto(itemRequest);
        setItemsToRequest(itemRequestDto);

        return itemRequestDto;
    }

    @Override
    public List<ItemRequestDto> getItemRequestsByOwnerId(Long userId) {
        User user = getUser(userId);
        List<ItemRequest> itemRequests = itemRequestRepository.findAllByUserId(user.getId());

        List<ItemRequestDto> dtoList = itemRequests.stream()
                .map(ItemRequestMapper::toItemRequestDto)
                .collect(Collectors.toList());
        dtoList.forEach(this::setItemsToRequest);
        return dtoList;
    }

    @Override
    public List<ItemRequestDto> getAllItemRequests(Long userId, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size, Sort.by("created").descending());
        List<ItemRequest> itemRequests = itemRequestRepository.findAllByUserIdIsNot(userId,pageable);

        List<ItemRequestDto> itemRequestsDto = itemRequests.stream()
                .map(ItemRequestMapper::toItemRequestDto)
                .collect(Collectors.toList());
        itemRequestsDto.forEach(this::setItemsToRequest);
        return itemRequestsDto;
    }

    private void setItemsToRequest(ItemRequestDto itemRequestDto) {
        List<Item> items = itemRepository
                .findAllByItemRequestIdOrderByItemRequestCreatedAsc(itemRequestDto.getId());

        itemRequestDto.setItems(items.stream()
                .map(ItemMapper::toItemDtoShort)
                .collect(Collectors.toList()));
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(String.format(
                        "Пользователь с id:%d не найден", userId
                )));
    }
}
