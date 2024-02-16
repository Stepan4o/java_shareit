package ru.practicum.shareit.booking.model;

import java.util.Optional;

public enum StateType {
    WAITING,
    REJECTED,
    APPROVED,
    ALL,
    FUTURE,
    CURRENT,
    PAST;

    public static Optional<StateType> fromString(String state) {
        for (StateType type : StateType.values()) {
            if (type.name().equals(state)) {
                return Optional.of(type);
            }
        }
        return Optional.empty();
    }
}
