import java.util.Objects;

/**
 * Abstract base class representing a generic Person in the system.
 * Demonstrates Object-Oriented Programming principles: Abstraction and Encapsulation.
 */
public abstract class Person {

    protected int id;
    protected String name;

    /**
     * Constructs a new Person with an ID and name.
     *
     * @param id   Unique identification number
     * @param name Full name of the person
     */
    public Person(int id, String name) {
        this.id = id;
        this.name = name != null ? name.trim() : "";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name != null ? name.trim() : "";
    }

    /**
     * Abstract method defining the role description of the person.
     * Demonstrates polymorphism and abstraction.
     *
     * @return Description of person's role
     */
    public abstract String getRoleDescription();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return id == person.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
