package io.liter.web.api.collection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MediaFile {

    private String name;

    private String type;

    private String path;

    private String hashCode;
}
