package fun.home.findatmapi.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Bounds {
    private Coordinate bottomLeft;
    private Coordinate topRight;

    public Bounds(double bottomLeftLat, double bottomLeftLng, double topRightLat, double topRightLng) {
        this.bottomLeft = new Coordinate(bottomLeftLat, bottomLeftLng);
        this.topRight = new Coordinate(topRightLat, topRightLng);
    }
}
