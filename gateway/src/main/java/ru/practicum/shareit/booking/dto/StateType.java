package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.exception.UnknownStateException;

public enum StateType {
    WAITING,
    REJECTED,
    APPROVED,
    ALL,
    FUTURE,
    CURRENT,
    PAST;

    public static StateType fromString(String type) {
        for (StateType stateType : StateType.values()) {
            if (stateType.name().equals(type))
                return stateType;
        }
        throw new UnknownStateException(type);
    }
}
