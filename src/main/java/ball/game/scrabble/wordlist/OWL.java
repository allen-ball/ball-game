/*
 * $Id$
 *
 * Copyright 2010 - 2019 Allen D. Ball.  All rights reserved.
 */
package ball.game.scrabble.wordlist;

import ball.game.scrabble.WordList;
import java.util.Locale;

/**
 * Scrabble Official {@link WordList} ({@link OWL}).
 *
 * @author {@link.uri mailto:ball@hcf.dev Allen D. Ball}
 * @version $Revision$
 */
public class OWL extends WordList {
    private static final long serialVersionUID = -2763151905579309417L;

    /**
     * Sole constructor.
     */
    public OWL() { super(Locale.ENGLISH); }
}
