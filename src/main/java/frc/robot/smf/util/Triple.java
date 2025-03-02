package frc.robot.smf.util;

public class Triple<E1, E2, E3> {
    public E1 left;
    public E2 middle;
    public E3 right;

    public Triple(E1 left, E2 middle, E3 right) {
        this.left = left;
        this.middle = middle;
        this.right = right;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Triple<?, ?, ?> other) {
            return other.left.equals(left) && other.middle.equals(middle) && other.right.equals(right);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return left.hashCode() * middle.hashCode() * right.hashCode();
    }
}
