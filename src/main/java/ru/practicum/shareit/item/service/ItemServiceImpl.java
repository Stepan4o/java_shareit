package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
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
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.BiConsumer;


@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

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
        List<Comment> comments = commentRepository.findAllByItemId(id);
        if (Objects.equals(item.getUser().getId(), (userId))) {
            Optional<Booking> nextBooking = bookingRepository
                    .findNextBookingByItemId(id, StateType.APPROVED)
                    .stream().findFirst();

            Optional<Booking> lastBooking = bookingRepository
                    .findLastBookingByItemId(id, StateType.APPROVED)
                    .stream().findFirst();

            ItemDto itemDto = ItemMapper.toItemWithBookingDto(item);
            nextBooking.ifPresent(booking ->
                    itemDto.setNextBooking(BookingMapper.toNextBookingDto(booking)));
            lastBooking.ifPresent(booking ->
                    itemDto.setLastBooking(BookingMapper.toLastBookingDto(booking)));

            itemDto.setComments(CommentMapper.toCommentsDto(comments));
            return itemDto;
        } else {
            log.debug("userId:{} поиск вещи по itemId:{}", userId, item.getId());

            ItemDto itemDto = ItemMapper.toItemDto(item);
            itemDto.setComments(CommentMapper.toCommentsDto(comments));
            return itemDto;
        }
    }

    @Override
    public List<ItemDto> getAllItemsByUserId(Long userId) {
        User savedUser = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(
                        String.format("Пользователь с id:%d не найден", userId)
                ));
        List<Item> itemList = itemRepository.findByUserId(savedUser.getId());
        if (itemList.get(0).getUser().getId().equals(userId)) {
            List<ItemDto> res = new ArrayList<>();
            for (Item i : itemList) {
                List<Booking> nextBooking = bookingRepository
                        .findNextBookingByItemId(i.getId(), StateType.APPROVED);
                List<Booking> lastBooking = bookingRepository
                        .findLastBookingByItemId(i.getId(), StateType.APPROVED);

                ItemDto itemDto = ItemMapper.toItemWithBookingDto(i);

                if (nextBooking.isEmpty()) {
                    itemDto.setNextBooking(null);
                } else {
                    itemDto.setNextBooking(BookingMapper.toNextBookingDto(nextBooking.get(0)));
                }
                if (lastBooking.isEmpty()) {
                    itemDto.setLastBooking(null);
                } else {
                    itemDto.setLastBooking(BookingMapper.toLastBookingDto(lastBooking.get(0)));
                }
//                nextBooking.ifPresent(booking -> itemDto.setNextBooking(BookingMapper.toNextBookingDto(booking)));
//                lastBooking.ifPresent(booking -> itemDto.setLastBooking(BookingMapper.toLastBookingDto(booking)));

                List<Comment> comments = commentRepository.findAllByItemId(itemDto.getId());
                itemDto.setComments(CommentMapper.toCommentsDto(comments));

                res.add(itemDto);
            }
            return res;
        } else {
            List<ItemDto> items = ItemMapper.toItemsDto(itemList);
            for (ItemDto itemDto : items) {
                List<Comment> comments = commentRepository.findAllByItemId(itemDto.getId());
                itemDto.setComments(CommentMapper.toCommentsDto(comments));
            }
            return items;

        }
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
    public CommentDto addComment(Long userId, CommentDtoIn commentDtoIn, Long itemId) {
        Booking booking = bookingRepository
                .findFirstByUserIdAndItemIdAndEndBeforeAndStateType(userId, itemId, LocalDateTime.now(), StateType.APPROVED)
                .orElseThrow(() -> new NotAvailableException("Ошибка"));
        Comment comment = CommentMapper.toComment(commentDtoIn, booking.getUser(), booking.getItem());
        commentRepository.save(comment);
        return CommentMapper.toCommentDto(comment);
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
