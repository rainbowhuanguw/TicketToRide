package route.shortestpaths;

import city.City;
import route.Route;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A result object returned by a {@link ShortestPathFinder}.
 */
public interface ShortestPath<V extends City, E extends Route<V>> {
    /**
     * Returns true iff a path was found between the given start and end vertices.
     */
    boolean exists();

    /**
     * Returns the list of edges in the shortest path (in order from start to end).
     *
     * @throws UnsupportedOperationException if no path exists from start to end
     */
    List<E> edges();

    /**
     * Returns the list of vertices in the shortest path (in order from start to end).
     *
     * @throws UnsupportedOperationException if no path exists from start to end
     */
    List<V> vertices();

    /**
     * Returns the total weight of the edges in the shortest path.
     *
     * @throws UnsupportedOperationException if no path exists from start to end
     */
    default double totalWeight() {
        return edges().stream().mapToDouble(E::getNumOfCars).sum();
    }

    class Success<V extends City, E extends Route<V>> implements ShortestPath<V, E> {
        private final List<E> edges;

        /**
         * @param edges The list of edges in this shortest path.
         * @throws IllegalArgumentException if edges is empty or null
         */
        public Success(List<E> edges) {
            if (edges == null || edges.isEmpty()) {
                throw new IllegalArgumentException("Input edges must not be null or empty.");
            }
            this.edges = edges;
        }

        @Override
        public boolean exists() {
            return true;
        }

        @Override
        public List<E> edges() {
            return this.edges;
        }

        @Override
        public List<V> vertices() {
            return Stream.concat(
                    Stream.of(this.edges.get(0).getCity1()),
                    this.edges.stream().map(E::getCity2)
            ).collect(Collectors.toList());
        }
    }

    /**
     * A path representing a single vertex (same start and end).
     */
    class SingleVertex<V extends City, E extends Route<V>> implements ShortestPath<V, E> {
        private final List<V> vertex;

        /**
         * @param vertex the only vertex in the path
         */
        public SingleVertex(V vertex) {
            this.vertex = List.of(vertex);
        }

        @Override
        public boolean exists() {
            return true;
        }

        @Override
        public List<E> edges() {
            return List.of();
        }

        @Override
        public List<V> vertices() {
            return this.vertex;
        }
    }

    class Failure<V extends City, E extends Route<V>> implements ShortestPath<V, E> {
        @Override
        public boolean exists() {
            return false;
        }

        @Override
        public List<E> edges() {
            throw new UnsupportedOperationException("No path found.");
        }

        @Override
        public List<V> vertices() {
            throw new UnsupportedOperationException("No path found.");
        }
    }
}