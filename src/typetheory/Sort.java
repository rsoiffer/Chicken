package typetheory;

import typetheory.Term.TermCategory.AtomicTermCategory;

public abstract class Sort extends AtomicTermCategory {

    public static final Sort Set = new Sort() {
        @Override
        public String printTerm(Term e) {
            return "Set";
        }
    };

    public static final Sort Prop = new Sort() {
        @Override
        public String printTerm(Term e) {
            return "Prop";
        }
    };

    public static class Type extends Sort {

        public final int i;

        public Type(int i) {
            this.i = i;
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
            final Type other = (Type) obj;
            if (this.i != other.i) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 89 * hash + this.i;
            return hash;
        }

        @Override
        public String printTerm(Term e) {
            return "Type(" + i + ")";
        }
    }
}
