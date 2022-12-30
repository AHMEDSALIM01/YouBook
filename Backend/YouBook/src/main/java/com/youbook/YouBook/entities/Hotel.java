package com.youbook.YouBook.entities;

import com.youbook.YouBook.enums.StatusHotel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Hotel implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Column(unique = true)
    private String name;
    @Column(unique = true)
    @NotNull
    private String address;
    @NotNull
    private String city;
    @NotNull
    private int numberOfRooms;
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "String default En_cours")
    private StatusHotel status;
    @OneToMany(mappedBy = "hotel",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<Room> rooms;
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Users> users;
}
