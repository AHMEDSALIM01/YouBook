package com.youbook.YouBook.entities;

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
public class Users implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    @NotNull
    private String name;
    @NotNull
    private String address;
    @NotNull
    private String phoneNumber;
    @NotNull
    @Column(unique = true)
    public String email;
    @NotNull
    public String password;
    public Boolean is_active=false;
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
}
