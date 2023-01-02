package com.youbook.YouBook.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.youbook.YouBook.enums.StatusHotel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,property = "id")
public class Hotel implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Column(unique = true)
    private String name;
    @NotNull
    @Column(unique = true)
    private String address;
    @NotNull
    private String city;
    @NotNull
    private int numberOfRooms;
    private LocalDate startNonAvailable;
    private LocalDate endNonAvailable;
    @Enumerated(EnumType.STRING)
    private StatusHotel status;
    @OneToMany(mappedBy = "hotel",fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JsonIgnoreProperties("hotel")
    private List<Room> rooms;
    @ManyToOne
    @JoinColumn(name = "owner_id")
    @JsonIgnoreProperties("hotels")
    private Users owner;
}
