package org.studyeasy.SpringStarter.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Setter;
import lombok.Getter;

@Entity
@Getter
@Setter
public class Authority {
    @Id

    private Long id;

    private String name;

}
