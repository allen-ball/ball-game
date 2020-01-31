/*
 * $Id$
 *
 * Copyright 2019, 2020 Allen D. Ball.  All rights reserved.
 */
package ball.game.crossword.entity;

import ball.persistence.entity.AbstractEntity;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import static javax.persistence.CascadeType.ALL;

/**
 * {@link Grid} {@link Entity}.
 *
 * {@bean.info}
 *
 * @author {@link.uri mailto:ball@hcf.dev Allen D. Ball}
 * @version $Revision$
 */
@Entity
@Table(catalog = "crossword", name = "grid")
@IdClass(Grid.PK.class)
@NoArgsConstructor @EqualsAndHashCode(callSuper = false) @ToString
public class Grid extends AbstractEntity {
    @Id @Column(nullable = false)
    @Getter @Setter
    private int height = -1;

    @Id @Column(nullable = false)
    @Getter @Setter
    private int width = -1;

    @Id @Column @Lob
    @Getter @Setter
    private String string = null;

    @OneToMany(mappedBy = "grid", cascade = ALL)
    @Getter @Setter
    private List<Crossword> crosswords = new ArrayList<>();

    @NoArgsConstructor @EqualsAndHashCode(callSuper = false) @ToString
    public static class PK {
        @Getter @Setter private int height = -1;
        @Getter @Setter private int width = -1;
        @Getter @Setter private String string = null;
    }
}
