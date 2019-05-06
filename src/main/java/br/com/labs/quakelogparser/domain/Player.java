package br.com.labs.quakelogparser.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Player {

    private String name;
    private Integer killsQuantity = 0;
}
