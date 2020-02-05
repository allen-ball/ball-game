package ball.game.scrabble;
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
import java.beans.ConstructorProperties;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TreeSet;

import static java.util.Comparator.comparing;
import static java.util.Objects.requireNonNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Abstract Word List base class.
 *
 * @author {@link.uri mailto:ball@hcf.dev Allen D. Ball}
 * @version $Revision$
 */
public abstract class WordList extends TreeSet<CharSequence> {
    private static final long serialVersionUID = 9123060041133450168L;

    /** @serial */ private final Locale locale;

    /**
     * Sole constructor.
     *
     * @param   locale          The {@link Locale}.
     */
    @ConstructorProperties({ "locale" })
    protected WordList(Locale locale) {
        super(comparing(CharSequence::toString, String.CASE_INSENSITIVE_ORDER));

        this.locale = requireNonNull(locale);

        ResourceBundle bundle =
            ResourceBundle.getBundle(getClass().getName(), getLocale());

        bundle.keySet()
            .stream()
            .map(t -> t.split("#", 2)[0])
            .map(t -> t.split(" ", 2)[0])
            .filter(t -> isNotBlank(t))
            .map(t -> t.trim().toUpperCase())
            .forEach(t -> this.add(t));
    }

    /**
     * Method to get the {@link Locale} for this {@link WordList}.
     *
     * @return  The {@link Locale} for this {@link WordList}.
     */
    public Locale getLocale() { return locale; }

    @Override
    public CharSequence[] toArray() { return toArray(new CharSequence[] { }); }
}
