package ball.game.crossword.entity;
/*-
 * ##########################################################################
 * Game Applications and Utilities
 * $Id$
 * $HeadURL$
 * %%
 * Copyright (C) 2010 - 2020 Allen D. Ball
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
