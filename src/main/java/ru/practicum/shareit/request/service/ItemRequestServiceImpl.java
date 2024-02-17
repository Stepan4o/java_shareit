package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.ItemRequestMapper;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoIn;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;

    @Override
    public ItemRequestDto addNewItemRequest(ItemRequestDtoIn itemRequestDtoIn, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(String.format(
                        "Пользователь с id:%d не найден", userId
                )));
        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestDtoIn);
        itemRequest.setUser(user);
        itemRequestRepository.save(itemRequest);
        return ItemRequestMapper.toItemRequestDto(itemRequest, user.getId());
    }

    @Override
    public List<ItemRequestDto> getItemRequestsByOwnerId(Long userId) {

        // пока заглушка
        return null;
    }
}
