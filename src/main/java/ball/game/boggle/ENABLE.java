/*
 * $Id$
 *
 * Copyright 2016 Allen D. Ball.  All rights reserved.
 */
package ball.game.boggle;

import ball.game.WordList;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * {@link.uri https://storage.googleapis.com/google-code-archive-downloads/v2/code.google.com/dotnetperls-controls/enable1.txt Public domain}
 * {@link ENABLE} {@link WordList}.
 *
 * @author {@link.uri mailto:ball@iprotium.com Allen D. Ball}
 * @version $Revision$
 */
public class ENABLE extends WordList {
    private static final long serialVersionUID = 7864980182816482028L;

    /**
     * Sole constructor.
     */
    public ENABLE() {
        super(Locale.ENGLISH);

        ResourceBundle bundle =
            ResourceBundle.getBundle(getClass().getName(), getLocale());

        for (String key : bundle.keySet()) {
            add(key.split("[\\p{Space}]+", 2)[0]);
        }
    }
}
