//package ru.practicum.shareit.item.model;
//
//import lombok.*;
//import ru.practicum.shareit.user.model.User;
//
//import javax.persistence.*;
//import java.time.LocalDateTime;
//
//@Entity
//@Table(name = "comments")
//@Getter
//@Setter
//@Builder
//@AllArgsConstructor
//@NoArgsConstructor
//public class Comment {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(nullable = false)
//    private String text;
//
//    @ManyToOne
//    @JoinColumn(name = "author_id")
//    private User author;
//
//    @ManyToOne
//    @JoinColumn(name = "item_id")
//    private Item item;
//
//    @Column(nullable = false)
//    private LocalDateTime created;
//}
