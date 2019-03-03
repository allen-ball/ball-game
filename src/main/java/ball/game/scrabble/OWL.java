/*
 * $Id$
 *
 * Copyright 2010 - 2014 Allen D. Ball.  All rights reserved.
 */
package ball.game.scrabble;

import ball.game.WordList;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Scrabble Official {@link WordList} ({@link OWL}).
 *
 * @author {@link.uri mailto:ball@hcf.dev Allen D. Ball}
 * @version $Revision$
 */
public class OWL extends WordList {
    private static final long serialVersionUID = -6945700016060061297L;

    /**
     * Sole constructor.
     */
    public OWL() {
        super(Locale.ENGLISH);

        ResourceBundle bundle =
            ResourceBundle.getBundle(getClass().getName(), getLocale());

        addAll(bundle.keySet());
    }
}
