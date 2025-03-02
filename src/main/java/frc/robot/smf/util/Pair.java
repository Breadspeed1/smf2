package frc.robot.smf.util;

public class Pair<E1, E2> {
    public E1 left;
    public E2 right;

    public Pair(E1 left, E2 right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Pair<?, ?> other) {
            return other.left.equals(left) && other.right.equals(right);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return left.hashCode() * right.hashCode();
    }
}
