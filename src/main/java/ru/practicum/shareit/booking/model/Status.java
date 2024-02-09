package ru.practicum.shareit.booking.model;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

public enum Status {
    @Enumerated(EnumType.STRING)
    WAITING,
    REJECTED,
    APPROVED;
}
