package ball.game.crossword.entity;
/*-
 * ##########################################################################
 * Game Applications and Utilities
 * %%
 * Copyright (C) 2010 - 2022 Allen D. Ball
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ##########################################################################
 */
import ball.game.crossword.Direction;
import ball.persistence.entity.AbstractEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
 * {@link Clue} {@link Entity}.
 *
 * {@bean.info}
 *
 * @author {@link.uri mailto:ball@hcf.dev Allen D. Ball}
 */
@Entity
@Table(catalog = "crossword", name = "clue")
@IdClass(Clue.PK.class)
@NoArgsConstructor @EqualsAndHashCode(callSuper = false) @ToString
public class Clue extends AbstractEntity {
    @Id @ManyToOne(fetch = LAZY)
    @Getter @Setter
    private Crossword crossword = null;

    @Id @Column(length = 6) @Enumerated(EnumType.STRING)
    @Getter @Setter
    private Direction direction = null;

    @Id @Column
    @Getter @Setter
    private int index = -1;

    @Column @Lob
    @Getter @Setter
    private String text = null;

    @Column
    @Getter @Setter
    private String answer = null;

    @NoArgsConstructor @EqualsAndHashCode(callSuper = false) @ToString
    public static class PK {
        @Getter @Setter private Crossword crossword = null;
        @Getter @Setter private Direction direction = null;
        @Getter @Setter private int index = -1;
    }
}
