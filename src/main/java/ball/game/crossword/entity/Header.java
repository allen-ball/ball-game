/*
 * $Id$
 *
 * Copyright 2019, 2020 Allen D. Ball.  All rights reserved.
 */
package ball.game.crossword.entity;

import ball.persistence.entity.AbstractEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import static javax.persistence.FetchType.LAZY;

/**
 * {@link Header} {@link Entity}.
 *
 * {@bean.info}
 *
 * @author {@link.uri mailto:ball@hcf.dev Allen D. Ball}
 * @version $Revision$
 */
@Entity
@Table(catalog = "crossword", name = "header")
@IdClass(Header.PK.class)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
public class Header extends AbstractEntity {
    @Getter @Setter
    @Id @ManyToOne(fetch = LAZY)
    private Crossword crossword = null;

    @Getter @Setter
    @Id @Column
    private String name = null;

    @Getter @Setter
    @Column @Lob
    private String value = null;

    @NoArgsConstructor
    @EqualsAndHashCode(callSuper = false)
    @ToString
    public static class PK {
        @Getter @Setter private Crossword crossword = null;
        @Getter @Setter private String name = null;
    }
}
