package monolith2microservice.logic.decomposition.graph.component;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.File;

@Getter
@Setter
@ToString
public class NodeRepresentation {

    private long id;

    private String label;

    private String fullClassName;

    private String color;

    public NodeRepresentation(long id, String fullClassName){
        this.id = id;
        this.fullClassName = fullClassName;
        this.label = getClassNameFromFileName(fullClassName);
    }

    public NodeRepresentation(long id, String fullClassName, String hexColorCode){
        this(id, fullClassName);
        this.color = hexColorCode;
    }

    private String getClassNameFromFileName(String fileName){
        String[] elements = fileName.split(File.separator);
        return elements[elements.length -1];
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NodeRepresentation)) return false;

        NodeRepresentation that = (NodeRepresentation) o;

        if (id != that.id) return false;
        return label.equals(that.label);

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + label.hashCode();
        return result;
    }
}
