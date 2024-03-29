package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
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

import static ru.practicum.shareit.utils.Constants.ITEM_REQUEST_NOT_FOUND;
import static ru.practicum.shareit.utils.Constants.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public ItemRequestDto addNewItemRequest(ItemRequestDtoIn itemRequestDtoIn, long userId) {
        User savedUser = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(String.format(
                        USER_NOT_FOUND, userId
                )));
        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestDtoIn);
        itemRequest.setUser(savedUser);

        return ItemRequestMapper.toItemRequestDto(itemRequestRepository.save(itemRequest));
    }

    @Override
    public ItemRequestDto getItemRequestById(long userId, long requestId) {
        checkUser(userId);
        ItemRequest itemRequest = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException(String.format(
                        ITEM_REQUEST_NOT_FOUND, requestId
                )));
        ItemRequestDto itemRequestDto = ItemRequestMapper.toItemRequestDto(itemRequest);
        setItemsToRequest(itemRequestDto);

        return itemRequestDto;
    }

    @Override
    public List<ItemRequestDto> getItemRequestsByOwnerId(long userId) {
        checkUser(userId);
        List<ItemRequest> itemRequests = itemRequestRepository.findAllByUserId(userId);

        List<ItemRequestDto> itemRequestsDto = itemRequests.stream()
                .map(ItemRequestMapper::toItemRequestDto)
                .collect(Collectors.toList());
        itemRequestsDto.forEach(this::setItemsToRequest);

        return itemRequestsDto;
    }

    @Override
    public List<ItemRequestDto> getAllItemRequests(long userId, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size, Sort.by("created").descending());
        List<ItemRequest> itemRequests = itemRequestRepository.findAllByUserIdIsNot(userId, pageable);

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

    private void checkUser(long userId) {
        if (!userRepository.existsById(userId))
            throw new NotFoundException(String.format(
                    USER_NOT_FOUND, userId
            ));
    }
}
