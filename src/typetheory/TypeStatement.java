package typetheory;

import java.util.Objects;

public class TypeStatement {

    public final Term t1, t2;

    public TypeStatement(Term t1, Term t2) {
        this.t1 = t1;
        this.t2 = t2;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TypeStatement other = (TypeStatement) obj;
        if (!Objects.equals(this.t1, other.t1)) {
            return false;
        }
        if (!Objects.equals(this.t2, other.t2)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + Objects.hashCode(this.t1);
        hash = 47 * hash + Objects.hashCode(this.t2);
        return hash;
    }

    @Override
    public String toString() {
        return t1 + ":" + t2;
    }
}
