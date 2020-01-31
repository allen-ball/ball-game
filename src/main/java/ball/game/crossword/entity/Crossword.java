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
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

/**
 * {@link Crossword} {@link Entity}.
 *
 * {@bean.info}
 *
 * @author {@link.uri mailto:ball@hcf.dev Allen D. Ball}
 * @version $Revision$
 */
@Entity
@Table(catalog = "crossword", name = "crossword")
@NoArgsConstructor @EqualsAndHashCode(callSuper = false) @ToString
public class Crossword extends AbstractEntity {
    @Id @GeneratedValue(strategy = IDENTITY)
    private long id = -1;

    @OneToMany(mappedBy = "crossword", cascade = ALL)
    @Getter @Setter
    private List<Header> headers = new ArrayList<>();

    @ManyToOne(fetch = LAZY)
    @Getter @Setter
    private Grid grid = null;

    @OneToMany(mappedBy = "crossword", cascade = ALL)
    @Getter @Setter
    private List<Clue> clues = new ArrayList<>();

    @Column @Lob
    @Getter @Setter
    private String notes = null;
}
