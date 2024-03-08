package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.StateType;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.NotAvailableException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentDtoIn;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoIn;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.UserRepository;

import java.awt.print.Book;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.BiConsumer;

import static ru.practicum.shareit.utils.Constants.*;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemRequestRepository itemRequestRepository;

    @Override
    public ItemDto add(ItemDtoIn itemDtoIn, long userId) {
        User savedUser = getUserIfExist(userId);
        Item newItem = ItemMapper.toItem(itemDtoIn);
        newItem.setUser(savedUser);

        Long requestId = itemDtoIn.getRequestId();
        if (requestId != null) {
            setRequestIfExist(newItem, requestId);
        }
        return ItemMapper.toItemDto(itemRepository.save(newItem));
    }

    @Override
    @Transactional(readOnly = true)
    public ItemDto getItemById(long itemId, long userId) {
        Item item = getItemIfExist(itemId);
        ItemDto itemDto = ItemMapper.toItemDto(item);
        setComments(itemDto);

        if (isOwner(item.getUser().getId(), (userId))) {
            setNextAndLastBooking(itemDto);
        }
        return itemDto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDto> getAllItemsByOwnerId(long userId, int from, int size) {
        User savedUser = getUserIfExist(userId);
        Pageable pageable = PageRequest.of(from / size, size, Sort.by(Sort.Direction.ASC, "id"));
        List<Item> itemList = itemRepository.findByUserId(savedUser.getId(), pageable);

        List<ItemDto> itemsDto = ItemMapper.toItemsDto(itemList);
        itemsDto.forEach(this::setComments);
        itemsDto.forEach(this::setNextAndLastBooking);
        return itemsDto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDto> getItemsBySubstring(
            String searchText,
            long userId,
            int from,
            int size
    ) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(String.format(USER_NOT_FOUND, userId));
        }
        Pageable pageable = PageRequest.of(
                from / size, size, Sort.by(Sort.Direction.ASC, "id")
        );
        List<Item> itemList = itemRepository.searchItemsBySubstring(searchText, pageable);
        return ItemMapper.toItemsDto(itemList);

    }

    @Override
    public CommentDto addComment(long userId, CommentDtoIn commentDtoIn, long itemId) {
        Booking booking = bookingRepository.findFirstByUserIdAndItemIdAndEndBeforeAndStateType(
                userId,
                itemId,
                LocalDateTime.now(),
                StateType.APPROVED
        ).orElseThrow(() -> new NotAvailableException("Добавление комметария невозможно"));

        Comment newComment = CommentMapper.toComment(commentDtoIn, booking.getUser(), booking.getItem());
        return CommentMapper.toCommentDto(commentRepository.save(newComment));
    }

    @Override
    public ItemDto update(ItemDtoIn itemDtoIn, long itemId, long userId) {
        Item savedItem = getItemIfExist(itemId);

        if (isOwner(savedItem.getUser().getId(), userId)) {
            Item updatedItem = updateItemFields(savedItem, itemDtoIn);
            itemRepository.save(updatedItem);

            return ItemMapper.toItemDto(updatedItem);
        } else {
            throw new AccessDeniedException(OWNERS_ONLY);
        }
    }

    private Item updateItemFields(Item item, ItemDtoIn itemDtoIn) {
        Map<String, BiConsumer<Item, ItemDtoIn>> fieldsUpdaters = new HashMap<>();
        fieldsUpdaters.put("name", (i, iDto) -> i.setName(iDto.getName()));
        fieldsUpdaters.put("description", (i, iDto) -> i.setDescription(iDto.getDescription()));
        fieldsUpdaters.put("available", (i, iDto) -> i.setAvailable(iDto.getAvailable()));

        fieldsUpdaters.forEach((field, updater) -> {
            switch (field) {
                case "name":
                    if (itemDtoIn.getName() != null) {
                        updater.accept(item, itemDtoIn);
                    }
                    break;
                case "description":
                    if (itemDtoIn.getDescription() != null) {
                        updater.accept(item, itemDtoIn);
                    }
                    break;
                case "available":
                    if (itemDtoIn.getAvailable() != null) {
                        updater.accept(item, itemDtoIn);
                    }
                    break;
            }
        });
        return item;
    }

    private void setNextAndLastBooking(ItemDto itemDto) {
        LocalDateTime now = LocalDateTime.now();
        long itemId = itemDto.getId();

        Optional<Booking> nextBooking = bookingRepository
                .findFirstByItemIdAndStartAfterAndStateTypeOrderByStartAsc(
                        itemId,
                        now,
                        StateType.APPROVED
                );
        Optional<Booking> lastBooking = bookingRepository
                .findFirstByItemIdAndStartBeforeAndStateTypeOrderByStartDesc(
                        itemId,
                        now,
                        StateType.APPROVED
                );

        nextBooking.ifPresent(booking ->
                itemDto.setNextBooking(BookingMapper.toNextBookingDto(booking)));
        lastBooking.ifPresent(booking ->
                itemDto.setLastBooking(BookingMapper.toLastBookingDto(booking)));
    }

    private void setComments(ItemDto itemDto) {
        List<Comment> comments = commentRepository.findAllByItemId(itemDto.getId());
        List<CommentDto> commentsDtoList = CommentMapper.toCommentsDto(comments);
        itemDto.setComments(commentsDtoList);
    }

    private void setRequestIfExist(Item item, long requestId) {
        Optional<ItemRequest> itemRequest = itemRequestRepository.findById(requestId);
        item.setItemRequest(itemRequest.orElseThrow(
                () -> new NotFoundException(String.format(
                        ITEM_REQUEST_NOT_FOUND, requestId
                ))));
    }

    private User getUserIfExist(long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(
                        String.format(USER_NOT_FOUND, userId)
                ));
    }

    private Item getItemIfExist(long itemId) {
        return itemRepository.findById(itemId).orElseThrow(
                () -> new NotFoundException(
                        String.format(ITEM_NOT_FOUND, itemId)
                ));
    }

    private boolean isOwner(long ownerId, long userId) {
        return ownerId == userId;
    }
}
