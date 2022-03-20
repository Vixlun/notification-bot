package fun.home.findatmapi.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AtmRequest {
    private Bounds bounds;
    private Filter filters;
    private int zoom;
}
