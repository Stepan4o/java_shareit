package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.StateType;
import ru.practicum.shareit.client.BaseClient;

import java.util.Map;

@Service
public class BookingClient extends BaseClient {
    public static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> addNewBooking(long userId, BookingDtoRequest bookingDtoRequest) {
        return post("", userId, bookingDtoRequest);
    }

    public ResponseEntity<Object> approveBooking(long userId, long bookingId, boolean isApproved) {
        return patch(String.format("/%s?approved=%s", bookingId, isApproved), userId, null);
    }

    public ResponseEntity<Object> getBooking(long bookingId, long userId) {
        return get("/" + bookingId, userId);
    }

    public ResponseEntity<Object> getAllByOwner(long userId, StateType stateType, int from, int size) {
        Map<String, Object> params = Map.of(
                "state", stateType,
                "from", from,
                "size", size
        );
        return get("/owner?state={state}&from={from}&size={size}", userId, params);
    }

    public ResponseEntity<Object> getAllByUserId(long userId, StateType stateType, int from, int size) {
        Map<String, Object> params = Map.of(
                "state", stateType,
                "from", from,
                "size", size
        );
        return get("?state={state}&from={from}&size={size}", userId, params);
    }
}
