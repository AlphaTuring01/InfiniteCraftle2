package lab.prog.infinitecraftle.domain;

import java.io.Serializable;

/**
 * Element class that
 * represents an element.
 * Contains the element's name, depth, id, emoji, and parents
 */
public class Element implements Serializable {
    private String name;
    private int depth, id;
    private String emoji;
    private Element parent1, parent2;

    /**
     * Constructor for Element class
     * @param name name of the element
     * @param emoji emoji of the element
     * @param depth depth of the element
     * @param parent1 first parent of the element
     * @param parent2 second parent of the element
     */
    public Element(String name, String emoji, int depth, Element parent1, Element parent2) {
        this.id = 0;
        this.name = name;
        this.emoji = emoji;
        this.depth = depth;
        this.parent1 = parent1;
        this.parent2 = parent2;
    }

    /**
     * Element constructor that takes a name and emoji
     * @param name String with the element's name
     * @param emoji String with the element's emoji
     */
    public Element(String name, String emoji) {
        this(name, emoji, 0, null, null);
    }

    /**
     *  Default constructor for Element
     */
    public Element() {
        this(null, null);
    }

    /**
     * Element constructor that takes a name, emoji, and depth
     * @param name String with the element's name
     * @param emoji String with the element's emoji
     * @param depth int with the element's depth
     */
    public Element(String name, String emoji, int depth) {
        this(name, emoji, depth, null, null);
    }

    /**
     * Getter for the element's name
     * @return String with the element's name
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for the element's name
     * @param name String with the element's name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for the element's depth
     * @return int with the element's depth
     */
    public int getDepth() {
        return depth;
    }

    /**
     * Setter for the element's depth
     * @param depth int with the element's depth
     */
    public void setDepth(int depth) {
        this.depth = depth;
    }

    /**
     * Getter for the element's id
     * @return int with the element's id
     */
    public int getId() {
        return id;
    }

    /**
     * Setter for the element's id
     * @param id int with the element's id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Getter for the element's emoji
     * @return String with the element's emoji
     */
    public String getEmoji() {
        return emoji;
    }

    /**
     * Setter for the element's emoji
     * @param emoji String with the element's emoji
     */
    public void setEmoji(String emoji) {
        this.emoji = emoji;
    }

    /**
     * Getter for the first parent of the element
     * @return Element with the element's first parent
     */
    public Element getParent1() {
        return parent1;
    }

    /**
     * Setter for the element's first parent
     * @param parent1 Element with the element's first parent
     */
    public void setParent1(Element parent1) {
        this.parent1 = parent1;
    }

    /**
     * Getter for the second parent of the element
     * @return Element with the element's second parent
     */
    public Element getParent2() {
        return parent2;
    }

    /**
     * Setter for the element's second parent
     * @param parent2 Element with the element's second parent
     */
    public void setParent2(Element parent2) {
        this.parent2 = parent2;
    }
}
