package ru.practicum.shareit.item.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentDtoIn;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class CommentMapper {
    public Comment toComment(CommentDtoIn commentDtoIn, User user, Item item) {
        return Comment.builder()
                .text(commentDtoIn.getText())
                .created(commentDtoIn.getCreate())
                .author(user)
                .item(item)
                .build();
    }

    public CommentDto toCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorName(comment.getAuthor().getName())
                .created(comment.getCreated())
                .build();
    }

    public List<CommentDto> toCommentsDto(List<Comment> comments) {
        List<CommentDto> dtoComments = new ArrayList<>();
        for (Comment comment : comments) {
            CommentDto commentDto = toCommentDto(comment);
            dtoComments.add(commentDto);
        }
        return dtoComments;
    }
}
