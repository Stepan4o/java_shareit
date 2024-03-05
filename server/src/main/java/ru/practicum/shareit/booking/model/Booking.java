//package ru.practicum.shareit.booking.model;
//
//import lombok.*;
//import ru.practicum.shareit.item.model.Item;
//import ru.practicum.shareit.user.model.User;
//
//import javax.persistence.*;
//import java.time.LocalDateTime;
//
//@Entity
//@Table(name = "bookings")
//@Getter
//@Setter
//@Builder
//@AllArgsConstructor
//@NoArgsConstructor
//public class Booking {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "item_id")
//    private Item item;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id")
//    private User user;
//
//    @Column(name = "start_of", nullable = false)
//    private LocalDateTime start;
//
//    @Column(name = "end_of", nullable = false)
//    private LocalDateTime end;
//
//    @Column(name = "status", nullable = false, columnDefinition = "varchar(50) default 'WAITING'")
//    @Enumerated(EnumType.STRING)
//    private StateType stateType = StateType.WAITING;
//}
