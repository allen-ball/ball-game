/*
 * $Id$
 *
 * Copyright 2014 - 2018 Allen D. Ball.  All rights reserved.
 */
package ball.game;

import ball.util.CharSequenceOrder;
import java.beans.ConstructorProperties;
import java.util.Locale;
import java.util.SortedSet;
import java.util.TreeSet;

import static java.util.Objects.requireNonNull;

/**
 * Abstract Word List base class.
 *
 * @author {@link.uri mailto:ball@iprotium.com Allen D. Ball}
 * @version $Revision$
 */
public abstract class WordList extends TreeSet<CharSequence> {
    private static final long serialVersionUID = 1127718429632938077L;

    private final Locale locale;

    /**
     * Sole constructor.
     *
     * @param   locale          The {@link Locale}.
     */
    @ConstructorProperties({ "locale" })
    protected WordList(Locale locale) {
        super(CharSequenceOrder.CASE_INSENSITIVE);

        this.locale = requireNonNull(locale, "locale");
    }

    /**
     * Method to get the {@link Locale} for this {@link WordList}.
     *
     * @return  The {@link Locale} for this {@link WordList}.
     */
    public Locale getLocale() { return locale; }

    /**
     * Method to get subset of words of a specified length.
     *
     * @param   length          The length.
     *
     * @return  The {@link SortedSet} of this {@link WordList} of the
     *          specified length.
     */
    public SortedSet<CharSequence> subSet(int length) {
        TreeSet<CharSequence> set = new TreeSet<CharSequence>();

        for (CharSequence sequence : this) {
            if (sequence.length() == length) {
                set.add(sequence);
            }
        }

        return set;
    }

    @Override
    public CharSequence[] toArray() { return toArray(new CharSequence[] { }); }
}
