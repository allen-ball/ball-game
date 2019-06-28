/*
 * $Id$
 *
 * Copyright 2019 Allen D. Ball.  All rights reserved.
 */
package ball.game.scrabble.wordlist;

import ball.game.scrabble.WordList;
import java.util.Locale;

/**
 * {@link.uri https://www.wordgamedictionary.com/sowpods/download/sowpods.txt SOWPODS (Europe Scrabble Word List)}
 *
 * @author {@link.uri mailto:ball@hcf.dev Allen D. Ball}
 * @version $Revision$
 */
public class SOWPODS extends WordList {
    private static final long serialVersionUID = 6591395489114961265L;

    /**
     * Sole constructor.
     */
    public SOWPODS() { super(Locale.ENGLISH); }
}
