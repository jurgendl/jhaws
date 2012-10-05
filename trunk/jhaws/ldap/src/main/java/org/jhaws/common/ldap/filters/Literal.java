package org.jhaws.common.ldap.filters;

/**
 * letterlijke filter, gebruik deze voor custom filters
 *
 * @author Jurgen De Landsheer
 */
public class Literal implements Filter {
    /** query (letterlijk) */
    private String literal;

/**
     * Creates a new Literal object.
     */
    public Literal() {
        super();
    }

/**
     * Creates a new Literal object.
     *
     * @param literal query (letterlijk)
     */
    public Literal(String literal) {
        super();
        setLiteral(literal);
    }

    /**
     * gets literal
     *
     * @return Returns the literal.
     */
    public String getLiteral() {
        return literal;
    }

    /**
     * sets literal
     *
     * @param literal The literal to set.
     */
    public void setLiteral(String literal) {
        this.literal = literal;
    }

    /**
     * wordt gebruikt om filter op te bouwen
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return literal;
    }
}
