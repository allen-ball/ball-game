/*
 * $Id$
 *
 * Copyright 2016 - 2019 Allen D. Ball.  All rights reserved.
 */
package ball.game.scrabble;

import ball.game.WordList;
import java.util.Locale;

/**
 * Scrabble Official {@link WordList} ({@link OWL21}).
 *
 * @author {@link.uri mailto:ball@hcf.dev Allen D. Ball}
 * @version $Revision$
 */
public class OWL21 extends WordList {
    private static final long serialVersionUID = 1381786432796311780L;

    /**
     * Sole constructor.
     */
    public OWL21() { super(Locale.ENGLISH); }
}
