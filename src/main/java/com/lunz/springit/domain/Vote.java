package com.lunz.springit.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@RequiredArgsConstructor
@Getter @Setter
//@NoArgsConstructor
public class Vote {

    @Id
    @GeneratedValue
    private long id;
    private int vote;

    // user
    // link
}
