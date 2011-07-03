/*
 * $Id$
 *
 * Copyright 2010, 2011 Allen D. Ball.  All rights reserved.
 */
package iprotium.game.card;

/**
 * Playing card.
 *
 * @author <a href="mailto:ball@iprotium.com">Allen D. Ball</a>
 * @version $Revision$
 */
public class Card implements Comparable<Card> {
    private final Suit suit;
    private final Rank rank;

    /**
     * Sole constructor.
     *
     * @param   suit            The {@link Card} {@link Suit}.
     * @param   rank            The {@link Card} {@link Rank}.
     */
    public Card(Suit suit, Rank rank) {
        this.rank = rank;
        this.suit = suit;
    }

    /**
     * Method to get the {@link Card} {@link Suit}.
     *
     * @return  {@link Suit}
     */
    public Suit getSuit() { return suit; }

    /**
     * Method to get the {@link Card} {@link Rank}.
     *
     * @return  {@link Rank}
     */
    public Rank getRank() { return rank; }

    /**
     * Method to get the {@link Card} {@link Color}.
     *
     * @return  {@link Suit#getColor()}
     */
    public Color getColor() {
        Suit suit = getSuit();

        return (suit != null) ? suit.getColor() : null;
    }

    @Override
    public int compareTo(Card that) {
        int difference = 0;

        if (difference == 0) {
            difference = this.getSuit().compareTo(that.getSuit());
        }

        if (difference == 0) {
            difference = this.getRank().compareTo(that.getRank());
        }

        return difference;
    }

    @Override
    public boolean equals(Object object) {
        return ((object instanceof Card)
                    ? equals((Card) object)
                    : super.equals(object));
    }

    private boolean equals(Card that) {
        return (this.getSuit().equals(that.getSuit())
                && this.getRank().equals(that.getRank()));
    }

    @Override
    public int hashCode() {
        return getSuit().hashCode() ^ getRank().hashCode();
    }

    @Override
    public String toString() {
        return getRank().toString() + "-" + getSuit().toString();
    }
}
