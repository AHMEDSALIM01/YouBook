package com.youbook.YouBook.entities;

import com.youbook.YouBook.enums.StatusReservation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Reservation implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String ref;
    @NotNull
    private Date startDate;
    @NotNull
    private Date endDate;
    @NotNull
    private Double totalPrice;
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "String default En_cours")
    private StatusReservation status;
    @Transient
    private static long counter = 0;
    @PrePersist
    public void generateReference() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        this.id = ++counter;
        this.ref = "Reservation N°" + year + "/" + this.id;
    }
}
