//package com.volunteerplatform.model;
//
//import jakarta.persistence.*;
//import lombok.Getter;
//import lombok.Setter;
//
//import java.time.LocalDate;
//
//@Entity
//@Getter
//@Setter
//public class Availability {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    private User user;
//
//    @ManyToOne
//    @JoinColumn(name = "cause_id")
//    private Cause cause;
//
//    private LocalDate date;
//    private String time;
//}
