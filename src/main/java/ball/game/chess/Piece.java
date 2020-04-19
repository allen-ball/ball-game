package ball.game.chess;
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
import lombok.Data;
import lombok.NonNull;

/**
 * Chess {@link Piece}.
 *
 * {@bean.info}
 *
 * @author {@link.uri mailto:ball@hcf.dev Allen D. Ball}
 * @version $Revision$
 */
@Data
public class Piece {
    private static final String[][] UNICODE = {
        /*         White,    Black   */
        /* K */ { "\u2654", "\u265a" },
        /* Q */ { "\u2655", "\u265b" },
        /* R */ { "\u2656", "\u265c" },
        /* B */ { "\u2657", "\u265d" },
        /* N */ { "\u2658", "\u265e" },
        /* P */ { "\u2659", "\u265f" }
    };

    @NonNull private final Rank rank;
    @NonNull private final Color color;

    @Override
    public String toString() {
        return UNICODE[rank.ordinal()][color.ordinal()];
    }

    /**
     * {@link Piece} {@link Rank}.
     */
    public enum Rank { K, Q, R, B, N, P; }
}
