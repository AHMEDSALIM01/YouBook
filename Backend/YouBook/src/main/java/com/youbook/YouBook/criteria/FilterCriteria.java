package com.youbook.YouBook.criteria;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FilterCriteria implements Serializable {
    private String city;
    private String hotelName;
    private Double prixMin;
    private Double prixMax;
    private LocalDate availabilityStart;
    private LocalDate availabilityEnd;
}
