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
 * Scrabble {@link OSPD41} {@link WordList}.
 *
 * @author {@link.uri mailto:ball@hcf.dev Allen D. Ball}
 * @version $Revision$
 */
public class OSPD41 extends WordList {
    private static final long serialVersionUID = -6267940304364188970L;

    /**
     * Sole constructor.
     */
    public OSPD41() {
        super(Locale.ENGLISH);

        ResourceBundle bundle =
            ResourceBundle.getBundle(getClass().getName(), getLocale());

        for (String key : bundle.keySet()) {
            add(key.split("[\\p{Space}]+", 2)[0]);
        }
    }
}
