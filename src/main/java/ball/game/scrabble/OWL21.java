/*
 * $Id$
 *
 * Copyright 2016 Allen D. Ball.  All rights reserved.
 */
package ball.game.scrabble;

import ball.game.WordList;
import java.util.Locale;
import java.util.ResourceBundle;

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
    public OWL21() {
        super(Locale.ENGLISH);

        ResourceBundle bundle =
            ResourceBundle.getBundle(getClass().getName(), getLocale());

        for (String key : bundle.keySet()) {
            add(key.split("[\\p{Space}]+", 2)[0]);
        }
    }
}
