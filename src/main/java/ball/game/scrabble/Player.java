/*
 * $Id$
 *
 * Copyright 2014 - 2016 Allen D. Ball.  All rights reserved.
 */
package ball.game.scrabble;

/**
 * Scrabble {@link Player} abstract base class.
 *
 * {@bean.info}
 *
 * @author {@link.uri mailto:ball@hcf.dev Allen D. Ball}
 * @version $Revision$
 */
public abstract class Player {
    private String name = getClass().getSimpleName();
    private Rack rack = new Rack();

    /**
     * Sole constructor.
     */
    protected Player() { }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Rack getRack() { return rack; }

    @Override
    public String toString() { return rack.toString(); }
}
