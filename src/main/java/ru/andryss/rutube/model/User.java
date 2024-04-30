package ru.andryss.rutube.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

import static jakarta.persistence.EnumType.STRING;

@Getter
@Setter
@Entity
@Table(name = "act_id_user")
public class User {
    @Id
    @Column(name = "id_")
    String username;
    @Column(name = "rev_")
    Integer rev;
    @Column(name = "first_")
    String first;
    @Column(name = "last_")
    String last;
    @Column(name = "email_")
    String email;
    @Column(name = "pwd_")
    String pwd;
    @Column(name = "salt_")
    String salt;
    @Column(name = "lock_exp_time_")
    Instant lockExpTime;
    @Column(name = "attempts_")
    Integer attempts;
    @Column(name = "picture_id_")
    String pictureId;
}
