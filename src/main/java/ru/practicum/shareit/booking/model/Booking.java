package ru.practicum.shareit.booking.model;

import lombok.Data;
import ru.practicum.shareit.item.model.Item;

import javax.persistence.*;
import java.time.Instant;

@Data
@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "item_id")
    private Item item;

    @Column(name = "start")
    private Instant start;

    @Column(name = "end")
    private Instant end;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;


}
