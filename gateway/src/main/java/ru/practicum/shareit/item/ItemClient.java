package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.CommentDtoRequest;
import ru.practicum.shareit.item.dto.ItemDtoRequest;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> addNewItem(long userId, ItemDtoRequest itemDtoRequest) {
        return post("", userId, itemDtoRequest);
    }

    public ResponseEntity<Object> updateItem(
            ItemDtoRequest itemDtoRequest,
            long itemId,
            long userId
    ) {
        return patch("/" + itemId, userId, itemDtoRequest);
    }

    public ResponseEntity<Object> getItemById(long itemId, long userId) {
        return get("/" + itemId, userId);
    }

    public ResponseEntity<Object> getItemByText(long userId, String text, int from, int size) {
        Map<String, Object> params = Map.of(
                "text", text,
                "from", from,
                "size", size
        );
        return get("/search?text={text}&from={from}&size={size}", userId, params);
    }

    public ResponseEntity<Object> getAllItemsByUserId(long userId, int from, int size) {
        Map<String, Object> params = Map.of(
                "from", from,
                "size", size
        );
        return get("?from={from}&size={size}", userId, params);
    }

    public ResponseEntity<Object> addCommentToItem(
            long userId,
            long itemId,
            CommentDtoRequest commentDtoRequest
    ) {
        return post("/" + itemId + "/comment", userId, commentDtoRequest);
    }
}
