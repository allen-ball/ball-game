/*
 * $Id$
 *
 * Copyright 2019, 2020 Allen D. Ball.  All rights reserved.
 */
package ball.game.scrabble.wordlist;

import ball.game.scrabble.WordList;
import java.util.Locale;

/**
 * {@link.uri https://www.wordgamedictionary.com/twl06/download/twl06.txt TWL06 Scrabble Word List}.
 *
 * @author {@link.uri mailto:ball@hcf.dev Allen D. Ball}
 * @version $Revision$
 */
public class TWL06 extends WordList {
    private static final long serialVersionUID = -3236174270784996984L;

    /**
     * Sole constructor.
     */
    public TWL06() { super(Locale.ENGLISH); }
}
