/*
 * $Id$
 *
 * Copyright 2016 - 2019 Allen D. Ball.  All rights reserved.
 */
package ball.game.scrabble.wordlist;

import ball.game.scrabble.WordList;
import java.util.Locale;

/**
 * Scrabble Official {@link WordList} ({@link OWL21}).
 *
 * @author {@link.uri mailto:ball@hcf.dev Allen D. Ball}
 * @version $Revision$
 */
public class OWL21 extends WordList {
    private static final long serialVersionUID = -3237235754918083278L;

    /**
     * Sole constructor.
     */
    public OWL21() { super(Locale.ENGLISH); }
}
