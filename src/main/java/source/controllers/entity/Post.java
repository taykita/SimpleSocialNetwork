package source.controllers.entity;

import javax.validation.constraints.NotEmpty;

public class Post {

    @NotEmpty
    private String text;

    private int id;
}
