package ball.game.scrabble.wordlist;
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
import ball.game.scrabble.WordList;
import java.util.Locale;

/**
 * {@link.uri https://www.wordgamedictionary.com/sowpods/download/sowpods.txt SOWPODS (Europe Scrabble Word List)}.
 *
 * @author {@link.uri mailto:ball@hcf.dev Allen D. Ball}
 */
public class SOWPODS extends WordList {
    private static final long serialVersionUID = 6591395489114961265L;

    /**
     * Sole constructor.
     */
    public SOWPODS() { super(Locale.ENGLISH); }
}
