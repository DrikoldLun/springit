package com.lunz.springit.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@RequiredArgsConstructor
@Getter @Setter
@NoArgsConstructor
public class Vote extends Auditable {

    @Id
    @GeneratedValue
    private long id;

    @NonNull
    private short direction; // up 1 down -1

    @NonNull
    @ManyToOne
    private Link link;

    // user
    // link
}
