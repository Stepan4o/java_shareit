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
    public Comment toComment (CommentDtoIn commentDtoIn, User user, Item item) {
        Comment comment = new Comment();
        comment.setText(commentDtoIn.getText());
        comment.setCreated(commentDtoIn.getCreate());
        comment.setAuthor(user);
        comment.setItem(item);
        return comment;
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
        List<CommentDto> res = new ArrayList<>();
        for (Comment comment : comments) {
            CommentDto commentDto = toCommentDto(comment);
            res.add(commentDto);
        }
        return res;
    }
}
