package ball.game.scrabble;
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
import ball.game.scrabble.wordlist.OWL;
import java.util.Collections;
import java.util.Set;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Scrabble {@link Game}.
 *
 * {@bean.info}
 *
 * @author {@link.uri mailto:ball@hcf.dev Allen D. Ball}
 */
@NoArgsConstructor @Getter @Setter
public class Game {
    private final Set<CharSequence> wordList = Collections.unmodifiableSet(new OWL().keySet());
    private final Board board = new Board();
    private final Bag bag = new Bag();

    @Override
    public String toString() { return board.toString(); }
}
